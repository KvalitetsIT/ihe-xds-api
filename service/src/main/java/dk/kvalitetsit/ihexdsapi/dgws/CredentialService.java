package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

import java.util.Collection;

public interface CredentialService {

	public CredentialInfo createAndAddCredentialInfo(String id, String cvr, String organisation, String publicCertStr, String privateKeyStr) throws DgwsSecurityException;

	public Collection<String> getIds();

	public CredentialInfo getCredentialInfoFromId(String id);
}
