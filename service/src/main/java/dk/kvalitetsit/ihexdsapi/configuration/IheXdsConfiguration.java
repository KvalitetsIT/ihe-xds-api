package dk.kvalitetsit.ihexdsapi.configuration;

import javax.xml.namespace.QName;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dgws.impl.CredentialServiceImpl;
import dk.kvalitetsit.ihexdsapi.dgws.impl.StsServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.CodesExecption;
import dk.kvalitetsit.ihexdsapi.service.impl.CodesServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.DgwsServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.CodesService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dk.kvalitetsit.ihexdsapi.dgws.StsService;
import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.impl.IheXdsServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.Iti18ServiceImpl;

@Configuration
public class IheXdsConfiguration {


    @Value("${STSURL}")
    private String STSURL;

    private static Logger LOGGER = LoggerFactory.getLogger(IheXdsConfiguration.class);
    @Value("${xdsIti18Endpoint}")
    private String xdsIti18Endpoint;


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
/*
    @Value("${availabilitystatus.codes}")
    private String availabilityStatusCodes;

    @Value("${availabilitystatus.names}")
    private String availabilityStatusNames;
*/
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

    @Bean
    public Iti18PortType getDocumentRegistryServiceIti18() {
        LOGGER.info("Creating Iti18PortType for url: " + xdsIti18Endpoint);

        XdsClientFactory xdsClientFactory = generateXdsRegistryClientFactory("wsdl/iti18.wsdl", xdsIti18Endpoint, Iti18PortType.class);
        Iti18PortType client = (Iti18PortType) xdsClientFactory.getClient();
        Client proxy = ClientProxy.getClient(client);
        proxy.getOutInterceptors().add(new LoggingOutInterceptor());
        proxy.getInInterceptors().add(new LoggingInInterceptor());
        return client;
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


    private void initProxy(Object o, boolean dgwsEnabled, boolean addRequestInterceptor) {
        Client proxy = ClientProxy.getClient(o);
		
		
/*		if (dgwsEnabled) {
			HsuidSoapDecorator hsuidSoapDecorator = appContext.getBean(HsuidSoapDecorator.class);
			proxy.getOutInterceptors().add(hsuidSoapDecorator);
		}*/
		
		
	/*	if (addRequestInterceptor) {
			InResponseInterceptor in = appContext.getBean(InResponseInterceptor.class);
			proxy.getInInterceptors().add(in);
			
			OutRequestInterceptor out = appContext.getBean(OutRequestInterceptor.class);
			proxy.getOutInterceptors().add(out);
		}*/
		
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
}
