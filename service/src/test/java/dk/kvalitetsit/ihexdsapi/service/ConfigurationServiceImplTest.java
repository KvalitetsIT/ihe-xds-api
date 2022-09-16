package dk.kvalitetsit.ihexdsapi.service;


import dk.kvalitetsit.ihexdsapi.service.impl.ConfigsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.model.ConfigResponse;


import java.util.List;
public class ConfigurationServiceImplTest {

    private ConfigsService subject;

    @Before
    public void setup() {

        String sts = "http://http://test1.ekstern-test.nspop.dk:8080test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService";
        String xds = "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsregistry";

        subject = new ConfigsServiceImpl(sts, xds) ;

    }


    @Test
    public void TestGetConfigResponsesList() {
       List<ConfigResponse> responses = subject.getListOfConfigResponses();

        Assert.assertEquals(2, responses.size());
        Assert.assertEquals("iti-18.url", responses.get(0).getConfigKey());


        Assert.assertEquals("http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsregistry", responses.get(0).getConfigValue());



    }

}
