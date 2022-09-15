package dk.kvalitetsit.ihexdsapi.service;

import org.openapitools.model.Code;

import java.util.List;

public interface CodesService {


    List<Code> getFormatCodesList();
    List<Code> getHealthcareFacilityTypeCodesList();
    List<Code> getEventCodesList();
    List<Code> getPractiseSettingCodesList();
    List<Code> getAvailabilityStatusList();
    List<Code> getDocumentTypeList();
    List<Code> getTypeCodesList();











}
