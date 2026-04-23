package com.rctereza.robotbx.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.rctereza.robotbx.exceptions.InvalidCertificate;
import com.rctereza.robotbx.wrappers.Ref;

public class ValidatePfx {

	private static Ref<com.rctereza.robotbx.models.Certificate> cert = null;
	
	private static KeyStore keystore = null;
	private static PrivateKey privateKey = null;
	private static Certificate certificate = null;

	private static String pass = "";
	private static String alias = "";
	private static String subject = "";
	private static String issuer = "";
	private static Date validFrom = null;
	private static Date validTo = null;
	
	private static Boolean okay = false;

//	public ValidatePfx(Ref<com.rctereza.robotbx.models.Certificate> cert, String password) {
//		this.cert = cert;
//		this.filePath = cert.get().getAbsolutePath();
//		this.password = password;
//	}

	public static void load(Ref<com.rctereza.robotbx.models.Certificate> certificate, String password) throws InvalidCertificate {
		String result = check(certificate,password);
		if (!result.equals("")) {
			throw new InvalidCertificate(result);
		}
	}
	
	public static String check(Ref<com.rctereza.robotbx.models.Certificate> certificate, String password) {

		String result = "";
		
		cert = certificate;
		pass = password;
		okay = false;

		try {
			checkPassword();
			checkExpirationDate();
			updateCertificate();
			okay = true;

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
				| UnrecoverableKeyException e) {

			result = "Não foi possivel validar o certificado! Verifique se o password esta correto. [" + e.getMessage()
					+ "]";

		} catch (DateTimeException e) {

			result = e.getMessage();

		}

		return result;
	}

	private static void checkPassword() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException {

		keystore = KeyStore.getInstance("PKCS12");

		try (FileInputStream fis = new FileInputStream(cert.get().getAbsolutePath())) {
			keystore.load(fis, pass.toCharArray());

			alias = keystore.aliases().nextElement();

			privateKey = (PrivateKey) keystore.getKey(alias, pass.toCharArray());

			certificate = keystore.getCertificate(alias);

			X509Certificate x509 = (X509Certificate) certificate;

			subject = x509.getSubjectX500Principal().toString();
			issuer = x509.getIssuerX500Principal().toString();
			validFrom = x509.getNotBefore();
			validTo = x509.getNotAfter();
		}

	}

	private static void checkExpirationDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		LocalDate inputDate = Instant.ofEpochMilli(getValidTo().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

		LocalDate today = LocalDate.now();

//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		System.out.println("Today ["+ today.format(dtf) + "] [Validade de " + sdf.format(getValidFrom()) + " até " + sdf.format(getValidTo()));

		if (inputDate.isBefore(today)) {
			throw new DateTimeException("O certificado está expirado! [Validade de " + sdf.format(getValidFrom())
					+ " até " + sdf.format(getValidTo()));
		}

	}

	private static void updateCertificate() {
		com.rctereza.robotbx.models.Certificate updCert = new com.rctereza.robotbx.models.Certificate(cert.get().ID(),
				cert.get().NAME(), cert.get().PATH(), pass, getAlias(), getSubject(), getIssuer(), getValidFrom(), getValidTo());
		cert.set(updCert);		
	}

	
	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static Certificate getCertificate() {
		return certificate;
	}

	public static String getAlias() {
		return alias;
	}

	public static String getSubject() {
		return subject;
	}

	public static String getIssuer() {
		return issuer;
	}

	public static Date getValidFrom() {
		return validFrom;
	}

	public static Date getValidTo() {
		return validTo;
	}
	
	public static Boolean isValid() {
		return okay;
	}

}
