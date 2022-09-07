package dk.kvalitetsit.ihexdsapi.controller;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.service.CodesExecption;
import dk.kvalitetsit.ihexdsapi.service.CodesService;
import dk.kvalitetsit.ihexdsapi.service.impl.CodesServiceImpl;
import org.openapitools.api.*;
import org.openapitools.model.Code;
import org.openapitools.model.CredentialInfoResponse;
import org.openapitools.model.Iti18Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
@RestController
public class CodesController implements TypeCodeApi, AvailabilityCodesApi, ClassCodesApi, EventCodeApi, FormatCodesApi, HealthCareFacilityTypeCodesApi, ObjectTypeCodeApi, PracticeSettingCodeApi{
    private CodesService codesService;

    public CodesController(CodesService codesService) {
        this.codesService = codesService;
    }


    @Override
    public ResponseEntity<List<Code>> v1CodesAvailabilityStatusCodeGet() {
        try {
            Collection<Code> codes = codesService.generateListOfAvailabilityStatus();
            ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<Code>> v1CodesClassCodeGet() {
        return null;
    }

    @Override
    public ResponseEntity<List<Code>> v1CodesEventCodeGet() {
        try {
            Collection<Code> codes = codesService.generateListOfEventCodes();
            ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
            return responseEntity;
        } catch (CodesExecption e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<Code>> v1CodesFormatCodeGet() {
        try {
            Collection<Code> codes = codesService.generateListOfFormatCodes();
            ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
            return responseEntity;
        } catch (CodesExecption e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<Code>> v1CodesHealthCareFacilityTypeGet() {
        try {
            Collection<Code> codes = codesService.generateListOfHealthcareFacilityTypeCodes();
            ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
            return responseEntity;
        } catch (CodesExecption e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }


    @Override
    public ResponseEntity<List<Code>> v1CodesObjectTypeCodeGet() {
        try {
            Collection<Code> codes = codesService.generateDocumentType();
            ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<Code>> v1CodesPracticeSettingCodeGet() {
        try {
            Collection<Code> codes = codesService.generateListOfPractiseSettingCodes();


            ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
            return responseEntity;
        } catch (CodesExecption e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<Code>> v1CodesTypeCodeGet() {
        try {
            Collection<Code> codes = codesService.generateListOfTypeCodes();


        ResponseEntity<List<Code>> responseEntity = new ResponseEntity(codes, HttpStatus.OK);
        return responseEntity;
        } catch (CodesExecption e) {
            // Add correct error InternaL??
            throw new RuntimeException(e);
        }
    }
}
