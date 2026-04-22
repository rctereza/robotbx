package com.rctereza.robotbx.tools;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.Certificate;

public class FileUtils {

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

	public static void clearDirectory(Path directory) throws IOException {

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
}
