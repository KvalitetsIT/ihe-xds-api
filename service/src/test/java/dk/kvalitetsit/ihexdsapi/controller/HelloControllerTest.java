package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore
public class HelloControllerTest {
    private IheXdsController iheXdsController;
    private IheXdsService iheXdsService;

    private Iti18Service iti18Service;

    @Before
    public void setup() {
        iheXdsService = Mockito.mock(IheXdsService.class);

        iti18Service = Mockito.mock(Iti18Service.class);

       // iheXdsController = new IheXdsController(iti18Service);
    }

    @Test
    public void testCallController() {
      /*  var input = new Iti18Request();
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
        //assertEquals(input.getName(), inputArgumentCaptor.getValue().getName());*/
        //assertEquals(input.getName(), inputArgumentCaptor.getValue().getName());*/
    }
}
