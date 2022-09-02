package dk.kvalitetsit.ihexdsapi.service;

import org.openapitools.model.Code;

import java.util.List;

public interface CodesService {
    List<Code> generateListOfTypeCodes() throws CodesExecption;
    List<Code> generateListOfFormatCodes() throws CodesExecption;
    List<Code> generateListOfHealthcareFacilityTypeCodes() throws CodesExecption;
    List<Code> generateListOfEventCodes() throws CodesExecption;
    List<Code> generateListOfPractiseSettingCodes() throws CodesExecption;
    List<Code> generateListOfAvailabilityStatus() ;

    List<Code> generateDocumentType() ;








}
