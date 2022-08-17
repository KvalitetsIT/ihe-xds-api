package dk.kvalitetsit.ihexdsapi.dao.impl;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CredentialRepositoryImpl implements CredentialRepository {

    private RedisTemplate redisTemplate;

    public CredentialRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
     }


    @Override
    public boolean saveCredentialsForID(CredentialInfoEntity credentialInfo, int TTL) {
        redisTemplate.opsForValue().set(credentialInfo.getId(), credentialInfo, Duration.ofMillis(TTL));
        return true;
    }

    @Override
    public boolean saveListOfCertsForUser(String owner, List<String> list, int TTL) {
        redisTemplate.opsForValue().set(owner, list, Duration.ofMillis(TTL));
        return true;
    }

    @Override
    public CredentialInfoEntity findByID(String id) {

        return ((CredentialInfoEntity)redisTemplate.opsForValue().get(id));
    }

    @Override
    public List<String> findByOwner(String owner) {
        return ((LinkedList<String>) redisTemplate.opsForValue().get(owner));
    }

    @Override
    public boolean updateCerts(String owner, List<String> list, int TTL) {
        this.saveListOfCertsForUser(owner, list, TTL);
        return true;
    }




}
