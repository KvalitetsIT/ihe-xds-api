package dk.kvalitetsit.ihexdsapi.configuration;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsService;
import dk.kvalitetsit.ihexdsapi.dgws.impl.CredentialServiceImpl;
import dk.kvalitetsit.ihexdsapi.dgws.impl.StsServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.DgwsServiceImpl;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dk.kvalitetsit.ihexdsapi.dgws.StsService;
import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.service.impl.IheXdsServiceImpl;
import dk.kvalitetsit.ihexdsapi.service.impl.Iti18ServiceImpl;

@Configuration
public class IheXdsConfiguration {

    private String STSURL = "http://test1.ekstern-test.nspop.dk:8080/sts/services/NewSecurityTokenService";

    private static Logger LOGGER = LoggerFactory.getLogger(IheXdsConfiguration.class);

    private String xdsIti18Endpoint = "http://test1-cnsp.ekstern-test.nspop.dk:8080/ddsregistry";


    @PostConstruct
    public void setupDefaultCredentials() {

    }

    @Bean
    public IheXdsService helloService() {
        return new IheXdsServiceImpl();
    }

    @Bean
    public StsService stsService() {
        return new StsServiceImpl(STSURL);
    }

    @Bean
    public CredentialService credentialService() {
        CredentialServiceImpl credentialService = new CredentialServiceImpl();
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
