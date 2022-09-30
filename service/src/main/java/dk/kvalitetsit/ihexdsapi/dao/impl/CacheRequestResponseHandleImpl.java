package dk.kvalitetsit.ihexdsapi.dao.impl;

import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dao.entity.LogEntry;
import dk.kvalitetsit.ihexdsapi.service.UtilityService;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;


public class CacheRequestResponseHandleImpl implements CacheRequestResponseHandle {

    private RedisTemplate redisTemplate;


    public CacheRequestResponseHandleImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;


    }


    @Override
    public String getRequestAndResponse(String id) {
        LogEntry collected = (LogEntry) this.redisTemplate.opsForValue().get(id);
        String result = "";
        if (collected != null) {
            result = collected.getPayload();
        }
        return result;
    }

    @Override
    public void saveRequestAndResponse(String id, LogEntry payload) {
        // Time To Live 2 hours
        this.redisTemplate.opsForValue().set(id,payload, Duration.ofMillis(7200000));


    }
}
