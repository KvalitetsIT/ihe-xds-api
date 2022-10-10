package dk.kvalitetsit.ihexdsapi.dgws.impl;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
        String displayName = "My cetifiacte";


        // When
        CredentialInfo info = credentialService.createAndAddCredentialInfo(owner, displayName, publicCertStr, privateKeyStr);
        // Then
        assertNotNull(info);

    }

    @Test(expected = DgwsSecurityException.class)
    public void testCreateCredentialVaultWithNonsenseInput() throws DgwsSecurityException {

        // Given
        String publicCertStr = "Not a cert";
        String privateKeyStr = "Not a key";
        String owner = "me";
        String displayName = "My cetifiacte";



        // When
        credentialService.createAndAddCredentialInfo(owner,  displayName,
                publicCertStr, privateKeyStr);
    }

    @Test
    public void testCreateCredentialVaultWithNonMatchingCertAndKey() throws DgwsSecurityException {

        // Given
        String publicCertStr = getFileString("/certificates/other-cert.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String owner = "me";
        String displayName = "My cetifiacte";

        // When
        CredentialInfo info = credentialService.createAndAddCredentialInfo(owner, displayName, publicCertStr, privateKeyStr);

        // Then
        assertNotNull(info);
    }

    @Test
    public void TestgetCredentialInfoFromId() {

        String publicCertStr = getFileString("/certificates/public-cert1.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String owner = "me";
        String displayName = "My cetifiacte";
        String id = "MyID";
        String serialNumber = "test";
        String type = "test";

        Mockito.when(credentialRepository.findCredentialInfoByID(id)).then(a -> {
            CredentialInfoEntity output = new CredentialInfoEntity(owner, id, displayName,
                    publicCertStr, privateKeyStr, serialNumber, type);
            return output;

        });

        // Test muck data mod metode

        CredentialInfo result = credentialService.getCredentialInfoFromId(id);
        Assert.assertNotNull(result);

    }

}
