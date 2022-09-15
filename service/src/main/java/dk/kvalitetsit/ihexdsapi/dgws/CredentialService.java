package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

import java.util.Collection;
import java.util.List;

public interface CredentialService {

	public CredentialInfo createAndAddCredentialInfo(String owner, String displayName , String publicCertStr, String privateKeyStr) throws DgwsSecurityException;

	public List<String[]> getIds(String owner);

	public CredentialInfo getCredentialInfoFromId(String id);


}
