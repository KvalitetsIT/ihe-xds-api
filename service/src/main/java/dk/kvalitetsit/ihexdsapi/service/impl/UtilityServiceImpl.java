package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.UtilityService;

import java.util.HashMap;

public class UtilityServiceImpl implements UtilityService {
    private HashMap<String, String> temporaryIdMap;

    public UtilityServiceImpl() {
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
