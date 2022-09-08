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

        System.out.println(credentialInfo.getCredentialVault());

        DgwsClientInfo dgwsClientInfo = stsService.getDgwsClientInfoForSystem(credentialInfo);
        dgwsClientInfo.setPatientId(patientId);
        dgwsClientInfo.setActingUserId(context.getActingUserId());
        dgwsClientInfo.setResponsibleUserId(context.getResponsibleUserId());
        dgwsClientInfo.setAuthorizationCode(context.getAuthorizationCode());
        dgwsClientInfo.setConsentOverride(context.getConsentOverride());
        dgwsClientInfo.setOrganisationCode(context.getOrganisationCode());
        return dgwsClientInfo;
    }
}
