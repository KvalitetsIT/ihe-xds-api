package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.Iti41RepositoriesService;
import org.openapitools.model.Iti41Repository;

import java.util.LinkedList;
import java.util.List;

public class Iti41RepositoriesServiceImpl implements Iti41RepositoriesService {

    private final String[] REPOSITORYNAMES =  {"DROS"};

    private List<Iti41Repository> repositories = new LinkedList<>();

    public Iti41RepositoriesServiceImpl(String dros) {
        Iti41Repository repoDros = createRepository(dros, 0);

        repositories.add(repoDros);

    }

    @Override
    public List<Iti41Repository> getRepositories() {
        return List.copyOf(this.repositories);
    }

    private Iti41Repository createRepository(String path, int id) {
        Iti41Repository r = new Iti41Repository();

        r.setDisplayName(this.REPOSITORYNAMES[id]);
        r.setPath(path);

        return r;
    }
}
