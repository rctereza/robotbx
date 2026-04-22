package com.rctereza.robotbx.ztest;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

public class ReadPFX {
    public static void main(String[] args) throws Exception {
        String pfxPath = "C:\\Temp\\Certificados\\JACIRA DE OLIVEIRA_40977839000107_123456.pfx";
        String password = "123456";

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(pfxPath)) {
            keystore.load(fis, password.toCharArray());
        }

        // Usually there's only one alias in a PFX
        String alias = keystore.aliases().nextElement();

        // Get private key
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, password.toCharArray());

        // Get certificate
        Certificate cert = keystore.getCertificate(alias);

        System.out.println("Alias: " + alias);
        System.out.println("Private Key: " + privateKey);
        System.out.println("Certificate: " + cert);
        
        X509Certificate x509 = (X509Certificate) cert;
        
        X500Principal subject = x509.getSubjectX500Principal();
        X500Principal issuer = x509.getIssuerX500Principal();
        
        System.out.println("Subject: " + subject);
        System.out.println("Issuer: " + issuer);
        System.out.println("Valid From: " + x509.getNotBefore());
        System.out.println("Valid To: " + x509.getNotAfter());
        
//        System.out.println("RFC2253--->" + subject.getName(X500Principal.RFC2253));
//        System.out.println("RFC1779--->" + subject.getName(X500Principal.RFC1779));
//        System.out.println("CANONICAL->" + subject.getName(X500Principal.CANONICAL));
        
        Enumeration<String> aliases = keystore.aliases();
        while (aliases.hasMoreElements()) {
            String a = aliases.nextElement();
            System.out.println("Found alias: " + a);
        }
    }
}