package dk.kvalitetsit.ihexdsapi.dgws.impl;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.junit.Before;
import org.mockito.Mockito;

public class StsServiceImplTest extends AbstractTest {

    public static final String ID = "id";
    String publicCertStr = getFileString("/certificates/public-cert13.cer");
    String privateKeyStr = getFileString("/certificates/private-cert1.pem");
    String displayName = "My certificate";
    String organisationName = "Statens Serum Institut";

    private CredentialRepository credentialRepository;

    CredentialService credentialService;
    StsServiceImpl stsServiceImpl;

    @Before
    public void setup() throws DgwsSecurityException {


        credentialRepository = Mockito.mock(CredentialRepositoryImpl.class);
        credentialService = new CredentialServiceImpl(credentialRepository);
        CredentialInfo credentialInfo = credentialService.createAndAddCredentialInfo(null, displayName, publicCertStr, privateKeyStr);

        stsServiceImpl = new StsServiceImpl("http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService");
    }

   /* @Test
    public void testCreateSystemIdCard() throws DgwsSecurityException {
        // Given
        Mockito.when(credentialRepository.findCredentialInfoByID(ID)).then(a -> {
            CredentialInfoEntity output = new CredentialInfoEntity(null, displayName,
                    publicCertStr, privateKeyStr);
            return output;

        });

        CredentialInfo credentialInfo = credentialService.getCredentialInfoFromId(ID);

        // When
        DgwsClientInfo dgwsClientInfo = stsServiceImpl.getDgwsClientInfoForSystem(credentialInfo);

        // Then
        Assert.assertNotNull(dgwsClientInfo);
        Assert.assertNotNull(dgwsClientInfo.getSosi());
    }*/
}
