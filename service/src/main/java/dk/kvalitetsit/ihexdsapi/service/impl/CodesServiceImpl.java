package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.CodesExecption;
import dk.kvalitetsit.ihexdsapi.service.CodesService;
import org.openapitools.model.Code;

import java.util.LinkedList;
import java.util.List;

public class CodesServiceImpl implements CodesService {

    private String[] typeCodeCodes;
    private String[] typeCodeNames;
    private String typeCodeScheme;

    private String[] formatCodeCodes;
    private String[] formatCodeNames;
    private String formatCodeScheme;

    private String[] eventCodeCodes;
    private String[] eventCodeNames;
    private String eventCodeScheme;

    private String[] healthcareFacilityTypeCodeCodes;
    private String[] healthcareFacilityTypeCodeNames;
    private String healthcareFacilityTypeCodeScheme;

    private String[] practiceSettingCodeCodes;
    private String[] practiceSettingCodeNames;
    private String practiceSettingCodeScheme;

    private String[] classCodeCodes;
    private String[] classCodeNames;
    private String classCodeScheme;

    // Might be getting an environmental variable
    private String[] objectTypeCodes;
    private String[] objectTypeNames;

    // Code lists variables

    private List<Code> typeCodesList;
    private List<Code> formatCodesList;
    private List<Code> healthcareFacilityTypeCodesList;
    private List<Code> eventCodesList;
    private List<Code> practiseSettingCodesList;
    private List<Code> availabilityStatusList;
    private List<Code> documentTypeList;

    private List<Code> classCodeList;



    // utility;
    public CodesServiceImpl(String typeCodeCodes, String typeCodeNames, String typeCodeScheme,
                            String formatCodeCodes, String formatCodeNames, String formatCodeScheme,
                            String eventCodeCodes, String eventCodeNames, String eventCodeScheme
            , String healthcareFacilityTypeCodeCodes, String healthcareFacilityTypeCodeNames, String healthcareFacilityTypeCodeScheme,
                            String practiceSettingCodeCodes, String practiceSettingCodeNames, String practiceSettingCodeScheme
            , String classCodeCodes, String classCodeNames, String classCodeScheme,
                            String objectTypeCodes, String objectTypeNames) throws CodesExecption {
        this.typeCodeCodes = splitStringToArray(typeCodeCodes);
        this.typeCodeScheme = typeCodeScheme;
        this.typeCodeNames = splitStringToArray(typeCodeNames);

        this.formatCodeCodes = splitStringToArray(formatCodeCodes);
        this.formatCodeScheme = formatCodeScheme;
        this.formatCodeNames = splitStringToArray(formatCodeNames);

        this.eventCodeCodes = splitStringToArray(eventCodeCodes);
        this.eventCodeScheme = eventCodeScheme; // can be deleted
        this.eventCodeNames = splitStringToArray(eventCodeNames);

        this.healthcareFacilityTypeCodeCodes = splitStringToArray(healthcareFacilityTypeCodeCodes);
        this.healthcareFacilityTypeCodeScheme = healthcareFacilityTypeCodeScheme;
        this.healthcareFacilityTypeCodeNames = splitStringToArray(healthcareFacilityTypeCodeNames);

        this.practiceSettingCodeCodes = splitStringToArray(practiceSettingCodeCodes);
        this.practiceSettingCodeScheme = practiceSettingCodeScheme;
        this.practiceSettingCodeNames = splitStringToArray(practiceSettingCodeNames);

        this.classCodeCodes = splitStringToArray(classCodeCodes);
        this.classCodeScheme = classCodeScheme;
        this.classCodeNames = splitStringToArray(classCodeNames);

        this.objectTypeCodes = splitStringToArray(objectTypeCodes);
        this.objectTypeNames = splitStringToArray(objectTypeNames);





        // Generate lister her.
        typeCodesList = this.generateListOfCodes(this.typeCodeCodes,
                this.typeCodeNames, typeCodeScheme,
                "Amount of type codes doesn't match amount of type codes names");
        formatCodesList = this.generateListOfCodes(this.formatCodeCodes, this.formatCodeNames, formatCodeScheme, "msg");
        healthcareFacilityTypeCodesList = this.generateListOfCodes(this.healthcareFacilityTypeCodeCodes, this.healthcareFacilityTypeCodeNames, this.healthcareFacilityTypeCodeScheme, "msg");
        eventCodesList = this.generateListOfCodes(this.eventCodeCodes, this.eventCodeNames, "", "msg");
        practiseSettingCodesList =  this.generateListOfCodes(this.practiceSettingCodeCodes, this.practiceSettingCodeNames, this.practiceSettingCodeScheme, "msg");
        availabilityStatusList = this.generateListOfAvailabilityStatus();
        documentTypeList = this.generateDocumentType();
        classCodeList = this.generateListOfCodes(this.classCodeCodes, this.classCodeNames, this.classCodeScheme, "msg");
    }

