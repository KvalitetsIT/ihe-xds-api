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

    private int ttl;

    private RedisTemplate redisTemplate;

    public CredentialRepositoryImpl(RedisTemplate redisTemplate, int ttl) {
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }


    @Override
    public boolean saveCredentialsForID(CredentialInfoEntity credentialInfo) {

        List<String> newList = null;
        try {
            if (FindListOfIDsForOwner(credentialInfo.getOwner()) == null) {
                newList = new LinkedList<>();
                newList.add(credentialInfo.getId());
                redisTemplate.opsForValue().set(credentialInfo.getId(), credentialInfo, Duration.ofMillis(ttl));
                saveListOfIDsForOwner(credentialInfo.getOwner(), newList);
            } else {
                newList = FindListOfIDsForOwner(credentialInfo.getOwner());
                newList.add(credentialInfo.getId());
                redisTemplate.opsForValue().set(credentialInfo.getId(), credentialInfo,
                        Duration.ofMillis(getExpiryTimeLeftInMilliSeconds(newList.get(0))));
                saveListOfIDsForOwner(credentialInfo.getOwner(), newList);
            }
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private int getExpiryTimeLeftInMilliSeconds(String id) {
        return 1000 * (redisTemplate.getExpire(id).intValue());
    }

    private boolean saveListOfIDsForOwner(String owner, List<String> list) {
        try {
            redisTemplate.opsForValue().set(owner, list, Duration.ofMillis(ttl));
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public CredentialInfoEntity findCredentialInfoByID(String id) {

        return ((CredentialInfoEntity) redisTemplate.opsForValue().get(id));
    }

    @Override
    public List<String> FindListOfIDsForOwner(String owner) {
        return ((LinkedList<String>) redisTemplate.opsForValue().get(owner));
    }


}
