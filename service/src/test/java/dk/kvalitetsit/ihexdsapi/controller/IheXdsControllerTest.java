package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dao.AbstractRedisTest;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dao.impl.CacheRequestResponseHandleImpl;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.UtilityService;
import dk.kvalitetsit.ihexdsapi.service.impl.DgwsServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.UtilityServiceImpl;
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

import java.util.Arrays;
import java.util.List;


public class IheXdsControllerTest {


    DgwsService dgwsService;
    Iti18Service iti18Service;

    CacheRequestResponseHandle cacheRequestResponseHandle;

    UtilityService utilityService;

    IheXdsController subject;



    @Before
    public void setup() {
        // Configure mocks senere
        this.dgwsService = Mockito.mock(DgwsServiceImpl.class);
        this.iti18Service = Mockito.mock(Iti18Service.class);
        this.cacheRequestResponseHandle = Mockito.mock(CacheRequestResponseHandleImpl.class);
        this.utilityService = Mockito.mock((UtilityServiceImpl.class));



        subject = new IheXdsController(dgwsService, iti18Service, cacheRequestResponseHandle, utilityService);
    }

   @Test
    public void testv1Iti18HealthcareProfessionalGet() throws DgwsSecurityException {
        // Given
       Mockito.when(utilityService.getId("tempRes")).then(a -> {

           String output = "Response";
           return output;
       });Mockito.when(utilityService.getId("tempReq")).then(a -> {
           String output = "Request";
           return output;
       });

       Mockito.when(dgwsService.getHealthCareProfessionalClientInfo(Mockito.any(), Mockito.any(), Mockito.any())).then(a -> {

          DgwsClientInfo output = new DgwsClientInfo(null, null, null, null, null);
           return output;
       });

        Mockito.when(iti18Service.queryForDocument(Mockito.any(), Mockito.any())).then(a -> {

            Iti18Response output = new Iti18Response();

           return output;
       });


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
        ResponseEntity<Iti18Response> responseEntity = subject.v1Iti18Post(request);

        // Then
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }
/*
    @Test
    public void testv1PrevRequestGet() {
        // When
        ResponseEntity<DownloadLog> responseEntity = subject.v1RequestRequestIdGet();


        // Then
       Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }


    @Test
    public void v1PrevResponseGet() {
         // When
        ResponseEntity<DownloadLog> responseEntity = subject.v1ResponseResponseIdGet();


        // Then
       Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }*/
}
