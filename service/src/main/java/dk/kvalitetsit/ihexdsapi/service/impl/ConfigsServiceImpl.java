package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dao.entity.Iti41RepositoryProfile;
import dk.kvalitetsit.ihexdsapi.service.ConfigsService;
import dk.kvalitetsit.ihexdsapi.service.Iti41RepositoryProfileService;
import org.openapitools.model.ConfigResponse;

import javax.annotation.PostConstruct;
import java.util.*;

public class ConfigsServiceImpl implements ConfigsService {


    private String sts, xdsIti18, xdsIti43;


    private List<ConfigResponse> configResponseList;

    private Iti41RepositoryProfileService iti41RepositoryProfileService;


    public ConfigsServiceImpl(String sts, String xdsIti18, String xdsIti43, Iti41RepositoryProfileService iti41RepositoryProfileService) {
        this.sts = sts;
        this.xdsIti18 = xdsIti18;
        this.xdsIti43 = xdsIti43;

        this.iti41RepositoryProfileService = iti41RepositoryProfileService;

        makeList();


    }

    @PostConstruct
    private void makeList() {
        List<ConfigResponse> list= new LinkedList<>();
        // variables
        list.add(createResponseObject("sts.url", sts));
        list.add(createResponseObject("iti-18.url", xdsIti18));
        list.add(createResponseObject("iti-43.url", xdsIti43));

        list.add(createResponseObjectForIti41());


        Collections.sort(list, new SortbyKey());

        configResponseList = list;

    }



    private ConfigResponse createResponseObject(String key, String value) {

        ConfigResponse obj = new ConfigResponse();
        obj.setConfigKey(key);
        obj.setConfigValue(value);

        return obj;
    }

    private ConfigResponse createResponseObjectForIti41() {

        String str = "";


        for (int i = 0; i < this.iti41RepositoryProfileService.getRepositoryProfileList().size(); i++) {
            Iti41RepositoryProfile repo = this.iti41RepositoryProfileService.getRepositoryProfileList().get(i);
            String s = repo.getName()  + ": " + repo.getEndpoint();

            if (i + 1 != this.iti41RepositoryProfileService.getRepositoryProfileList().size()) {
                s = s + "\n";
            }

            str = str + s;
        }


        ConfigResponse obj = new ConfigResponse();
        obj.setConfigKey("iti-41.url");
        obj.setConfigValue(str);

        return obj;
    }


    @Override
    public List<ConfigResponse> getListOfConfigResponses() {
        return this.configResponseList;
    }

}

class SortbyKey implements Comparator<ConfigResponse> {


    @Override
    public int compare(ConfigResponse o1, ConfigResponse o2) {
        return o1.getConfigKey().compareToIgnoreCase(o2.getConfigKey());
    }
}

