package dk.kvalitetsit.ihexdsapi.service;

import java.util.HashMap;

public interface UtilityService {

    HashMap<String, String> getIds();

    String getId(String key);

    void updateId(String key, String idValue);
}
