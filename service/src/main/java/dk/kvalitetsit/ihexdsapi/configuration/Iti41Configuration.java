package dk.kvalitetsit.ihexdsapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "xds.iti41.repos")
public class Iti41Configuration {

    private List<Repo> repo = new ArrayList<Repo>();

    public static class Repo {

        private String name;
        private String endpoint;
        private boolean dgwsenabled;
        private String repositoryuniqueid;
        private boolean updateallowed;

        private List<String> validdoctypes = new ArrayList<String>();

        public Repo() {

        }
        public Repo(String name, String endpoint, boolean dgws, String repositoryuniqueid, boolean updateallowed) {
            this.name = name;
            this.endpoint = endpoint;
            this.dgwsenabled = dgws;
            this.repositoryuniqueid = repositoryuniqueid;
            this.updateallowed = updateallowed;
        }


        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getEndpoint() {
            return endpoint;
        }
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        public boolean isDgwsenabled() {
            return dgwsenabled;
        }
        public void setDgwsenabled(boolean dgwsenabled) {
            this.dgwsenabled = dgwsenabled;
        }
        public String getRepositoryuniqueid() {
            return repositoryuniqueid;
        }
        public void setRepositoryuniqueid(String repositoryuniqueid) {
            this.repositoryuniqueid = repositoryuniqueid;
        }
        public boolean isUpdateallowed() {
            return updateallowed;
        }
        public void setUpdateallowed(boolean updateallowed) {
            this.updateallowed = updateallowed;
        }
        public List<String> getValiddoctypes() {
            return validdoctypes;
        }
        public void setValiddoctypes(List<String> validdoctypes) {
            this.validdoctypes = validdoctypes;
        }

        public void addValidDoctype(String validDoctype) {
            validdoctypes.add(validDoctype);
        }

    }

    public List<Repo> getRepo() {
        return repo;
    }

    public void setRepo(List<Repo> repo) {
        this.repo = repo;
    }

    public void addRepo(Repo r) {
        repo.add(r);
    }

    public String getNamesForSelection() {
        StringBuilder names = new StringBuilder();
        for (Repo r: repo) {
            if (names.length() > 0) {
                names.append(";");
            }
            names.append(r.getName());
        }
        return names.toString();
    }

    public String getEndpointsForSelection() {
        StringBuilder endpoints = new StringBuilder();
        for (Repo r: repo) {
            if (endpoints.length() > 0) {
                endpoints.append(";");
            }
            endpoints.append(r.getEndpoint());
        }
        return endpoints.toString();
    }
}
