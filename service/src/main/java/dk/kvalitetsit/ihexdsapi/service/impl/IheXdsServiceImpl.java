package dk.kvalitetsit.ihexdsapi.service.impl;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceOutput;
import org.openapitools.model.Code;

public class IheXdsServiceImpl implements IheXdsService {



    public IheXdsServiceImpl() {}
    @Override
    public HelloServiceOutput helloServiceBusinessLogic(HelloServiceInput input) {
        var output = new HelloServiceOutput();
        output.setNow(ZonedDateTime.now());
        output.setName(input.getName());

        return output;
    }




}
