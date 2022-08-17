package dk.kvalitetsit.ihexdsapi.dao;

import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;

import java.util.List;

public interface CredentialRepository {

      boolean saveCredentialsForID(CredentialInfoEntity credentialInfo, int TTL);

      boolean saveListOfCertsForUser(String owner, List<String> list, int TTL);

    CredentialInfoEntity findByID(String id);

    List<String> findByOwner(String owner);

    boolean updateCerts(String owner, List<String> list, int TTL);


}
