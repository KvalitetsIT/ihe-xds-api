package dk.kvalitetsit.ihexdsapi.service;

import org.openapitools.model.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;

import java.util.List;

public interface RegistryErrorService {

    void createListOfErrors(QueryResponse queryResponse);
    List<RegistryError> getList();

    void cleanup();
}
