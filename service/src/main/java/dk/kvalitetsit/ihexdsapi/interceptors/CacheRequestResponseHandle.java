package dk.kvalitetsit.ihexdsapi.interceptors;


public interface CacheRequestResponseHandle {

    String getRequestAndResponse(String id);

    void saveRequestAndResponse(String id, String payload);


}
