package dk.kvalitetsit.ihexdsapi.dao.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("CredentialInfo")
public class CredentialInfoEntity implements Serializable {

    private String owner;
    private String id;
    private String displayName;
    private String publicCertStr;

    private String privateKeyStr;

    private String serialNumber;
    private String type;

    public CredentialInfoEntity(String owner, String id, String displayName, String publicCertStr, String privateKeyStr, String serialNumber, String type) {
        this.owner = owner;
        this.id = id;
        this.displayName = displayName;
        this.publicCertStr = publicCertStr;
        this.privateKeyStr = privateKeyStr;
        this.type = type;
        this.serialNumber = serialNumber;
    }



    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }


    public String getPublicCertStr() {
        return publicCertStr;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getType() {
        return type;
    }
}
