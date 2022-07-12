package dk.kvalitetsit.ihexdsapi.service;

import java.time.ZonedDateTime;

import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceOutput;

public class IheXdsServiceImpl implements IheXdsService {
    @Override
    public HelloServiceOutput helloServiceBusinessLogic(HelloServiceInput input) {
        var output = new HelloServiceOutput();
        output.setNow(ZonedDateTime.now());
        output.setName(input.getName());

        return output;
    }
}
