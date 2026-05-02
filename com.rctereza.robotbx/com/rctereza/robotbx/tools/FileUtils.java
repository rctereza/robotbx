package com.rctereza.robotbx.tools;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.Certificate;

public class FileUtils {

//	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static void saveCertificatePathChosen(String path) {
		Preferences prefs = Preferences.userNodeForPackage(FileUtils.class);
		prefs.put(Constants.CERTIFICATE_PATH, path);
	}

	public static String getCertificatePathSaved() {
		Preferences prefs = Preferences.userNodeForPackage(FileUtils.class);
		return prefs.get(Constants.CERTIFICATE_PATH, "");
	}

	public static void removeCertificatePathChosen() {
		Preferences prefs = Preferences.userNodeForPackage(FileUtils.class);
		prefs.remove(Constants.CERTIFICATE_PATH);
	}

	public static List<Certificate> getListOfCertificates(String path) {
		return getList(path);
	}

	public static List<Certificate> getListOfCertificates() {
		return getList(Constants.PROGRAM_CERTIFICATES);
	}

	private static List<Certificate> getList(String path) {
		Integer counter = 1;
		ArrayList<Certificate> list = new ArrayList<>();
		Path folder = Paths.get(path);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.pfx")) {
			for (Path entry : stream) {
				String filename = entry.getFileName().toString();
				String filepath = entry.getParent().toString();
				list.add(new Certificate(counter++, filename, filepath, null, null, null, null, null, null));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void openFolderWithExplorer(String folderPath) {
		Path path = Paths.get(folderPath);
		if (Files.exists(path) && Files.isDirectory(path)) {
			try {
				new ProcessBuilder("explorer.exe", folderPath).start();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "O caminho informado não existe ou não é um diretorio: " + folderPath,
					"Atenção", JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void deleteDirectory(String sourceFolder) throws IOException {
		Path directory = Paths.get(sourceFolder);

		if (!Files.exists(directory) || !Files.isDirectory(directory)) {
			throw new IllegalArgumentException("Path must be an existing directory: " + directory);
		}

		// Walk through the directory tree and delete files/subfolders
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.deleteIfExists(file); // Delete file
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (!dir.equals(directory)) { // Don't delete the root folder
					Files.deleteIfExists(dir);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static void moveDirectory(String sourceFolder, String targetFolder) throws IOException {
		Path source = Paths.get(sourceFolder);
		Path target = Paths.get(targetFolder);

		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path targetDir = target.resolve(source.relativize(dir));
				Files.createDirectories(targetDir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path targetFile = target.resolve(source.relativize(file));
				Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});

		// After copying, delete source
		deleteDirectory(sourceFolder);

//		logger.info("Directory moved successfully!");

	}
}