    public CodesServiceImpl() {
    }

    private String[] splitStringToArray(String inputString) {
        return inputString.split(";");
    }



    private List<Code> generateListOfAvailabilityStatus()  {
        List<Code> codesCollection = new LinkedList<>();

        // Approved
        Code c = new Code();
        c.setCode("APPROVED");
        c.setName("Approved");
        c.setScheme(" ");

        codesCollection.add(c);
        // Deprecated
        c = new Code();
        c.setCode("DEPRECATED");
        c.setName("Deprecated");
        c.setScheme(" ");

        codesCollection.add(c);
        // Submitted
            c = new Code();
            c.setCode("SUBMITTED");
            c.setName("Submitted");
            c.setScheme(" ");

            codesCollection.add(c);
        return codesCollection;
    }


    private List<Code> generateDocumentType() {
        List<Code> codesCollection = new LinkedList<>();

        // Approved
        Code c = new Code();
        c.setCode("STABLE");
        c.setName("Stable");
        c.setScheme(" ");

        codesCollection.add(c);
        // Deprecated
        c = new Code();
        c.setCode("ON-DEMAND");
        c.setName("On-demand");
        c.setScheme(" ");
        codesCollection.add(c);

        return codesCollection;

    }


    private List<Code> generateListOfCodes(String[] codesArray, String[] namesArray, String scheme, String execptionMsg) throws CodesExecption {
        if (codesArray.length != namesArray.length) {
            throw new CodesExecption(200, execptionMsg);
        }
        List<Code> codesCollection = new LinkedList<>();

        for (int i = 0; i < codesArray.length; i++) {
            Code temp = new Code();
            temp.setCode(codesArray[i]);
            temp.setName(namesArray[i]);
            temp.setScheme(scheme);
            codesCollection.add(temp);
        }
        return codesCollection;
    }

    public List<Code> getTypeCodesList() {
        return List.copyOf(typeCodesList);
    }

    @Override
    public String getClassCodeFromTypeCode(String typeCode) {
        List<Code> typeCodes = getTypeCodesList();
        int i = 0;

        while (i < typeCodes.size()) {
            if (typeCodes.get(i).getCode().equals(typeCodes)) {
                return typeCodes.get(i).getCode();
            }
            i++;
        }
        return null;
    }

    @Override
    public String getClassCodeNameFromCode(String classCode) {
        List<Code> classCodes = getClassCodeList();
        int i = 0;

        while (i < classCodes.size()) {
            if (classCodes.get(i).getCode().equals(classCode)) {
                return classCodes.get(i).getName();
            }
            i++;
        }
        return null;
    }

    public List<Code> getFormatCodesList() {
        return List.copyOf(formatCodesList);
    }

    public List<Code> getHealthcareFacilityTypeCodesList() {
        return List.copyOf(healthcareFacilityTypeCodesList);
    }

    public List<Code> getEventCodesList() {
        return List.copyOf(eventCodesList);
    }

    public List<Code> getPractiseSettingCodesList() {
        return List.copyOf(practiseSettingCodesList);
    }

    public List<Code> getAvailabilityStatusList() {
        return List.copyOf(availabilityStatusList);
    }

    public List<Code> getDocumentTypeList() {
        return List.copyOf(documentTypeList);
    }

    public List<Code> getClassCodeList() {
        return List.copyOf(classCodeList);
    }


}
