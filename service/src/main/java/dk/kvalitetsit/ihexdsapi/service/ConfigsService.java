package dk.kvalitetsit.ihexdsapi.service;

import org.openapitools.model.ConfigResponse;

import java.util.List;

public interface ConfigsService {

    List<ConfigResponse> getListOfConfigResponses();
}
