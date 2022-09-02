package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.service.impl.CodesServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.model.Code;

import java.util.List;

public class CodesServiceImplTest {

    //@Autowired
    private CodesServiceImpl subject;

    @Before
    public void setup() {

        subject = new CodesServiceImpl();
    }

    @Test
    public void testGenerateListOfTypeCodes() throws CodesExecption {
        String codes = "53576-5;74468-0;74465-6;11502-2;56446-8;";
        String names = "Personal health monitoring report Document;Questionnaire Form Definition Document;Questionnaire Response Document;LABORATORY REPORT.TOTAL;Appointment Summary Document;";
        String scheme = "2.16.840.1.113883.6.1";
        String filler = "";
        subject = new CodesServiceImpl(codes, names, scheme, filler, filler, filler, filler, filler,
                filler, filler, filler, filler, filler, filler, filler, filler,filler, filler, filler, filler);

        List<Code> codeList = subject.generateListOfTypeCodes();

        Assert.assertNotNull(codeList);
        Assert.assertEquals(5, codeList.size());

    }

    @Test(expected = CodesExecption.class)
    public void testGenerateListOfTypeCodesListSizesNotMatching() throws CodesExecption {
        String codes = "53576-5;74468-0;74465-6;11502-2;56446-8;83322-2";
        String names = "Personal health monitoring report Document;Questionnaire Form Definition Document;Questionnaire Response Document;LABORATORY REPORT.TOTAL;Appointment Summary Document;";
        String scheme = "2.16.840.1.113883.6.1";
        String filler = "";



        subject = new CodesServiceImpl(codes, names, scheme, filler, filler, filler, filler, filler,
                filler, filler, filler, filler, filler, filler, filler, filler,filler, filler, filler, filler);

       subject.generateListOfTypeCodes();

    }


}
