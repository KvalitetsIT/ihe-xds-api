package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.controller.CodesController;
import dk.kvalitetsit.ihexdsapi.interceptors.impl.CacheRequestResponseHandleImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.CodesServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {dk.kvalitetsit.ihexdsapi.configuration.RedisConfiguration.class})
public class CacheRequestResponseHandleTest {
    @Test
    public void test (){}
    /*

    private CacheRequestResponseHandleImpl subject;
    private static final int REDIS_PORT = 6379;

    private static GenericContainer<?> redis;

    static  {
        redis = new GenericContainer("redis:7.0.4")
                .withExposedPorts(REDIS_PORT)
                .withCommand("redis-server /usr/local/etc/redis/redis.conf")
                .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf", BindMode.READ_ONLY);

        redis.start();

        Integer mappedRedisPort = redis.getMappedPort(REDIS_PORT);
        ;
        System.setProperty("redis.host", DockerClientFactory.instance().dockerHostIpAddress());
        System.setProperty("redis.port", mappedRedisPort.toString());

        System.setProperty("redis.data.ttl", "3000");
    }


    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    public String data, id;

    @Before
    public void setup() {
        subject = new CacheRequestResponseHandleImpl(redisTemplate);
        data = "This is a test";
        id = "TEST";
    }

    @After
    public void cleanup() {
        this.redisTemplate.opsForValue().getAndDelete(id);
    }

    @Test
    public void testGetRequestAndResponse() {

        Assert.assertNull(this.redisTemplate.opsForValue().get(id));
        this.redisTemplate.opsForValue().set(id, data);

        Assert.assertEquals(data, subject.getRequestAndResponse(id));
    }

    @Test
    public void saveRequestAndResponse() {
        Assert.assertNull(this.redisTemplate.opsForValue().get(id));

        subject.saveRequestAndResponse(id, data);

        Assert.assertEquals(data, subject.getRequestAndResponse(id));


    }*/
}
