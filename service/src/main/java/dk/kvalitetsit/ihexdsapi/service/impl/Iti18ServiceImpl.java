package dk.kvalitetsit.ihexdsapi.service.impl;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.openapitools.model.Iti18Request;
import org.openapitools.model.Iti18Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.dgws.StsService;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;

public class Iti18ServiceImpl implements Iti18Service {

	private Iti18PortType iti18PortType;
	
	private StsService stsService;
	
	private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();
	
	public Iti18ServiceImpl(StsService stsService, Iti18PortType iti18PortType) {
		this.iti18PortType = iti18PortType;
		this.stsService = stsService;
		
		Client proxy = ClientProxy.getClient(iti18PortType);	
		proxy.getOutInterceptors().add(dgwsSoapDecorator);
	}

	@Override
	public Iti18Response queryForDocument(Iti18Request iti18Request) {
		
		try {
			DgwsClientInfo dgwsClientInfo = stsService.getDgwsClientInfo();
			dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo);
			
			var response = iti18PortType.documentRegistryRegistryStoredQuery(createQuery(iti18Request));
			return createResponse(response);
			
		} finally {
			dgwsSoapDecorator.clearSDgwsClientInfo();;
		}
	}

	private Iti18Response createResponse(AdhocQueryResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	private AdhocQueryRequest createQuery(Iti18Request iti18Request) {
		// TODO Auto-generated method stub
		return null;
	}

}
