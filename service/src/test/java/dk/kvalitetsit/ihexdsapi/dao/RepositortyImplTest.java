package dk.kvalitetsit.ihexdsapi.dao;

import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dao.exception.ConnectionFailedExecption;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.utility.TestHelper;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class RepositortyImplTest extends AbstractRedisTest{





    @Autowired
    CredentialRepositoryImpl subject;



    @Test (expected = ConnectionFailedExecption.class)
    public void TestConnectionToRedisFailed() throws ConnectionFailedExecption, URISyntaxException, IOException {
        JedisConnectionFactory jedisConFactory
                    = new JedisConnectionFactory();
            jedisConFactory.setHostName("localhost");
            jedisConFactory.setPort(40321);


            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConFactory);


    CredentialRepositoryImpl credentialRepository = new CredentialRepositoryImpl(template, 3000);


        //When
        CredentialInfoEntity credentialInfoEntity;

        String id = "7w7777w";
        String displayName =  "My certificate";
        String owner = "Me";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, displayName,   publicKey, privateKey);


        credentialRepository.saveCredentialsForID(credentialInfoEntity);


    }

    @Test
    public void TestSaveCredentialEntityToRedis() throws URISyntaxException, IOException, InterruptedException {

        // Owner as key
        CredentialInfoEntity credentialInfoEntity;

        String id = "7w7777w";
        String displayName =  "My certificate";
        String owner = "Me";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, displayName, publicKey, privateKey);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));

        subject.saveCredentialsForID(credentialInfoEntity);


        CredentialInfoEntity result = ((CredentialInfoEntity) redisTemplate.opsForValue().get(id));


        Assert.assertNotNull(redisTemplate.opsForValue().get(id));

        TestHelper.waiter(3000 + 1);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));
    }

    @Test
    public void TestSaveCredentialEntityToRedisTwice() throws URISyntaxException, IOException, InterruptedException {

        // Owner as key
        CredentialInfoEntity credentialInfoEntity;

        String id = "ID 1";
        String id2 = "ID 2";
        String displayName =  "My certificate";
        String owner = "Test";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, displayName, publicKey, privateKey);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));

        subject.saveCredentialsForID(credentialInfoEntity);
        CredentialInfoEntity result = ((CredentialInfoEntity) redisTemplate.opsForValue().get(id));
        Assert.assertNotNull(result);

        // Wait one second to test expiry
        TestHelper.waiter(1000 + 1);
        credentialInfoEntity = new CredentialInfoEntity(owner, id2, displayName, publicKey, privateKey);

        subject.saveCredentialsForID(credentialInfoEntity);
        result = ((CredentialInfoEntity) redisTemplate.opsForValue().get(id2));
        Assert.assertNotNull(result);
        Assert.assertEquals(2000, redisTemplate.getExpire(id2).intValue() * 1000);
        Assert.assertEquals(2, subject.FindListOfIDsForOwner(owner).size());


        TestHelper.waiter(1000 + 1);
        TestHelper.waiter(1000 + 1);

        Assert.assertNull(redisTemplate.opsForValue().get(id));
        Assert.assertNull(redisTemplate.opsForValue().get(id2));


    }




    @Test
    public void TestGetCredentialByID() throws URISyntaxException, IOException {
        // Owner as key
        CredentialInfoEntity credentialInfoEntity;
        String id = "7w7777w";
        String displayName =  "My certificate";
        String owner = "Me";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, displayName, publicKey, privateKey);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));


        subject.saveCredentialsForID(credentialInfoEntity);

        CredentialInfoEntity result = subject.findCredentialInfoByID(id);
        Assert.assertNotNull(result);
    }
}