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
import java.util.*;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import dk.sosi.seal.vault.GenericCredentialVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;



public class CredentialServiceImpl implements CredentialService {

	private static final String PASSWORD = "Test1234";

	private static final String DEFAULT_OWNER = "  ";
	private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

	private Map<String, List<String>> registeredOwners = new HashMap<>();
	private Map<String, CredentialInfo> registeredInfos = new HashMap<>();

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
	public synchronized CredentialInfo createAndAddCredentialInfo(String owner, String id, String cvr, String organisation, String publicCertStr, String privateKeyStr) throws DgwsSecurityException {

		if (registeredInfos.containsKey(id)) {
			throw new DgwsSecurityException("A credential vault with id "+id+" is already registered.");
		}

		String ownerKey = DEFAULT_OWNER;
		if (owner != null && owner.trim().length() >= 0) {
			ownerKey = owner.trim();
		}
		List<String> owning = null;
		if (!registeredOwners.containsKey(ownerKey)) {
			owning = new LinkedList<>();
			registeredOwners.put(ownerKey, owning);
		} else {
			owning = registeredOwners.get(ownerKey);
		}

		Properties properties = new Properties();
		KeyStore keystore;
		try {
			keystore = createKeystore(CredentialVault.ALIAS_SYSTEM, PASSWORD, publicCertStr, privateKeyStr);
		} catch (KeyStoreException  | NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			LOGGER.error("Error creating keystore", e);
			throw new DgwsSecurityException(e, 2, "Invalid private key");
		}
		catch (CertificateException e)  {
			LOGGER.error("Error creating keystore", e);
			throw new DgwsSecurityException(e, 1, "Invalid certificate");
		}
		
		GenericCredentialVault generic = new GenericCredentialVault(properties, keystore, PASSWORD);
		CredentialInfo credentialInfo = new CredentialInfo(generic, cvr, organisation);

		registeredInfos.put(id, credentialInfo);
		owning.add(id);

		return credentialInfo;
	}

	@Override
	public Collection<String> getIds(String owner) {

		List<String> result = new LinkedList<>();

		String ownerKey = null;
		if (owner != null && owner.trim().length() >= 0) {
			ownerKey = owner.trim();
		}
		if (ownerKey != null && registeredOwners.containsKey(ownerKey)) {
			result.addAll(registeredOwners.get(ownerKey));
		}

		if (registeredOwners.containsKey(DEFAULT_OWNER)) {
			result.addAll(registeredOwners.get(DEFAULT_OWNER));
		}
		return result;
	}

	@Override
	public CredentialInfo getCredentialInfoFromId(String id) {
		return registeredInfos.get(id);
	}

}
