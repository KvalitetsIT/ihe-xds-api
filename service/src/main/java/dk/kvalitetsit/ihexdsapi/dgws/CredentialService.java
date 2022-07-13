package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

public interface CredentialService {

	public CredentialVault createCredentialVault(String password, String publicCertStr, String privateKeyStr) throws DgwsSecurityException;

}
