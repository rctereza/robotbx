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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.Certificate;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class FileUtils {

//	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	// ----------------------------------------------------------------------------
	// SOFTWARE
	public static void saveSoftwarePathChosen(String path) {
		Preferences prefs = Preferences.userNodeForPackage(FileUtils.class);
		prefs.put(Constants.SOFTWARE_PATH, path);
	}

	public static String getSoftwarePathSaved() {
		Preferences prefs = Preferences.userNodeForPackage(FileUtils.class);
		return prefs.get(Constants.SOFTWARE_PATH, "");
	}

	public static void removeSoftwarePathChosen() {
		Preferences prefs = Preferences.userNodeForPackage(FileUtils.class);
		prefs.remove(Constants.SOFTWARE_PATH);
	}
	
	public static DefaultComboBoxModel<String> getModelOfSoftwares() {
		return getModelOfSoftwares(getSoftwarePathSaved());
	}

	public static DefaultComboBoxModel<String> getModelOfSoftwares(String path) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		List<String> list = getListOfSoftwares(path);
		if (list.size() > 0) {
			saveSoftwarePathChosen(path);
			model.addAll(list);
		}
		return model;
	}

	public static List<String> getListOfSoftwares(String path) {
		return getList2(path);
	}

	public static List<String> getListOfSoftwares() {
		return getList2(getCertificatePathSaved());
	}

	private static List<String> getList2(String path) {
		ArrayList<String> list = new ArrayList<>();
		Path folder = Paths.get(path);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.jar")) {
			for (Path entry : stream) {
				String filename = entry.getFileName().toString();
				String filepath = entry.getParent().toString();
				list.add(filepath + "\\" + filename);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// ----------------------------------------------------------------------------
	// CERTIFICATE
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

	public static DefaultComboBoxModel<Certificate> getModelOfCertificates() {
		return getModelOfCertificates(getCertificatePathSaved());
	}

	public static DefaultComboBoxModel<Certificate> getModelOfCertificates(String path) {
		DefaultComboBoxModel<Certificate> model = null;
		List<Certificate> list = getListOfCertificates(path);
		if (list.size() > 0) {
			saveCertificatePathChosen(path);
			model = new DefaultComboBoxModel<>();
			model.addAll(list);
		}
		return model;
	}

	public static List<Certificate> getListOfCertificates(String path) {
		return getList(path);
	}

	public static List<Certificate> getListOfCertificates() {
		return getList(getCertificatePathSaved());
	}

	private static List<Certificate> getList(String path) {
		Integer counter = 1;
		ArrayList<Certificate> list = new ArrayList<>();
		Path folder = Paths.get(path);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.pfx")) {
			for (Path entry : stream) {
				String filename = entry.getFileName().toString();
				String filepath = entry.getParent().toString();
				list.add(new Certificate(counter++, filename, filepath, null, null, null, null, null, null, null, null));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static int getCertificateIndex(String valueToFind) {
		int result = -1;
		List<Certificate> list = getListOfCertificates(getCertificatePathSaved());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Certificate cert = list.get(i);
				if (cert.toString().equals(valueToFind)) {
					result = i;
					break; // stop once found
				}
			}
		}
		return result;
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

	public static void createDirectory(String folder) throws IOException {
		Path path = Paths.get(folder);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
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

	public static void copyDirectory(String sourceFolder, String targetFolder) throws IOException {
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
		//deleteDirectory(sourceFolder);

		//logger.info("Directory moved successfully!");
	}
	
	public static String getDocumentsPath() {  
		
		String path = "";
		
        // Initialize COM (required for SHGetKnownFolderPath)  
        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);  
 
        try {  
        	
            Guid.GUID folderId = new Guid.GUID("{FDD39AD0-238F-46AF-ADB4-6C85480369C7}");  
            
            PointerByReference pPath = new PointerByReference();  
 
            int result = Shell32.INSTANCE.SHGetKnownFolderPath(folderId, 0, null, pPath);  
            
            if (result != 0) { // 0 = S_OK  
                throw new Win32Exception(result);  
            }  
 
            // Convert the returned wide string to a Java String  
            path = pPath.getValue().getWideString(0);  
            
            // Free the allocated memory  
            Ole32.INSTANCE.CoTaskMemFree(pPath.getValue());  
            
        } finally {  
            Ole32.INSTANCE.CoUninitialize();  
        }
        
        return path;  
    }  

	// Define the Shell32 library interface  
	public interface Shell32 extends StdCallLibrary {  
	    Shell32 INSTANCE = Native.load("shell32", Shell32.class, W32APIOptions.DEFAULT_OPTIONS);  
	 
	    // SHGetKnownFolderPath prototype  
	    int SHGetKnownFolderPath(  
	        Guid.GUID rfid,          		// Folder ID (FOLDERID_Documents)  
	        int dwFlags,             		// Flags (0 for default)  
	        WinNT.HANDLE hToken,    		// User token (null for current user)  
	        PointerByReference ppszPath  	// Output: Pointer to the path string  
	    );  
	} 
}
