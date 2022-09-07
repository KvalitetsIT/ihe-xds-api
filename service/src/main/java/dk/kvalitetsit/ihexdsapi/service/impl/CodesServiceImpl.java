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


    // utility;
    public CodesServiceImpl(String typeCodeCodes, String typeCodeNames, String typeCodeScheme,
                            String formatCodeCodes, String formatCodeNames, String formatCodeScheme,
                            String eventCodeCodes, String eventCodeNames, String eventCodeScheme
            , String healthcareFacilityTypeCodeCodes, String healthcareFacilityTypeCodeNames, String healthcareFacilityTypeCodeScheme,
                            String practiceSettingCodeCodes, String practiceSettingCodeNames, String practiceSettingCodeScheme
            , String classCodeCodes, String classCodeNames, String classCodeScheme,
                            String objectTypeCodes, String objectTypeNames) {
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

    }

    public CodesServiceImpl() {
    }

    private String[] splitStringToArray(String inputString) {
        return inputString.split(";");
    }

    @Override
    public List<Code> generateListOfTypeCodes() throws CodesExecption {

        String msg = "Amount of type codes doesn't match amount of type codes names";
        return this.generateListOfCodes(this.typeCodeCodes, this.typeCodeNames, typeCodeScheme, msg);
    }

    @Override
    public List<Code> generateListOfFormatCodes() throws CodesExecption {
        String msg = "Format code execption";
        return this.generateListOfCodes(this.formatCodeCodes, this.formatCodeNames, formatCodeScheme, msg);
    }

    @Override
    public List<Code> generateListOfHealthcareFacilityTypeCodes() throws CodesExecption {
        String msg = "Healthcare facility code execption";
        return this.generateListOfCodes(this.healthcareFacilityTypeCodeCodes, this.healthcareFacilityTypeCodeNames, this.healthcareFacilityTypeCodeScheme, msg);
    }

    @Override
    public List<Code> generateListOfEventCodes() throws CodesExecption {
        String msg = "Event scheme execption";
        return this.generateListOfCodes(this.eventCodeCodes, this.eventCodeNames, "", msg);
    }

    @Override
    public List<Code> generateListOfPractiseSettingCodes() throws CodesExecption {
        String msg = "Practice setting codes execption";
        return this.generateListOfCodes(this.practiceSettingCodeCodes, this.practiceSettingCodeNames, this.practiceSettingCodeScheme, msg);
    }

    @Override
    public List<Code> generateListOfAvailabilityStatus()  {
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

    @Override
    public List<Code> generateDocumentType() {
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


}
