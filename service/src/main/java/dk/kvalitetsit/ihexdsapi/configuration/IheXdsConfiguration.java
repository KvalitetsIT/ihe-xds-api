package dk.kvalitetsit.ihexdsapi.configuration;


import javax.xml.namespace.QName;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dgws.impl.CredentialServiceImpl;
import dk.kvalitetsit.ihexdsapi.dgws.impl.StsServiceImpl;
import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dao.impl.CacheRequestResponseHandleImpl;
import dk.kvalitetsit.ihexdsapi.interceptors.InResponseInterceptor;
import dk.kvalitetsit.ihexdsapi.interceptors.OutResponseInterceptor;
import dk.kvalitetsit.ihexdsapi.service.*;
import dk.kvalitetsit.ihexdsapi.service.impl.*;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import dk.kvalitetsit.ihexdsapi.dgws.StsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@Import(RedisConfiguration.class)
public class IheXdsConfiguration {


    @Value("${STSURL}")
    private String STSURL;

    private static Logger LOGGER = LoggerFactory.getLogger(IheXdsConfiguration.class);
    @Value("${xdsIti18Endpoint}")
    private String xdsIti18Endpoint;

    // ONE ITI41
    @Value("${xdsIti41Endpoint}")
    private String xdsIti41Endpoint;

    @Value("${xdsIti43Endpoint}")
    private String xdsIti43Endpoint;


// Codes

    @Value("${type.code.scheme}")
    private String typeCodeScheme;

    @Value("${format.code.scheme}")
    private String formatCodeScheme;

    @Value("${event.code.scheme}")
    private String eventCodeScheme;

    @Value("${event.code.scheme.codes}")
    private String eventCodeSchemeCodes;

    @Value("${event.code.scheme.names}")
    private String eventCodeSchemeNames;

    @Value("${healthcarefacilitytype.code.scheme}")
    private String healthcareFacilityTypeCodeScheme;

    @Value("${practicesetting.code.scheme}")
    private String practicesettingCodeScheme;

    @Value("${class.code.scheme}")
    private String classCodeScheme;


    // Dropdown lists

    @Value("${format.code.codes}")
    private String formatCodeCodes;

    @Value("${format.code.names}")
    private String formatCodeNames;

    @Value("${class.code.codes}")
    private String classCodeCodes;

    @Value("${class.code.names}")
    private String classCodeNames;

    @Value("${healthcarefacilitytype.code.codes}")
    private String healthcareFacilityTypeCodeCodes;

    @Value("${healthcarefacilitytype.code.names}")
    private String healthcareFacilityTypeCodeNames;

    @Value("${practicesetting.code.codes}")
    private String practiceSettingCodeCodes;

    @Value("${practicesetting.code.names}")
    private String practiceSettingCodeNames;

    @Value("${object.type.codes}")
    private String objectTypeCodes;

    @Value("${object.type.names}")
    private String objectTypeNames;
    @Value("${type.code.codes}")
    private String typeCodeCodes;

    @Value("${type.code.names}")
    private String typeCodeNames;


   
    @Bean
    public IheXdsService helloService() {
        return new IheXdsServiceImpl();
    }

    @Bean
    public StsService stsService() {
        return new StsServiceImpl(STSURL);
    }

    @Bean
    public CredentialService credentialService(CredentialRepository credentialRepository) {
        CredentialServiceImpl credentialService = new CredentialServiceImpl(credentialRepository);
        return credentialService;
    }

    @Bean
    public DgwsService dgwsService(CredentialService credentialService, StsService stsService) {
        DgwsServiceImpl dgwsService = new DgwsServiceImpl(stsService, credentialService);
        return dgwsService;
    }


