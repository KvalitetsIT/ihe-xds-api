package dk.kvalitetsit.ihexdsapi.configuration;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {


    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.data.ttl}")
    private int ttl;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory()  {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory(getJedisPool());
        jedisConFactory.setHostName(redisHost);
        jedisConFactory.setPort(redisPort);

        return jedisConFactory;
    }

    private JedisPoolConfig getJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMinIdle(0);



        return config;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public CredentialRepository credentialRepository() {
        return new CredentialRepositoryImpl(redisTemplate(),ttl);
    }
}
