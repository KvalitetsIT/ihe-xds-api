package dk.kvalitetsit.hello.controller;

import dk.kvalitetsit.ihexdsapi.controller.IheXdsController;
import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceOutput;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.openapitools.model.HelloRequest;

import java.time.ZonedDateTime;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

public class HelloControllerTest {
    private IheXdsController helloController;
    private IheXdsService helloService;

    @Before
    public void setup() {
        helloService = Mockito.mock(IheXdsService.class);

        helloController = new IheXdsController(helloService);
    }

    @Test
    public void testCallController() {
        var input = new HelloRequest();
        input.setName(UUID.randomUUID().toString());

        var expectedDate = ZonedDateTime.now();
        Mockito.when(helloService.helloServiceBusinessLogic(Mockito.any())).then(a -> {
            HelloServiceOutput output = new HelloServiceOutput();
            output.setNow(expectedDate);
            output.setName(a.getArgument(0, HelloServiceInput.class).getName());
            return output;
        });

        var result = helloController.v1HelloPost(input);

        assertNotNull(result);
        assertEquals(input.getName(), result.getBody().getName());
        assertEquals(expectedDate.toOffsetDateTime(), result.getBody().getNow());

        var inputArgumentCaptor = ArgumentCaptor.forClass(HelloServiceInput.class);
        Mockito.verify(helloService, times(1)).helloServiceBusinessLogic(inputArgumentCaptor.capture());

        assertNotNull(inputArgumentCaptor.getValue());
        assertEquals(input.getName(), inputArgumentCaptor.getValue().getName());
    }
}
