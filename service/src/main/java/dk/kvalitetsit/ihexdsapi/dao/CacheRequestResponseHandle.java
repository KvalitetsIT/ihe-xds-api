package dk.kvalitetsit.ihexdsapi.dao;


import dk.kvalitetsit.ihexdsapi.dao.entity.LogEntry;

public interface CacheRequestResponseHandle {

    String getRequestAndResponse(String id);

    void saveRequestAndResponse(String id, LogEntry payload);


}
