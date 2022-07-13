package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.ihexdsapi.service.impl.IheXdsServiceImpl;
import org.junit.Before;
import org.junit.Test;

import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.model.HelloServiceInput;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HelloServiceImplTest {
    private IheXdsService helloService;

    @Before
    public void setup() {
        helloService = new IheXdsServiceImpl();
    }

    @Test
    public void testValidInput() {
        var input = new HelloServiceInput();
        input.setName(UUID.randomUUID().toString());

        var result = helloService.helloServiceBusinessLogic(input);
        assertNotNull(result);
        assertNotNull(result.getNow());
        assertEquals(input.getName(), result.getName());
    }
}
