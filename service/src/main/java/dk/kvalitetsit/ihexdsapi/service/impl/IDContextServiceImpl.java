package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.IDContextService;

import java.util.HashMap;

/**
 * This is the ID context which keeps track of the ID for requests and responses
 * when you want to downlod the previous request or ide as a file.
 *
*/
public class IDContextServiceImpl implements IDContextService {
    private HashMap<String, String> temporaryIdMap;

    public IDContextServiceImpl() {
        temporaryIdMap = new HashMap<>();
    }


    @Override
    public HashMap<String, String> getIds() {
        return (HashMap<String, String>) temporaryIdMap.values();
    }

    @Override
    public String getId(String key) {
        return temporaryIdMap.get(key);
    }

    @Override
    public void updateId(String key, String idValue) {
        temporaryIdMap.put(key, idValue);

    }
}
