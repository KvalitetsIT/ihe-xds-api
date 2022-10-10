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

   // 3000 3 seconds
    private static final int ttl = 86400000;

    private Network dockerNetwork;
    private String jdbcUrl;

    private int mappedRedisPort;

    public void startServices() throws IOException {
        dockerNetwork = Network.newNetwork();


        setupDatabaseContainer();
        setupRedisContainer();

        System.setProperty("STSURL", "http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService");
        System.setProperty("xdsIti18Endpoint", "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsregistry");
        System.setProperty("xdsIti43Endpoint", "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsrepository");

        // Gets correct test paths
        var privateKey = new ClassPathResource("/certificates/private-cert1.pem");
        var publicKey = new ClassPathResource("/certificates/public-cert1.cer");
        var privateKey2 = new ClassPathResource("/certificates/private-cert2.pem");
        var publicKey2 = new ClassPathResource("/certificates/public-cert2.cer");

        var privateKey3 = new ClassPathResource("/certificates/voces1.pem");
        var publicKey3 = new ClassPathResource("/certificates/voces1.cer");

        System.setProperty("default.cert.private", privateKey.getFile().getAbsolutePath());
        System.setProperty("default.cert.public", publicKey.getFile().getAbsolutePath());
        System.setProperty("default.cert.private.two", privateKey2.getFile().getAbsolutePath());
        System.setProperty("default.cert.public.two", publicKey2.getFile().getAbsolutePath());
        System.setProperty("default.cert.private.three", privateKey3.getFile().getAbsolutePath());
        System.setProperty("default.cert.public.three", publicKey3.getFile().getAbsolutePath());
        System.setProperty("redis.host", "localhost");
        System.setProperty("redis.port", Integer.toString(mappedRedisPort));

        System.setProperty("redis.data.ttl", Integer.toString(ttl));

        // Codes

        System.setProperty("type.code.codes", "53576-5;74468-0;74465-6;11502-2;56446-8;");
        System.setProperty("type.code.names", "Personal health monitoring report Document;Questionnaire Form Definition Document;Questionnaire Response Document;LABORATORY REPORT.TOTAL;Appointment Summary Document;");
        System.setProperty("type.code.scheme", "2.16.840.1.113883.6.1");

        System.setProperty("format.code.codes", "urn:ad:dk:medcom:phmr:full;urn:ad:dk:medcom:qfdd:full;urn:ad:dk:medcom:qrd:full;urn:ad:dk:medcom:appointmentsummary:full;urn:ad:dk:medcom:labreports:svareksponeringsservice;");
        System.setProperty("format.code.names", "DK PHMR schema;DK QFDD schema;DK QRD schema;DK Appointment Summary Document schema;Laboratoriesvar (samling af svar);");
        System.setProperty("format.code.scheme", "1.2.208.184.100.10");

        System.setProperty("event.code.scheme.codes", "1.2.208.176.2.1;1.2.208.176.2.4;");
        System.setProperty("event.code.scheme.names", "NPU;Episode of care label;");
        System.setProperty("event.code.scheme", "2.16.840.1.113883.6.1");

        System.setProperty("healthcarefacilitytype.code.codes", "N/A;264372000;20078004;554221000005108;554031000005103;546821000005103;547011000005103;546811000005109;550621000005101;22232009;550631000005103;550641000005106;550651000005108;394761003;550661000005105;42665001;554211000005102;550711000005101;550671000005100;554061000005105;264361005;554041000005106;554021000005101;550681000005102;550691000005104;550701000005104;554231000005106;554051000005108;");
        System.setProperty("healthcarefacilitytype.code.names", "N/A;apotek;behandlingscenter for stofmisbrugere;bosted;diætistklinik;ergoterapiklinik;fysioterapiklinik;genoptræningscenter (snomed: rehabiliteringscenter);hjemmesygepleje;hospital;jordemoderklinik;kiropraktor klinik;lægelaboratorium;lægepraksis (snomed:  almen lægepraksis);lægevagt;plejehjem;præhospitals enhed (snomed:  præhospitalsenhed);psykologisk rådgivningsklinik;speciallægepraksis;statsautoriseret fodterapeut (snomed:  fodterapeutklinik);sundhedscenter;sundhedsforvaltning;sundhedspleje;tandlægepraksis;tandpleje klinik;tandteknisk klinik;vaccinationsklinik;zoneterapiklinik;");
        System.setProperty("healthcarefacilitytype.code.scheme", "2.16.840.1.113883.6.96");

        System.setProperty("practicesetting.code.codes", "N/A;408443003;394577000;394821009;394588006;394582007;394914008;394583002;394811001;394585009;408472002;394803006;394807007;419192003;394579002;408463005;394609007;551411000005104;394596001;394600006;394601005;394580004;421661004;408454008;394809005;394592004;418112009;394805004;394584008;394589003;394610002;394591006;394812008;394594003;394801008;394604002;394915009;394611003;394587001;394537008;394810000;554011000005107;394581000;394605001;394603008;408448007;394612005;");
        System.setProperty("practicesetting.code.names", "N/A;almen medicin;anæstesiologi;arbejdsmedicin;børne- og ungdomspsykiatri;dermato-venerologi;diagnostisk radiologi;endokrinologi;geriatri;gynækologi og obstetrik;hepatologi;hæmatologi;infektionsmedicin;intern medicin;kardiologi;karkirurgi;kirurgi;kirurgisk gastroenterologi;klinisk biokemi;klinisk farmakologi;klinisk fysiologi og nuklearmedicin (snomed:  klinisk fysiologi);klinisk genetik;klinisk immunologi;klinisk mikrobiologi;klinisk neurofysiologi;klinisk onkologi;lungesygdomme;medicinsk allergologi;medicinsk gastroenterologi;nefrologi;neurokirurgi;neurologi;odontologi (snomed: odontologiske specialer);oftalmologi;ortopædisk kirurgi;oto-rhino-laryngologi;patologisk anatomi og cytologi;plastikkirurgi;psykiatri;pædiatri;reumatologi;Retsmedicin;samfundsmedicin;tand-, mund- og kæbekirurgi;thoraxkirurgi;tropemedicin;urologi;");
        System.setProperty("practicesetting.code.scheme", "2.16.840.1.113883.6.96");

        System.setProperty("class.code.codes", "001");
        System.setProperty("class.code.names", "Clinical report;");
        System.setProperty("class.code.scheme", "1.2.208.184.100.9");

        System.setProperty("object.type.codes", "STABLE");
        System.setProperty("object.type.names", "Stable");


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
                .withEnv("xdsIti43Endpoint", "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsrepository")
                .withEnv("default.cert.private", "/certificates/private-cert1.pem")
                .withEnv("default.cert.public", "/certificates/public-cert1.cer")
                .withEnv("redis.host", "redis")
                .withEnv("redis.port", "6379")
                .withEnv("redis.data.ttl", Integer.toString(ttl))

                //Codes
                .withEnv("type.code.codes", "53576-5;74468-0;74465-6;11502-2;56446-8;")
                .withEnv("type.code.names", "Personal health monitoring report Document;Questionnaire Form Definition Document;Questionnaire Response Document;LABORATORY REPORT.TOTAL;Appointment Summary Document;")
                .withEnv("type.code.scheme", "2.16.840.1.113883.6.1")

                .withEnv("format.code.codes", "urn:ad:dk:medcom:phmr:full;urn:ad:dk:medcom:qfdd:full;urn:ad:dk:medcom:qrd:full;urn:ad:dk:medcom:appointmentsummary:full;urn:ad:dk:medcom:labreports:svareksponeringsservice;")
                .withEnv("format.code.names", "DK PHMR schema;DK QFDD schema;DK QRD schema;DK Appointment Summary Document schema;Laboratoriesvar (samling af svar);")
                .withEnv("format.code.scheme", "1.2.208.184.100.10")


                .withEnv("event.code.scheme.codes", "1.2.208.176.2.1;1.2.208.176.2.4;")
                .withEnv("event.code.scheme.names", "NPU;Episode of care label;")
                .withEnv("event.code.scheme", "2.16.840.1.113883.6.1")

                .withEnv("healthcarefacilitytype.code.codes", "N/A;264372000;20078004;554221000005108;554031000005103;546821000005103;547011000005103;546811000005109;550621000005101;22232009;550631000005103;550641000005106;550651000005108;394761003;550661000005105;42665001;554211000005102;550711000005101;550671000005100;554061000005105;264361005;554041000005106;554021000005101;550681000005102;550691000005104;550701000005104;554231000005106;554051000005108;")
                .withEnv("healthcarefacilitytype.code.names", "N/A;apotek;behandlingscenter for stofmisbrugere;bosted;diætistklinik;ergoterapiklinik;fysioterapiklinik;genoptræningscenter (snomed: rehabiliteringscenter);hjemmesygepleje;hospital;jordemoderklinik;kiropraktor klinik;lægelaboratorium;lægepraksis (snomed:  almen lægepraksis);lægevagt;plejehjem;præhospitals enhed (snomed:  præhospitalsenhed);psykologisk rådgivningsklinik;speciallægepraksis;statsautoriseret fodterapeut (snomed:  fodterapeutklinik);sundhedscenter;sundhedsforvaltning;sundhedspleje;tandlægepraksis;tandpleje klinik;tandteknisk klinik;vaccinationsklinik;zoneterapiklinik;")
                .withEnv("healthcarefacilitytype.code.scheme", "2.16.840.1.113883.6.96")
// Fix values
                .withEnv("practicesetting.code.codes", "N/A;408443003;394577000;394821009;394588006;394582007;394914008;394583002;394811001;394585009;408472002;394803006;394807007;419192003;394579002;408463005;394609007;551411000005104;394596001;394600006;394601005;394580004;421661004;408454008;394809005;394592004;418112009;394805004;394584008;394589003;394610002;394591006;394812008;394594003;394801008;394604002;394915009;394611003;394587001;394537008;394810000;554011000005107;394581000;394605001;394603008;408448007;394612005;")
                .withEnv("practicesetting.code.names", "N/A;almen medicin;anæstesiologi;arbejdsmedicin;børne- og ungdomspsykiatri;dermato-venerologi;diagnostisk radiologi;endokrinologi;geriatri;gynækologi og obstetrik;hepatologi;hæmatologi;infektionsmedicin;intern medicin;kardiologi;karkirurgi;kirurgi;kirurgisk gastroenterologi;klinisk biokemi;klinisk farmakologi;klinisk fysiologi og nuklearmedicin (snomed:  klinisk fysiologi);klinisk genetik;klinisk immunologi;klinisk mikrobiologi;klinisk neurofysiologi;klinisk onkologi;lungesygdomme;medicinsk allergologi;medicinsk gastroenterologi;nefrologi;neurokirurgi;neurologi;odontologi (snomed: odontologiske specialer);oftalmologi;ortopædisk kirurgi;oto-rhino-laryngologi;patologisk anatomi og cytologi;plastikkirurgi;psykiatri;pædiatri;reumatologi;Retsmedicin;samfundsmedicin;tand-, mund- og kæbekirurgi;thoraxkirurgi;tropemedicin;urologi;")
                .withEnv("practicesetting.code.scheme", "2.16.840.1.113883.6.96")

                .withEnv("class.code.codes", "001")
                .withEnv("class.code.names", "Clinical report;")
                .withEnv("class.code.scheme", "1.2.208.184.100.9")

                .withEnv("object.type.codes", "STABLE")
                .withEnv("object.type.names", "Stable")


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
