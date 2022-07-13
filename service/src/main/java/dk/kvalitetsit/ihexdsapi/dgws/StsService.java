package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

public interface StsService {

	public DgwsClientInfo getDgwsClientInfoForSystem(CredentialVault credentialVault, String cvr, String organisation) throws DgwsSecurityException;
}
