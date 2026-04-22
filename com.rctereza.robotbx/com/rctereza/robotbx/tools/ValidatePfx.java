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

import com.rctereza.robotbx.mutables.Ref;

public class ValidatePfx {

	private Ref<com.rctereza.robotbx.models.Certificate> cert;

	private KeyStore keystore;
	private PrivateKey privateKey;
	private Certificate certificate;

	private String filePath;
	private String password;
	private String alias;
	private String subject;
	private String issuer;
	private Date validFrom;
	private Date validTo;

	public ValidatePfx(Ref<com.rctereza.robotbx.models.Certificate> cert, String password) {
		this.cert = cert;
		this.filePath = certificate.toString();
		this.password = password;
	}

	public String check() {

		String result = "";

		try {
			checkPassword();
			checkExpirationDate();
			updateCertificate();

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
				| UnrecoverableKeyException e) {

			result = "Não foi possivel validar o certificado! Verifique se o password esta correto. [" + e.getMessage()
					+ "]";

		} catch (DateTimeException e) {

			result = e.getMessage();

		}

		return result;
	}

	private void updateCertificate() {
		com.rctereza.robotbx.models.Certificate updCert = new com.rctereza.robotbx.models.Certificate(cert.get().ID(),
				cert.get().NAME(), cert.get().PATH(), this.password, getAlias(), getSubject(), getIssuer(), getValidFrom(), getValidTo());
		cert.set(updCert);		
	}

	private void checkPassword() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException {

		keystore = KeyStore.getInstance("PKCS12");

		try (FileInputStream fis = new FileInputStream(filePath)) {
			keystore.load(fis, password.toCharArray());

			alias = keystore.aliases().nextElement();

			privateKey = (PrivateKey) keystore.getKey(alias, password.toCharArray());

			certificate = keystore.getCertificate(alias);

			X509Certificate x509 = (X509Certificate) certificate;

			subject = x509.getSubjectX500Principal().toString();
			issuer = x509.getIssuerX500Principal().toString();
			validFrom = x509.getNotBefore();
			validTo = x509.getNotAfter();
		}

	}

	private void checkExpirationDate() {

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

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public String getAlias() {
		return alias;
	}

	public String getSubject() {
		return subject;
	}

	public String getIssuer() {
		return issuer;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

}
