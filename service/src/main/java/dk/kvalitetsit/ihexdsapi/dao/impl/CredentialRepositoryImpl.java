package dk.kvalitetsit.ihexdsapi.dao.impl;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dao.exception.ConnectionFailedExecption;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CredentialRepositoryImpl implements CredentialRepository {

    private int ttl;

    private RedisTemplate redisTemplate;

    private final String ERROR_MESSAGE = "Failed to connect to Redis";
    private final int ERROR_CODE = 1;

    public CredentialRepositoryImpl(RedisTemplate redisTemplate, int ttl) {
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }

    // Might be a fix for ttl not loading??
    public CredentialRepositoryImpl() {

    }


    @Override
    public boolean saveCredentialsForID(CredentialInfoEntity credentialInfo) {

        List<String[]> newList = null;

        String[] idAndDisplayName = new String[] {credentialInfo.getId(), credentialInfo.getDisplayName()};
        try {
            if (FindListOfIDsForOwner(credentialInfo.getOwner()) == null) {
                newList = new LinkedList<>();


                newList.add(idAndDisplayName);
                redisTemplate.opsForValue().set(credentialInfo.getId(), credentialInfo, Duration.ofMillis(ttl));
                saveListOfIDsForOwner(credentialInfo.getOwner(), newList);
            } else {
                newList = FindListOfIDsForOwner(credentialInfo.getOwner());
                newList.add(idAndDisplayName);
                redisTemplate.opsForValue().set(credentialInfo.getId(), credentialInfo,
                        Duration.ofMillis(getExpiryTimeLeftInMilliSeconds(newList.get(0)[0])));
                saveListOfIDsForOwner(credentialInfo.getOwner(), newList);
            }
            return true;
        } catch (RuntimeException e) {
            throw new ConnectionFailedExecption(ERROR_CODE, ERROR_MESSAGE);
        }
    }

    private int getExpiryTimeLeftInMilliSeconds(String id) {
        return 1000 * (redisTemplate.getExpire(id).intValue());
    }

    private boolean saveListOfIDsForOwner(String owner, List<String[]> list) {
        try {
            redisTemplate.opsForValue().set(owner, list, Duration.ofMillis(ttl));
            return true;
        } catch (RuntimeException e) {
            throw new ConnectionFailedExecption(ERROR_CODE, ERROR_MESSAGE);

        }
    }

    @Override
    public CredentialInfoEntity findCredentialInfoByID(String id) {
        try {
            return ((CredentialInfoEntity) redisTemplate.opsForValue().get(id));
        } catch (RuntimeException e) {
            throw new ConnectionFailedExecption(ERROR_CODE, ERROR_MESSAGE);
        }
    }

    @Override
    public List<String[]> FindListOfIDsForOwner(String owner) {

        try {
            return ((LinkedList<String[]>) redisTemplate.opsForValue().get(owner));
        } catch (RuntimeException e) {
            throw new ConnectionFailedExecption(ERROR_CODE, ERROR_MESSAGE);
        }

    }
}
