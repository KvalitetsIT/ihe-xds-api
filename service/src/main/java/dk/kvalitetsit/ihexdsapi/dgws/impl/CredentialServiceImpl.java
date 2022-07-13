package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import dk.sosi.seal.vault.GenericCredentialVault;

public class CredentialServiceImpl implements CredentialService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

	private KeyStore createKeystore(String alias, String password, String publicCertStr, String privateKeyStr) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Create the keystore
		KeyStore keyStore = KeyStore.getInstance("jks");
		keyStore.load(null);

		// Load the certificate chain (in X.509 DER encoding).
		CertificateFactory certificateFactory =	CertificateFactory.getInstance("X.509");
		java.security.cert.Certificate[] chain = {};
		InputStream certificateStream = new ByteArrayInputStream(publicCertStr.getBytes());
		chain = certificateFactory.generateCertificates(certificateStream).toArray(chain);
		certificateStream.close();

		// Load the private key (in PKCS#8 DER encoding).
		PrivateKey privateKey = getPrivateKeyFromString(privateKeyStr);
		keyStore.setEntry(alias, new KeyStore.PrivateKeyEntry(privateKey, chain), new KeyStore.PasswordProtection(password.toCharArray()));
		return keyStore;
	}		

	private PrivateKey getPrivateKeyFromString(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// Read in the key into a String
		StringBuilder pkcs8Lines = new StringBuilder();
		BufferedReader rdr = new BufferedReader(new StringReader(privateKey));
		String line;
		while ((line = rdr.readLine()) != null) {
			pkcs8Lines.append(line);
		}

		// Remove the "BEGIN" and "END" lines, as well as any whitespace
		String pkcs8Pem = pkcs8Lines.toString();
		pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
		pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
		pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");

		// Base64 decode the result
		byte [] pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);

		// extract the private key
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privKey = kf.generatePrivate(keySpec);
		return privKey;
	}

	@Override
	public CredentialVault createCredentialVault(String password, String publicCertStr, String privateKeyStr) throws DgwsSecurityException {
		
		Properties properties = new Properties();
		KeyStore keystore;
		try {
			keystore = createKeystore(CredentialVault.ALIAS_SYSTEM, password, publicCertStr, privateKeyStr);
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			LOGGER.error("Error creating keystore", e);
			throw new DgwsSecurityException(e);
		}
		
		GenericCredentialVault generic = new GenericCredentialVault(properties, keystore, password);
		return generic;
	}
}
