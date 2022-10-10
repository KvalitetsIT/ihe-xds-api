package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.IDContextService;

import java.util.HashMap;

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
