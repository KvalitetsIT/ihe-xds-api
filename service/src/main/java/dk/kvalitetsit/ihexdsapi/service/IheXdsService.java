package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceOutput;

public interface IheXdsService {
    HelloServiceOutput helloServiceBusinessLogic(HelloServiceInput input);
}
