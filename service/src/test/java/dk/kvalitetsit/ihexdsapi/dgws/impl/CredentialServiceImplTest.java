package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CredentialServiceImplTest extends AbstractTest {

    private CredentialServiceImpl credentialService;
    private CredentialRepository credentialRepository;

    @Before
    public void setup() {
        credentialRepository = Mockito.mock(CredentialRepository.class);
        credentialService = new CredentialServiceImpl(credentialRepository);
    }

    @Test
    public void testCreateCredentialVaultWithLegalCertificatePair() throws DgwsSecurityException {

        // Given
        String publicCertStr = getFileString("/certificates/public-cert1.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String owner = "me";
        String cvr = "46837428";
        String organisationName = "Statens Serum Institut";
        String id = "id";


        // When
        CredentialInfo info = credentialService.createAndAddCredentialInfo(owner, id, cvr, organisationName, publicCertStr, privateKeyStr);
        // Then
        assertNotNull(info);

    }

    @Test(expected = DgwsSecurityException.class)
    public void testCreateCredentialVaultWithNonsenseInput() throws DgwsSecurityException {

        // Given
        String publicCertStr = "Not a cert";
        String privateKeyStr = "Not a key";
        String owner = "me";
        String cvr = "46837428";
        String organisationName = "Statens Serum Institut";

        // When
        credentialService.createAndAddCredentialInfo(owner, "id", cvr, organisationName,
                publicCertStr, privateKeyStr);
    }

    @Test
    public void testCreateCredentialVaultWithNonMatchingCertAndKey() throws DgwsSecurityException {

        // Given
        String publicCertStr = getFileString("/certificates/other-cert.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String owner = "me";
        String cvr = "46837428";
        String organisationName = "Statens Serum Institut";

        // When
        CredentialInfo info = credentialService.createAndAddCredentialInfo(owner, "id", cvr, organisationName, publicCertStr, privateKeyStr);

        // Then
        assertNotNull(info);
    }

    @Test
    public void TestgetCredentialInfoFromId() {

        String publicCertStr = getFileString("/certificates/public-cert1.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String owner = "me";
        String cvr = "46837428";
        String organisationName = "Statens Serum Institut";
        String id = "MyID";

        Mockito.when(credentialRepository.findCredentialInfoByID(id)).then(a -> {
            CredentialInfoEntity output = new CredentialInfoEntity(owner, id, cvr, organisationName,
                    publicCertStr, privateKeyStr);
            return output;

        });

        // Test muck data mod metode

        CredentialInfo result = credentialService.getCredentialInfoFromId(id);
        Assert.assertNotNull(result);
        assertEquals(cvr, result.getCvr());

    }

}
