package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import org.mockito.Mockito;

public class CredentialServiceImplTest extends AbstractTest {

	private CredentialServiceImpl credentialService;
	private CredentialRepositoryImpl credentialRepository;

	@Before
	public void setup() {
		credentialRepository = Mockito.mock(CredentialRepositoryImpl.class);
		credentialService = new CredentialServiceImpl(credentialRepository);
	}

	@Test
	public void testCreateCredentialVaultWithLegalCertificatePair() throws DgwsSecurityException  {

		// Given
		String publicCertStr = getFileString("/certificates/public-cert1.cer");
		String privateKeyStr = getFileString("/certificates/private-cert1.pem");
		String owner = "me";
		String cvr = "46837428";
		String organisationName = "Statens Serum Institut";
		String id = "id";
		/*Mockito.when(credentialRepository.(Mockito.any())).then(a -> {

		})

*/
		// When
		CredentialInfo info = credentialService.createAndAddCredentialInfo(owner,id, cvr, organisationName, publicCertStr, privateKeyStr);
		System.out.println(info.getCredentialVault().getSystemCredentialPair().getCertificate());
		// Then
		Assert.assertNotNull(info);
	}

	@Test(expected = DgwsSecurityException.class)
	public void testCreateCredentialVaultWithNonsenseInput() throws DgwsSecurityException  {

		// Given
		String publicCertStr = "Not a cert";
		String privateKeyStr = "Not a key";
		String owner = "me";
		String cvr = "46837428";
		String organisationName = "Statens Serum Institut";

		// When
		credentialService.createAndAddCredentialInfo(owner,"id", cvr, organisationName, publicCertStr, privateKeyStr);
	}

	@Test
	public void testCreateCredentialVaultWithNonMatchingCertAndKey() throws DgwsSecurityException  {

		// Given
		String publicCertStr = getFileString("/certificates/other-cert.cer");
		String privateKeyStr = getFileString("/certificates/private-cert1.pem");
		String owner = "me";
		String cvr = "46837428";
		String organisationName = "Statens Serum Institut";

		// When
		CredentialInfo info = credentialService.createAndAddCredentialInfo(owner,"id", cvr, organisationName, publicCertStr, privateKeyStr);
		
		// Then
		Assert.assertNotNull(info);
	}

}
