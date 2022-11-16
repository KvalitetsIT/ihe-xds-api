package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;
import org.openapitools.model.HealthcareProfessionalContext;

public interface StsService {

	DgwsClientInfo getDgwsClientInfoForSystem(CredentialInfo credentialInfo, String patientId, HealthcareProfessionalContext context) throws DgwsSecurityException;

	DgwsClientInfo getDgwsClientInfoForSystem(CredentialInfo credentialInfo) throws DgwsSecurityException;

}
