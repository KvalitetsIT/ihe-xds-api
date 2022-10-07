package dk.kvalitetsit.ihexdsapi.dgws.impl;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openapitools.model.HealthcareProfessionalContext;

public class StsServiceImplTest extends AbstractTest {

    public static final String ID = "id";
    String publicCertStr = getFileString("/certificates/public-cert1.cer");
    String privateKeyStr = getFileString("/certificates/private-cert1.pem");
    String displayName = "My certificate";
    String organisationName = "Statens Serum Institut";

    String patientID = "2512489996";
    private CredentialRepository credentialRepository;

    CredentialService credentialService;
    StsServiceImpl stsServiceImpl;
    HealthcareProfessionalContext context;


    @Before
    public void setup() throws DgwsSecurityException {

         context = new HealthcareProfessionalContext();
        context.setAuthorizationCode("CBNH1");
        context.setConsentOverride(false);



        credentialRepository = Mockito.mock(CredentialRepositoryImpl.class);
        credentialService = new CredentialServiceImpl(credentialRepository);
        CredentialInfo credentialInfo = credentialService.createAndAddCredentialInfo(null, displayName, publicCertStr, privateKeyStr);

        stsServiceImpl = new StsServiceImpl("http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService");
    }
/*
    @Test
    public void testgetDgwsClientInfoForSystem() throws DgwsSecurityException {
        // Given
        Mockito.when(credentialRepository.findCredentialInfoByID(ID)).then(a -> {
            CredentialInfoEntity output = new CredentialInfoEntity(null,"test", displayName,
                    publicCertStr, privateKeyStr);
            return output;

        });

        CredentialInfo credentialInfo = credentialService.getCredentialInfoFromId(ID);

        // When
        DgwsClientInfo dgwsClientInfo = stsServiceImpl.getDgwsClientInfoForSystem(credentialInfo, patientID , context);

        // Then
        Assert.assertNotNull(dgwsClientInfo);
        Assert.assertNotNull(dgwsClientInfo.getSosi());
    }*/
}
