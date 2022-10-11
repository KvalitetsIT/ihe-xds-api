package dk.kvalitetsit.ihexdsapi.dao.entity;
import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;

public class Iti41RepositoryProfile {
    private String name;
    private String endpoint;
    private boolean dgws;
    private String repositoryuniqueid;
    private boolean updateallowed;
    private Iti41PortType iti41PortType;
    private List<String> validDocTypes;

    public Iti41RepositoryProfile() {
        validDocTypes = new ArrayList<>();
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
    public boolean isDgws() {
        return dgws;
    }
    public void setDgws(boolean dgws) {
        this.dgws = dgws;
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
    public Iti41PortType getIti41PortType() {
        return iti41PortType;
    }
    public void setIti41PortType(Iti41PortType iti41PortType) {
        this.iti41PortType = iti41PortType;
    }
    public List<String> getValidDocTypes() {
        return validDocTypes;
    }
    public void setValidDocTypes(List<String> validDocTypes) {
        this.validDocTypes = validDocTypes;
    }
    public void addDocType(String docType) {
        this.validDocTypes.add(docType);
    }
    public boolean isDocTypeValid(String docTypeToCheck) {
        if (validDocTypes.size() == 0) {
            return true;
        }
        for (String docType: this.validDocTypes) {
            if (docType.equals(docTypeToCheck)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "RepositoryProfileIti41Dto [name=" + name + ", endpoint=" + endpoint + ", dgws=" + dgws + ", iti41PortType=" + iti41PortType + "]";
    }
}
