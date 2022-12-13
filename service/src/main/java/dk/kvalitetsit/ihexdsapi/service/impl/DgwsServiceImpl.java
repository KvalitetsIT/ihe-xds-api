package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dgws.*;
import org.openapitools.model.HealthcareProfessionalContext;

public class DgwsServiceImpl implements DgwsService {

    private StsService stsService;

    private CredentialService credentialService;

    public DgwsServiceImpl(StsService stsService, CredentialService credentialService) {
        this.stsService = stsService;
        this.credentialService = credentialService;
    }
    @Override
    public DgwsClientInfo getHealthCareProfessionalClientInfo(String patientId, String credentialId, HealthcareProfessionalContext context) throws DgwsSecurityException {

        CredentialInfo credentialInfo = credentialService.getCredentialInfoFromId(credentialId);

        DgwsClientInfo dgwsClientInfo = stsService.getDgwsClientInfoForSystem(credentialInfo, patientId, context);


        return dgwsClientInfo;
    }

    @Override
    public DgwsClientInfo getSystemClientInfo(String credentialId) throws DgwsSecurityException {
        CredentialInfo credentialInfo = credentialService.getCredentialInfoFromId(credentialId);

        if (credentialInfo == null) {
            throw new DgwsSecurityException(1000, "Credentials do not exist");
        }

        DgwsClientInfo dgwsClientInfo = stsService.getDgwsClientInfoForSystem(credentialInfo);

        return  dgwsClientInfo;
    }
}
