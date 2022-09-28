package dk.kvalitetsit.ihexdsapi.interceptors.impl;

import dk.kvalitetsit.ihexdsapi.interceptors.CacheRequestResponseHandle;
import org.springframework.data.redis.core.RedisTemplate;


public class CacheRequestResponseHandleImpl implements CacheRequestResponseHandle {

    private RedisTemplate redisTemplate;

    public CacheRequestResponseHandleImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;

    }


    @Override
    public String getRequestAndResponse(String id) {
        String result = ((String) this.redisTemplate.opsForValue().get(id));
        if (result == null) {
            result = "";
        }
        return result;
    }

    @Override
    public void saveRequestAndResponse(String id, String payload) {
        // Time To Live ???
        this.redisTemplate.opsForValue().set(id,payload);

    }
}
