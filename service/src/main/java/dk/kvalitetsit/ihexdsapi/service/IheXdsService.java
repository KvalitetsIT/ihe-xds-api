package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceOutput;
import org.openapitools.model.Code;

import java.util.List;

public interface IheXdsService {
    HelloServiceOutput helloServiceBusinessLogic(HelloServiceInput input);

}
