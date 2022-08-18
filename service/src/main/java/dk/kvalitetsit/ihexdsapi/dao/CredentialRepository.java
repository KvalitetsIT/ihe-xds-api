package dk.kvalitetsit.ihexdsapi.dao;

import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;

import java.util.List;

public interface CredentialRepository {

    boolean saveCredentialsForID(CredentialInfoEntity credentialInfo);

    CredentialInfoEntity findCredentialInfoByID(String id);

    List<String> FindListOfIDsForOwner(String owner);


}
