package dk.kvalitetsit.ihexdsapi.utility;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import dk.sosi.seal.vault.GenericCredentialVault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

public class ValutGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValutGenerator.class);
    private static final String PASSWORD = "Test1234";


    private static PrivateKey getPrivateKeyFromString(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
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
    private static KeyStore createKeystore(String alias, String password, String publicCertStr, String privateKeyStr) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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


    public static GenericCredentialVault generateGenericCredentialVault(String publicCertStr, String privateKeyStr) throws DgwsSecurityException {
        Properties properties = new Properties();
        KeyStore keystore;
        try {
            keystore = createKeystore(CredentialVault.ALIAS_SYSTEM, PASSWORD, publicCertStr, privateKeyStr);
        } catch (KeyStoreException | NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            LOGGER.error("Error creating keystore", e);
            throw new DgwsSecurityException(e, 2, "Invalid private key");
        }
        catch (CertificateException e)  {
            LOGGER.error("Error creating keystore", e);
            throw new DgwsSecurityException(e, 1, "Invalid certificate");
        }

        GenericCredentialVault generic = new GenericCredentialVault(properties, keystore, PASSWORD);
        return generic;
    }
}
