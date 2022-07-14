package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

public interface StsService {

	DgwsClientInfo getDgwsClientInfoForSystem(CredentialInfo credentialInfo) throws DgwsSecurityException;

}
