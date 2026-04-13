package com.rctereza.robotbx.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.models.ReceitaBx;

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
				String password = getCertificatePassword(filename); 
				String customer = getCertificateCustomer(filename);
				list.add(new Certificate(counter++, filename, filepath, password, customer));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static String getCertificatePassword(String filename) {
		String result = "";
		/*
		System.out.println(filename);
		if (filename.toUpperCase().indexOf("_SENHA") > 0) {
			result = filename.substring(filename.toUpperCase().indexOf("_SENHA") + 7,filename.indexOf("."));
		}
		else if (filename.toUpperCase().indexOf(" SENHA") > 0) {
			result = filename.substring(filename.toUpperCase().indexOf(" SENHA") + 7,filename.indexOf("."));
		}
		else if (filename.toUpperCase().indexOf("- SENHA") > 0) {
			result = filename.substring(filename.toUpperCase().indexOf("- SENHA") + 8,filename.indexOf("."));
		}
		else if (filename.toUpperCase().indexOf("(SENHA") > 0) {
			result = filename.substring(filename.toUpperCase().indexOf("(SENHA") + 7,filename.indexOf("."));
		} 
		*/
		return result;
	}
	
	private static String getCertificateCustomer(String filename) {
		String result = filename;
		/*
		if (filename.toUpperCase().indexOf("_SENHA") > 0) {
			result = filename.substring(0,filename.toUpperCase().indexOf("_SENHA"));
		}
		else if (filename.toUpperCase().indexOf(" SENHA") > 0) {
			result = filename.substring(0,filename.toUpperCase().indexOf(" SENHA"));
		}
		else if (filename.toUpperCase().indexOf("- SENHA") > 0) {
			result = filename.substring(0,filename.toUpperCase().indexOf("- SENHA"));
		}
		else if (filename.toUpperCase().indexOf("(SENHA") > 0) {
			result = filename.substring(0,filename.toUpperCase().indexOf("(SENHA"));
		}
		*/
		return result;
	}
	
	public static void saveReceitaBx(ReceitaBx object) {
		try (ObjectOutputStream out =
		         new ObjectOutputStream(new FileOutputStream("config.dat"))) {
		    out.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ReceitaBx loadReceitaBx() {
		ReceitaBx receitabx = new ReceitaBx();
		
		try (ObjectInputStream in =
		         new ObjectInputStream(new FileInputStream("config.dat"))) {
			receitabx = (ReceitaBx) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			if (!(e instanceof FileNotFoundException)) {
				e.printStackTrace();
			}
		}
		
		return receitabx;
	}

}
