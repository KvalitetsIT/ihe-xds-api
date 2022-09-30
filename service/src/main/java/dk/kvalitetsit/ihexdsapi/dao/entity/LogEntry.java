package dk.kvalitetsit.ihexdsapi.dao.entity;

import java.io.Serializable;

public class LogEntry implements Serializable {
    private String id;

    private String payload;

    public LogEntry(String id, String payload) {
        this.id = id;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }


    public String getPayload() {
        return payload;
    }

}
