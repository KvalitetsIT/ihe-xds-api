package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;

public class CredentialServiceImplTest extends AbstractTest {


	CredentialServiceImpl subject = new CredentialServiceImpl();

	@Test
	public void testCreateCredentialVaultWithLegalCertificatePair() throws DgwsSecurityException  {

		// Given
		String publicCertStr = getFileString("/certificates/public-cert1.cer");
		String privateKeyStr = getFileString("/certificates/private-cert1.pem");
		String password = "password";
		
		// When
		CredentialVault vault = subject.createCredentialVault(password, publicCertStr, privateKeyStr);
		
		// Then
		Assert.assertNotNull(vault);
	}

	@Test(expected = DgwsSecurityException.class)
	public void testCreateCredentialVaultWithNonsenseInput() throws DgwsSecurityException  {

		// Given
		String publicCertStr = "Not a cert";
		String privateKeyStr = "Not a key";
		String password = "password";
		
		// When
		subject.createCredentialVault(password, publicCertStr, privateKeyStr);
	}

	@Test
	public void testCreateCredentialVaultWithNonMatchingCertAndKey() throws DgwsSecurityException  {

		// Given
		String publicCertStr = getFileString("/certificates/other-cert.cer");
		String privateKeyStr = getFileString("/certificates/private-cert1.pem");
		String password = "password";
		
		// When
		CredentialVault vault = subject.createCredentialVault(password, publicCertStr, privateKeyStr);
		
		// Then
		Assert.assertNotNull(vault);
	}
}
