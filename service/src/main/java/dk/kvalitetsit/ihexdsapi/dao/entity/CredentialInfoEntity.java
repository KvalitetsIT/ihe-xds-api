package dk.kvalitetsit.ihexdsapi.dao.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("CredentialInfo")
public class CredentialInfoEntity implements Serializable {

    private String owner;

    public CredentialInfoEntity(String owner, String id, String cvr, String organisation, String publicCertStr, String privateKeyStr) {
        this.owner = owner;
        this.id = id;
        this.cvr = cvr;
        this.organisation = organisation;
        this.publicCertStr = publicCertStr;
        this.privateKeyStr = privateKeyStr;
    }

    private String id;

    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public String getCvr() {
        return cvr;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getPublicCertStr() {
        return publicCertStr;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    private String cvr;

    private String organisation;

    private String publicCertStr;

    private String privateKeyStr;
}