    @Bean
    public Iti18Service iti18Service(Iti18PortType iti18PortType) {
        Iti18ServiceImpl iti18ServiceImpl = new Iti18ServiceImpl(iti18PortType);
        return iti18ServiceImpl;
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    @RequestScope
    public IDContextService idContextService() {
        IDContextService idContextService = new IDContextServiceImpl();
        return idContextService;
    }

    @Bean
    CacheRequestResponseHandle cacheRequestResponseHandle(RedisTemplate<String, Object> redisTemplate) {
        CacheRequestResponseHandleImpl cacheRequestResponseHandleimpl = new CacheRequestResponseHandleImpl(redisTemplate);
        return cacheRequestResponseHandleimpl;
    }


    @Bean
    public Iti18PortType getDocumentRegistryServiceIti18(OutResponseInterceptor outResponseInterceptor, InResponseInterceptor inResponseInterceptor) {
        LOGGER.info("Creating Iti18PortType for url: " + xdsIti18Endpoint);

        XdsClientFactory xdsClientFactory = generateXdsRegistryClientFactory("wsdl/iti18.wsdl", xdsIti18Endpoint, Iti18PortType.class);
        Iti18PortType client = (Iti18PortType) xdsClientFactory.getClient();
        Client proxy = ClientProxy.getClient(client);
        proxy.getOutInterceptors().add(outResponseInterceptor);
        proxy.getInInterceptors().add(inResponseInterceptor);
        return client;
    }

    @Bean
    public Iti41RepositoriesService iti41RepositoriesService () {
        return new Iti41RepositoriesServiceImpl(xdsIti41Endpoint);
    }

    @Bean
   // @Scope(value = "prototype")
    public Iti41PortType getDocumentRepositoryServiceIti41() {
        LOGGER.info("Creating Iti41PortType for url: "+xdsIti41Endpoint);
        XdsClientFactory xdsClientFactory = generateXdsRepositoryClientFactory("wsdl/iti41.wsdl", xdsIti41Endpoint, Iti41PortType.class);
        Iti41PortType client = (Iti41PortType) xdsClientFactory.getClient();
       Client proxy = ClientProxy.getClient(client);
        proxy.getOutInterceptors().add(new LoggingOutInterceptor());
        proxy.getInInterceptors().add(new LoggingInInterceptor());

        return client;
    }

    @Bean
    public UploadService uploadService(CodesService codesService) {
        UploadServiceImpl uploadService = new UploadServiceImpl((CodesServiceImpl) codesService);
        return uploadService;
    }
    @Bean
    public Iti41Service iti41Service(Iti41PortType getDocumentRepositoryServiceIti41, UploadService uploadService) {
        Iti41ServiceImpl iti41ServiceImpl = new Iti41ServiceImpl(getDocumentRepositoryServiceIti41, uploadService);
        return iti41ServiceImpl;
    }


    @Bean
    public Iti43PortType getDocumentRepositoryServiceIti43() {
        LOGGER.info("Creating Iti43PortType for url: "+xdsIti43Endpoint);

        XdsClientFactory xdsClientFactory = generateXdsRepositoryClientFactory("wsdl/iti43.wsdl", xdsIti43Endpoint, Iti43PortType.class);
        Iti43PortType client = (Iti43PortType) xdsClientFactory.getClient();

        return client;
    }

    @Bean
    public Iti43Service iti43Service(Iti43PortType iti43PortType) {
        Iti43ServiceImpl iti43ServiceImpl = new Iti43ServiceImpl(iti43PortType);
        return iti43ServiceImpl;
    }

    @Bean
    public OutResponseInterceptor outResponseInterceptor (CacheRequestResponseHandle cacheRequestResponseHandle, IDContextService iDContextService) {
        return new OutResponseInterceptor(cacheRequestResponseHandle, iDContextService);
    }

    @Bean
    public InResponseInterceptor inResponseInterceptor (CacheRequestResponseHandle cacheRequestResponseHandle, IDContextService iDContextService) {
        return new InResponseInterceptor(cacheRequestResponseHandle, iDContextService);
    }



    @Bean
    public CodesService codesService() throws CodesExecption {
        CodesServiceImpl codeService = new CodesServiceImpl( typeCodeCodes,  typeCodeNames,  typeCodeScheme,
                 formatCodeCodes,  formatCodeNames,  formatCodeScheme,
                eventCodeSchemeCodes,  eventCodeSchemeNames,  eventCodeScheme
                ,  healthcareFacilityTypeCodeCodes,  healthcareFacilityTypeCodeNames,  healthcareFacilityTypeCodeScheme,
                 practiceSettingCodeCodes,  practiceSettingCodeNames,  practicesettingCodeScheme
                ,  classCodeCodes,  classCodeNames,  classCodeScheme,
                 objectTypeCodes,  objectTypeNames);
        return codeService;
    }
    @Bean
    public ConfigsService configsService() {

        ConfigsServiceImpl configsService = new ConfigsServiceImpl(STSURL, xdsIti18Endpoint,xdsIti43Endpoint, xdsIti41Endpoint );
        return configsService;

    }


    private void initProxy(Object o, boolean dgwsEnabled, boolean addRequestInterceptor) {
        /*Client proxy = ClientProxy.getClient(o);

        System.out.println("Hello");
        System.out.println(proxy.getContexts());

		/*
		if (dgwsEnabled) {
			HsuidSoapDecorator hsuidSoapDecorator = appContext.getBean(HsuidSoapDecorator.class);
			proxy.getOutInterceptors().add(hsuidSoapDecorator);
		}*/
		
		/*
		if (addRequestInterceptor) {
			InResponseInterceptor in = appContext.getBean(InResponseInterceptor.class);
			proxy.getInInterceptors().add(in);
			
			OutRequestInterceptor out = appContext.getBean(OutRequestInterceptor.class);
			proxy.getOutInterceptors().add(out);
		}
		
/*		HTTPConduit conduit = (HTTPConduit)proxy.getConduit();
		TLSClientParameters tcp = new TLSClientParameters();
		tcp.setTrustManagers(trustAllCerts);
		tcp.setHostnameVerifier(new HostnameVerifier() {			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		conduit.setTlsClientParameters(tcp);*/
    }


    private XdsClientFactory generateXdsRegistryClientFactory(String wsdl, String url, Class<?> clazz) {
        return generateXdsRegistryClientFactory("urn:ihe:iti:xds-b:2007", wsdl, url, clazz);
    }

    private XdsClientFactory generateXdsRegistryClientFactory(String namespace, String wsdl, String url, Class<?> clazz) {
        final WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
                new QName(namespace, "DocumentRegistry_Service",
                        "ihe"), clazz, new QName(
                namespace,
                "DocumentRegistry_Binding_Soap12", "ihe"), false,
                wsdl, true, false, false, false);

        return new XdsClientFactory(WS_CONFIG, url, null, null, null);
    }

    private XdsClientFactory generateXdsRepositoryClientFactory(String wsdl, String url, Class<?> clazz){
        final WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
                new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service",
                        "ihe"), clazz, new QName(
                "urn:ihe:iti:xds-b:2007",
                "DocumentRepository_Binding_Soap12", "ihe"), true,
                wsdl, true, false, false, false);


        XdsClientFactory xcf = new XdsClientFactory(WS_CONFIG, url, null, null,null);
        return xcf;
    }
}
