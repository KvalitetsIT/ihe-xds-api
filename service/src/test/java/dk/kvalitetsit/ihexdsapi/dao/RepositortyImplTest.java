package dk.kvalitetsit.ihexdsapi.dao;

import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dao.exception.ConnectionFailedExecption;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.utility.TestHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {dk.kvalitetsit.ihexdsapi.configuration.RedisConfiguration.class})
public class RepositortyImplTest {

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

    @Autowired
    CredentialRepositoryImpl subject;

    @Test (expected = ConnectionFailedExecption.class)
    public void TestConnectionToRedisFailed() throws ConnectionFailedExecption, URISyntaxException, IOException {
        redis.close();

        //When
        CredentialInfoEntity credentialInfoEntity;
        String cvr = "637283d";
        String id = "7w7777w";
        String org = "Statens Serum Institute";
        String owner = "Me";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, cvr, org, publicKey, privateKey);


        subject.saveCredentialsForID(credentialInfoEntity);

        redis.start();
    }

    @Test
    public void TestSaveCredentialEntityToRedis() throws URISyntaxException, IOException, InterruptedException {

        // Owner as key
        CredentialInfoEntity credentialInfoEntity;
        String cvr = "637283d";
        String id = "7w7777w";
        String org = "Statens Serum Institute";
        String owner = "Me";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, cvr, org, publicKey, privateKey);

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
        String cvr = "637283d";
        String id = "ID 1";
        String id2 = "ID 2";
        String org = "Statens Serum Institute";
        String owner = "Test";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, cvr, org, publicKey, privateKey);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));

        subject.saveCredentialsForID(credentialInfoEntity);
        CredentialInfoEntity result = ((CredentialInfoEntity) redisTemplate.opsForValue().get(id));
        Assert.assertNotNull(result);

        // Wait one second to test expiry
        TestHelper.waiter(1000 + 1);
        credentialInfoEntity = new CredentialInfoEntity(owner, id2, cvr, org, publicKey, privateKey);

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
        String cvr = "637283d";
        String id = "7w7777w";
        String org = "Statens Serum Institute";
        String owner = "Me";
        String privateKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

        credentialInfoEntity = new CredentialInfoEntity(owner, id, cvr, org, publicKey, privateKey);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));


        subject.saveCredentialsForID(credentialInfoEntity);

        CredentialInfoEntity result = subject.findCredentialInfoByID(id);
        Assert.assertNotNull(result);
    }

}
