package com.rctereza.robotbx.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.function.Supplier;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.rctereza.robotbx.mutables.Ref;

public class CryptoUtils {

	private static final String VERSION = "ENCV1"; // version header
	private static final int IV_LENGTH = 12; // recommended for GCM
	private static final int TAG_LENGTH = 128; // authentication tag

	public static SecretKey getKeyFromPassword(String password, byte[] salt)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	public static byte[] generateSalt() {
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		return salt;
	}

	public static void saveEncryptedCBC(Object obj, String password, String filePath)
			throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException {

		byte[] salt = CryptoUtils.generateSalt();
		SecretKey key = CryptoUtils.getKeyFromPassword(password, salt);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);

		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

		try (FileOutputStream fos = new FileOutputStream(filePath)) {

			// Save salt + IV first (needed for decryption)
			fos.write(salt);
			fos.write(iv);

			CipherOutputStream cos = new CipherOutputStream(fos, cipher);

			try (ObjectOutputStream oos = new ObjectOutputStream(cos)) {
				oos.writeObject(obj);
			}
		}
	}

	public static Object loadEncryptedCBC(String password, String filePath)
			throws ClassNotFoundException, IOException, InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {

		try (FileInputStream fis = new FileInputStream(filePath)) {

			byte[] salt = fis.readNBytes(16);
			byte[] iv = fis.readNBytes(16);

			SecretKey key = CryptoUtils.getKeyFromPassword(password, salt);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

			CipherInputStream cis = new CipherInputStream(fis, cipher);

			try (ObjectInputStream ois = new ObjectInputStream(cis)) {
				return ois.readObject();
			}
		}
	}

	public static void saveEncryptedGCM(Object obj, String password, String filePath)
			throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException {

		byte[] salt = CryptoUtils.generateSalt();
		SecretKey key = CryptoUtils.getKeyFromPassword(password, salt);

		byte[] iv = new byte[IV_LENGTH];
		new SecureRandom().nextBytes(iv);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		
		byte[] versionBytes = VERSION.getBytes(StandardCharsets.UTF_8);
		
		  // 🔒 Authenticate header
	    cipher.updateAAD(versionBytes);

	    File file = new File(filePath);
	    if (file.getParentFile() != null) {
	        file.getParentFile().mkdirs();
	    }

		try (FileOutputStream fos = new FileOutputStream(filePath)) {

			// Write header
			fos.write(versionBytes);
			fos.write(salt);
			fos.write(iv);

			try (CipherOutputStream cos = new CipherOutputStream(fos, cipher);
					ObjectOutputStream oos = new ObjectOutputStream(cos)) {
				oos.writeObject(obj);
				oos.flush();
			}
		}
	}

	public static <T> T loadEncryptedGCM(String password, String filePath, Class<T> clazz, Supplier<T> defaultSupplier)
			throws ClassNotFoundException, IOException, InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {

		/*
		 * What Changes from CBC Version vs CGM Version Integrity Protection If someone
		 * modifies even 1 byte, this happens: javax.crypto.AEADBadTagException: Tag
		 * mismatch! That means: File was tampered with OR Wrong password Also adding
		 * file versioning (VERY useful) This helps you upgrade your format later
		 * without breaking old files.
		 */

		try (FileInputStream fis = new FileInputStream(filePath)) {

			byte[] header = fis.readNBytes(VERSION.length());
			String version = new String(header, StandardCharsets.UTF_8);
			

			if (!version.equals(VERSION)) {
				throw new IllegalStateException("Unsupported file version");
			}

			byte[] salt = fis.readNBytes(16);
			byte[] iv = fis.readNBytes(IV_LENGTH);

			SecretKey key = CryptoUtils.getKeyFromPassword(password, salt);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

			cipher.init(Cipher.DECRYPT_MODE, key, spec);
			
			cipher.updateAAD(header);

			try (CipherInputStream cis = new CipherInputStream(fis, cipher);
					ObjectInputStream ois = new ObjectInputStream(cis)) {

				Object obj = ois.readObject(); // throws if tampered or wrong password

				if (!clazz.isInstance(obj)) {
					throw new IllegalStateException(
							"Invalid type. Expected: " + clazz.getName() + ", Found: " + obj.getClass().getName());
				}

				return clazz.cast(obj);
			}

		} catch (FileNotFoundException e) {
			return defaultSupplier.get();
		}
	}
	
	public static <T> Ref<T> loadRef(
	        String password,
	        String file,
	        Class<T> clazz,
	        Supplier<T> supplier) throws Exception {

	    return new Ref<>(CryptoUtils.loadEncryptedGCM(password, file, clazz, supplier));
	}
}
