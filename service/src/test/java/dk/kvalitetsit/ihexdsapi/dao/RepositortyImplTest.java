package dk.kvalitetsit.ihexdsapi.dao;

import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dao.impl.CredentialRepositoryImpl;
import dk.kvalitetsit.ihexdsapi.utility.UtilityTest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {dk.kvalitetsit.ihexdsapi.configuration.RedisConfiguration.class})
public class RepositortyImplTest {

    private static final int REDIS_PORT = 6379;

    static {
        GenericContainer<?> redis = new GenericContainer("redis:7.0.4")
                .withExposedPorts(REDIS_PORT)
                .withCommand("redis-server /usr/local/etc/redis/redis.conf")
                .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf", BindMode.READ_ONLY);
        redis.start();

        Integer mappedRedisPort = redis.getMappedPort(REDIS_PORT);
        System.setProperty("redis.host", "localhost");
        System.setProperty("redis.port", mappedRedisPort.toString());
    }


  @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    @Autowired
    CredentialRepositoryImpl subject;


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

        int ttl = 10;
        subject.saveCredentialsForID(credentialInfoEntity, ttl);

        CredentialInfoEntity result = ((CredentialInfoEntity)redisTemplate.opsForValue().get(id));

        System.out.println(result.getOwner());

        Assert.assertNotNull(redisTemplate.opsForValue().get(id));

        UtilityTest.waiter(ttl + 1);

        System.out.println((redisTemplate.opsForValue().get(owner)));
        Assert.assertNull(redisTemplate.opsForValue().get(owner));
    }

    @Test
    public void TestSaveCertEnitity() throws URISyntaxException, IOException {

        List<String> list = new LinkedList<>();
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));
        String dummyKey = "This is dummy data";

        list.add(publicKey);
        list.add(dummyKey);

        String owner = "John Doe";

        int ttl = 10;

        subject.saveListOfCertsForUser(owner, list, ttl);

        LinkedList<String> result = ((LinkedList<String>) redisTemplate.opsForValue().get(owner));
        System.out.println(result);
        Assert.assertEquals(2, result.size());
        Assert.assertNotNull(result);
        UtilityTest.waiter(ttl + 1);

        Assert.assertNull(redisTemplate.opsForValue().get(owner));

    }

    // Muligvis overflødig
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

        int ttl = 10;
        subject.saveCredentialsForID(credentialInfoEntity, ttl);

        CredentialInfoEntity result = subject.findByID(id);
        Assert.assertNotNull(result);
    }
    // Også muligvis overflødig
    @Test
    public void TestGetCertsByOwner() throws URISyntaxException, IOException {
        List<String> list = new LinkedList<>();
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));
        String dummyKey = "This is dummy data";

        list.add(publicKey);
        list.add(dummyKey);

        String owner = "John Doe";

        int ttl = 10;

        subject.saveListOfCertsForUser(owner, list, ttl);

        List<String> result = subject.findByOwner(owner);
        System.out.println(result);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void TestUpdateCertsforOwner() throws URISyntaxException, IOException {
        List<String> list = new LinkedList<>();
        String publicKey = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));
        String dummyKey = "This is dummy data";
        String dummyKey2 = "This is also dummy data";

        list.add(publicKey);
        list.add(dummyKey);

        String owner = "John Doe";

        int ttl = 5000;

        subject.saveListOfCertsForUser(owner, list, ttl);

        List<String> result = subject.findByOwner(owner);
        System.out.println(result);
        Assert.assertEquals(2, result.size());

        list.add(dummyKey2);

        subject.updateCerts(owner, list, ttl);

         result = subject.findByOwner(owner);
        System.out.println(result);
        Assert.assertEquals(3, result.size());

        UtilityTest.waiter(ttl + 100);

        result = subject.findByOwner(owner);
        Assert.assertNull(result);

    }
}
