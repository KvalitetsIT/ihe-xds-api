package dk.kvalitetsit.ihexdsapi.dao.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@RedisHash("CertList")
public class CertificateListEnitity implements Serializable {

    private String owner;

    private List<String> certificates = new LinkedList<>();

    public CertificateListEnitity(String owner, List<String> certificates) {
        this.owner = owner;
        this.certificates = certificates;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getCertificates() {
        return certificates;
    }
}
