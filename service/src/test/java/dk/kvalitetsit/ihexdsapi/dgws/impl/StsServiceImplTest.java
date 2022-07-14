package dk.kvalitetsit.ihexdsapi.dgws.impl;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.CredentialVault;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StsServiceImplTest extends AbstractTest {

    public static final String ID = "id";
    CredentialService credentialService;
    StsServiceImpl stsServiceImpl;

    @Before
    public void setup() throws DgwsSecurityException {

        String publicCertStr = getFileString("/certificates/public-cert1.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String cvr = "46837428";
        String organisationName = "Statens Serum Institut";
        credentialService = new CredentialServiceImpl();
        CredentialInfo credentialInfo = credentialService.createAndAddCredentialInfo(null, ID, cvr, organisationName, publicCertStr, privateKeyStr);

        stsServiceImpl = new StsServiceImpl("http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService");
    }

    @Test
    public void testCreateSystemIdCard() throws DgwsSecurityException {
        // Given
        CredentialInfo credentialInfo = credentialService.getCredentialInfoFromId(ID);

        // When
        DgwsClientInfo dgwsClientInfo = stsServiceImpl.getDgwsClientInfoForSystem(credentialInfo);

        // Then
        Assert.assertNotNull(dgwsClientInfo);
        Assert.assertNotNull(dgwsClientInfo.getSosi());
    }
}
