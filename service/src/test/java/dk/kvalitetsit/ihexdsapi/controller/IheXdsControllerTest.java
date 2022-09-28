package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.interceptors.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.interceptors.impl.CacheRequestResponseHandleImpl;
import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.impl.DgwsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {dk.kvalitetsit.ihexdsapi.configuration.RedisConfiguration.class})
public class IheXdsControllerTest {


    DgwsService dgwsService;
    Iti18Service iti18Service;

    CacheRequestResponseHandle cacheRequestResponseHandle;

    IheXdsController subject;

    // Needed?
    private static final int REDIS_PORT = 6379;

    private static GenericContainer<?> redis;

    static  {
        redis = new GenericContainer("redis:7.0.4")
                .withExposedPorts(REDIS_PORT)
                .withCommand("redis-server /usr/local/etc/redis/redis.conf")
                .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf", BindMode.READ_ONLY);

        redis.start();

        Integer mappedRedisPort = redis.getMappedPort(REDIS_PORT);
        ;
        System.setProperty("redis.host", DockerClientFactory.instance().dockerHostIpAddress());
        System.setProperty("redis.port", mappedRedisPort.toString());

        System.setProperty("redis.data.ttl", "3000");
    }


    @Autowired
    public RedisTemplate<String, Object> redisTemplate;


    @Before
    public void setup() {
        // Configure mocks senere
        this.dgwsService = Mockito.mock(DgwsServiceImpl.class);
        this.iti18Service = Mockito.mock(Iti18Service.class);
        this.cacheRequestResponseHandle = Mockito.mock(CacheRequestResponseHandleImpl.class);



        subject = new IheXdsController(dgwsService, iti18Service, cacheRequestResponseHandle);
    }

   @Test
    public void testv1Iti18HealthcareProfessionalGet() {
        // Given
        Iti18Request request = new Iti18Request();
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();
        context.setAuthorizationCode("CBNH1");
        context.setConsentOverride(false);
        request.setContext(context);
        Iti18QueryParameter queryParameters = new Iti18QueryParameter();
        queryParameters.setPatientId("2512489996");
        request.setQueryParameters(queryParameters);
        request.setCredentialId("DEFAULT");

        // When
        ResponseEntity<List<Iti18Response>> responseEntity = subject.v1Iti18Post(request);

        // Then
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testv1PrevRequestGet() {
        // When
        ResponseEntity<DownloadLog> responseEntity = subject.v1PrevRequestGet();


        // Then
       Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }


    @Test
    public void v1PrevResponseGet() {
         // When
        ResponseEntity<DownloadLog> responseEntity = subject.v1PrevResponseGet();


        // Then
       Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }
}
