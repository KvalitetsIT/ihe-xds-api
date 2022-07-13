package dk.kvalitetsit.hello.controller;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.time.ZonedDateTime;
import java.util.UUID;

import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.openapitools.model.Iti18Request;

import dk.kvalitetsit.ihexdsapi.controller.IheXdsController;
import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceOutput;

public class HelloControllerTest {
    private IheXdsController iheXdsController;
    private IheXdsService iheXdsService;

    private Iti18Service iti18Service;

    @Before
    public void setup() {
        iheXdsService = Mockito.mock(IheXdsService.class);

        iti18Service = Mockito.mock(Iti18Service.class);

        iheXdsController = new IheXdsController(iti18Service);
    }

    @Test
    public void testCallController() {
        var input = new Iti18Request();
        input.setPatientId(UUID.randomUUID().toString());

        var expectedDate = ZonedDateTime.now();
        Mockito.when(iheXdsService.helloServiceBusinessLogic(Mockito.any())).then(a -> {
            HelloServiceOutput output = new HelloServiceOutput();
            output.setNow(expectedDate);
            output.setName(a.getArgument(0, HelloServiceInput.class).getName());
            return output;
        });

        var result = iheXdsController.v1Iti18Get(input);

        assertNotNull(result);
       // assertEquals(input.getName(), result.getBody().getName());
        //assertEquals(expectedDate.toOffsetDateTime(), result.getBody().getNow());

        var inputArgumentCaptor = ArgumentCaptor.forClass(HelloServiceInput.class);
        Mockito.verify(iheXdsService, times(1)).helloServiceBusinessLogic(inputArgumentCaptor.capture());

        assertNotNull(inputArgumentCaptor.getValue());
        //assertEquals(input.getName(), inputArgumentCaptor.getValue().getName());
    }
}
