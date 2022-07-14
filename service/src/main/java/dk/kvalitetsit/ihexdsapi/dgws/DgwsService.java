package dk.kvalitetsit.ihexdsapi.dgws;

import org.openapitools.model.HealthcareProfessionalContext;

public interface DgwsService {
    DgwsClientInfo getHealthCareProfessionalClientInfo(String patientId, String credentialId, HealthcareProfessionalContext context) throws DgwsSecurityException;
}
