package dk.kvalitetsit.ihexdsapi.dgws.impl;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StsServiceImplTest extends AbstractTest {

    StsServiceImpl stsServiceImpl;

    @Before
    public void setup() {

        stsServiceImpl = new StsServiceImpl("http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService");
    }

    @Test
    public void testCreateSystemIdCard() throws DgwsSecurityException {
        // Given
        String publicCertStr = getFileString("/certificates/public-cert1.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        CredentialServiceImpl credentialService = new CredentialServiceImpl();
        CredentialVault credentialVault = credentialService.createCredentialVault("Test1234", publicCertStr, privateKeyStr);

        // When
        DgwsClientInfo dgwsClientInfo = stsServiceImpl.getDgwsClientInfoForSystem(credentialVault, "46837428", "Statens Serum Institut");

        // Then
        Assert.assertNotNull(dgwsClientInfo);
        Assert.assertNotNull(dgwsClientInfo.getSosi());
    }
}
