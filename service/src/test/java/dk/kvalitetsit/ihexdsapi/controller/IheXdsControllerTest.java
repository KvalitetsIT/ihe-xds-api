package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dao.impl.CacheRequestResponseHandleImpl;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.IDContextService;
import dk.kvalitetsit.ihexdsapi.service.Iti41Service;
import dk.kvalitetsit.ihexdsapi.service.Iti43Service;
import dk.kvalitetsit.ihexdsapi.service.impl.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openapitools.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class IheXdsControllerTest {


    DgwsService dgwsService;
    Iti18Service iti18Service;

    Iti43Service iti43Service;

    Iti41Service iti41Service;

    CacheRequestResponseHandle cacheRequestResponseHandle;

    IDContextService iDContextService;



    IheXdsController subject;



    @Before
    public void setup() {
        // Configure mocks senere
        this.dgwsService = Mockito.mock(DgwsServiceImpl.class);
        this.iti18Service = Mockito.mock(Iti18ServiceImpl.class);
        this.iti43Service = Mockito.mock(Iti43ServiceImpl.class);
        this.cacheRequestResponseHandle = Mockito.mock(CacheRequestResponseHandleImpl.class);
        this.iDContextService = Mockito.mock((IDContextServiceImpl.class));
        this.iti41Service = Mockito.mock(Iti41ServiceImpl.class);




        subject = new IheXdsController(dgwsService, iti18Service, cacheRequestResponseHandle, iDContextService, iti43Service, iti41Service);
    }

   @Test
    public void testv1Iti18HealthcareProfessionalGet() throws DgwsSecurityException {
        // Given
       Mockito.when(iDContextService.getId("tempRes")).then(a -> {

           String output = "Response";
           return output;
       });Mockito.when(iDContextService.getId("tempReq")).then(a -> {
           String output = "Request";
           return output;
       });

       Mockito.when(dgwsService.getHealthCareProfessionalClientInfo(Mockito.any(), Mockito.any(), Mockito.any())).then(a -> {

          DgwsClientInfo output = new DgwsClientInfo(null, null, null, null, null, false);
           return output;
       });

        Mockito.when(iti18Service.queryForDocument((Iti18QueryParameter) Mockito.any(), Mockito.any())).then(a -> {

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

    @Test
    public void testv1PrevRequestGet() {
        // When
        String test = "test";
        ResponseEntity<DownloadLog> responseEntity = subject.v1RequestRequestIdGet(test);


        // Then
       Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }


    @Test
    public void testv1PrevResponseGet() {
         // When
        String test = "test";
        ResponseEntity<DownloadLog> responseEntity = subject.v1ResponseResponseIdGet(test);


        // Then
       Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testv1Iti43Post() {

        Iti43Request request = new Iti43Request();
        HealthcareProfessionalContext context = new HealthcareProfessionalContext();
        context.setAuthorizationCode("CBNH1");
        context.setConsentOverride(false);
        request.setContext(context);
        Iti43QueryParameter queryParameters = new Iti43QueryParameter();
        queryParameters.setPatientId("2512489996");
        request.setQueryParameters(queryParameters);
        request.setCredentialId("DEFAULT");

        // When
        ResponseEntity<Iti43Response> responseEntity = subject.v1Iti43Post(request);

        // Then
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}
