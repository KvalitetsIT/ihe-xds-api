package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.ConfigsService;
import org.openapitools.model.ConfigResponse;

import java.util.*;

public class ConfigsServiceImpl implements ConfigsService {


    private String sts, xdsIti18, xdsIti43;

    private List<ConfigResponse> configResponseList;


    public ConfigsServiceImpl(String sts, String xdsIti18, String xdsIti43) {
        this.sts = sts;
        this.xdsIti18 = xdsIti18;
        this.xdsIti43 = xdsIti43;

        makeList();


    }

    private void makeList() {
        List<ConfigResponse> list= new LinkedList<>();
        // variables
        list.add(createResponseObject("sts.url", sts));
        list.add(createResponseObject("iti-18.url", xdsIti18));
        list.add(createResponseObject("iti-43.url", xdsIti43));

        Collections.sort(list, new SortbyKey());

        configResponseList = list;

    }

    private ConfigResponse createResponseObject(String key, String value) {

        ConfigResponse obj = new ConfigResponse();
        obj.setConfigKey(key);
        obj.setConfigValue(value);

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

