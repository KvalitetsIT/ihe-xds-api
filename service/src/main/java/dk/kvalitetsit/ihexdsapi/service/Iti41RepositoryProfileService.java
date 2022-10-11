package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dao.entity.Iti41RepositoryProfile;

import java.util.List;

public interface Iti41RepositoryProfileService {
    void init();
    boolean isUpdateAllowedForRepositoryUniqueId(String repositoryUniqueId);
    boolean isDeprecateAllowedForRepositoryUniqueId(String repositoryUniqueId);
    Iti41RepositoryProfile getRepositoryFromUniqueId(String repositoryId);
    Iti41RepositoryProfile getRepositoryFromEndpoint(String endpoint);
    List<Iti41RepositoryProfile> getRepositoryProfileList();


}
