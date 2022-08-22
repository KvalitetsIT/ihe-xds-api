package dk.kvalitetsit.ihexds.integrationtest;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import com.github.dockerjava.api.model.VolumesFrom;

import dk.kvalitetsit.ihexdsapi.VideoLinkHandlerApplication;

public class ServiceStarter {
    private static final Logger logger = LoggerFactory.getLogger(ServiceStarter.class);
    private static final Logger serviceLogger = LoggerFactory.getLogger("ihe-xds-api");
    private static final Logger mariadbLogger = LoggerFactory.getLogger("mariadb");
    private static final Logger redisLogger = LoggerFactory.getLogger("redis");

    private static final int ttl = 3000;

    private Network dockerNetwork;
    private String jdbcUrl;

    private int mappedRedisPort;

    public void startServices() throws IOException {
        dockerNetwork = Network.newNetwork();


        setupDatabaseContainer();
        setupRedisContainer();

        System.setProperty("STSURL", "http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService");
        System.setProperty("xdsIti18Endpoint", "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsregistry");

        // Gets correct test paths
        var privateKey = new ClassPathResource("/certificates/private-cert1.pem");
        var publicKey = new ClassPathResource("/certificates/public-cert1.cer");

        System.setProperty("default.cert.private", privateKey.getFile().getAbsolutePath());
        System.setProperty("default.cert.public", publicKey.getFile().getAbsolutePath());
        System.setProperty("redis.host", "localhost");
        System.setProperty("redis.port", Integer.toString(mappedRedisPort));

        System.setProperty("ttl", Integer.toString(ttl));


        SpringApplication.run((VideoLinkHandlerApplication.class));
    }

    public GenericContainer startServicesInDocker() {
        dockerNetwork = Network.newNetwork();

        setupDatabaseContainer();
        setupRedisContainer();


        var resourcesContainerName = "ihe-xds-api-resources";
        var resourcesRunning = containerRunning(resourcesContainerName);
        logger.info("Resource container is running: " + resourcesRunning);

        GenericContainer service;

        // Start service
        if (resourcesRunning) {
            VolumesFrom volumesFrom = new VolumesFrom(resourcesContainerName);
            service = new GenericContainer<>("local/ihe-xds-api-qa:dev")
                    .withCreateContainerCmdModifier(modifier -> modifier.withVolumesFrom(volumesFrom))
                    .withEnv("JVM_OPTS", "-javaagent:/jacoco/jacocoagent.jar=output=file,destfile=/jacoco-report/jacoco-it.exec,dumponexit=true,append=true -cp integrationtest.jar");
        } else {
            service = new GenericContainer<>("local/ihe-xds-api-qa:dev")
                    .withFileSystemBind("/tmp", "/jacoco-report/")
                    .withEnv("JVM_OPTS", "-javaagent:/jacoco/jacocoagent.jar=output=file,destfile=/jacoco-report/jacoco-it.exec,dumponexit=true -cp integrationtest.jar");
        }

        service.withNetwork(dockerNetwork)
                .withNetworkAliases("ihe-xds-api")

                .withEnv("LOG_LEVEL", "INFO")
                .withClasspathResourceMapping("certificates/private-cert1.pem", "/certificates/private-cert1.pem", BindMode.READ_ONLY)
                .withClasspathResourceMapping("certificates/public-cert1.cer", "/certificates/public-cert1.cer", BindMode.READ_ONLY)
                .withEnv("STSURL", "http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService")
                .withEnv("xdsIti18Endpoint", "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsregistry")
                .withEnv("default.cert.private", "/certificates/private-cert1.pem")
                .withEnv("default.cert.public", "/certificates/public-cert1.cer")
                .withEnv("redis.host", "redis")
                .withEnv("redis.port", "6379")
                .withEnv("ttl", Integer.toString(ttl))

//                .withEnv("JVM_OPTS", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000")

                .withExposedPorts(8081, 8080)
                .waitingFor(Wait.forHttp("/actuator").forPort(8081).forStatusCode(200));
        service.start();
        attachLogger(serviceLogger, service);



        return service;
    }

    private boolean containerRunning(String containerName) {
        return DockerClientFactory
                .instance()
                .client()
                .listContainersCmd()
                .withNameFilter(Collections.singleton(containerName))
                .exec()
                .size() != 0;
    }

    private void setupDatabaseContainer() {
/*        // Database server for Organisation.
        MariaDBContainer mariadb = (MariaDBContainer) new MariaDBContainer<>("mariadb:10.6")
                .withDatabaseName("hellodb")
                .withUsername("hellouser")
                .withPassword("secret1234")
                .withNetwork(dockerNetwork)
                .withNetworkAliases("mariadb");
        mariadb.start();
        jdbcUrl = mariadb.getJdbcUrl();
        attachLogger(mariadbLogger, mariadb);*/
    }

    private void setupRedisContainer() {

        int REDIS_PORT = 6379;
        GenericContainer<?> redis = new GenericContainer("redis:7.0.4")
                .withExposedPorts(REDIS_PORT)
                .withCommand("redis-server /usr/local/etc/redis/redis.conf")
                .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf", BindMode.READ_ONLY)
                .withNetwork(dockerNetwork)
                .withNetworkAliases("redis");


        redis.start();

        this.mappedRedisPort = redis.getMappedPort(REDIS_PORT);

        attachLogger(redisLogger, redis);

    }


    private void attachLogger(Logger logger, GenericContainer container) {
        ServiceStarter.logger.info("Attaching logger to container: " + container.getContainerInfo().getName());
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
        container.followOutput(logConsumer);
    }
}
