package dk.kvalitetsit.ihexdsapi.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {dk.kvalitetsit.ihexdsapi.configuration.RedisConfiguration.class})
public abstract class AbstractRedisTest {
    private static final int REDIS_PORT = 6379;

    protected static GenericContainer<?> redis;
    // TODO

    static  {

        redis = new GenericContainer("redis:7.0.4")
                .withExposedPorts(REDIS_PORT)
                .withCommand("redis-server /usr/local/etc/redis/redis.conf")
                .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf", BindMode.READ_ONLY)
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(20)));

        redis.start();


        Integer mappedRedisPort = redis.getMappedPort(REDIS_PORT);
        ;
        System.setProperty("redis.host", DockerClientFactory.instance().dockerHostIpAddress());
        System.setProperty("redis.port", mappedRedisPort.toString());

        System.setProperty("redis.data.ttl", "3000");
    }
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
}
