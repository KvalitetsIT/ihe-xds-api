package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.service.Iti18Service;
import dk.kvalitetsit.ihexdsapi.xds.Codes;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.openapitools.model.Iti18QueryParameter;
import org.openapitools.model.Iti18Response;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLQueryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;

import java.util.LinkedList;
import java.util.List;

public class Iti18ServiceImpl implements Iti18Service {

	private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

	private Iti18PortType iti18PortType;

	private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();
	
	public Iti18ServiceImpl(Iti18PortType iti18PortType) {
		this.iti18PortType = iti18PortType;

		Client proxy = ClientProxy.getClient(iti18PortType);
		proxy.getOutInterceptors().add(dgwsSoapDecorator);
	}

	private List<Iti18Response> createResponse(String patientId, AdhocQueryResponse response) {

		QueryResponseTransformer queryResponseTransformer = new QueryResponseTransformer(getEbXmlFactory());
		EbXMLQueryResponse30 ebXmlresponse = new EbXMLQueryResponse30(response);
		QueryResponse queryResponse = queryResponseTransformer.fromEbXML(ebXmlresponse);

		List<Iti18Response> result = new LinkedList<>();
		for (DocumentEntry documentEntry : queryResponse.getDocumentEntries()) {
			Iti18Response iti18Response = new Iti18Response();
			iti18Response.setPatientId(patientId);
			iti18Response.setDocumentId(documentEntry.getUniqueId());
			result.add(iti18Response);
		}
		return result;
	}

	private EbXMLFactory getEbXmlFactory() {
		return ebXMLFactory;
	}

	private AdhocQueryRequest createQuery(Iti18QueryParameter iti18Request) {

		FindDocumentsQuery fdq = new FindDocumentsQuery();

		// Patient ID
		AssigningAuthority authority = new AssigningAuthority(Codes.DK_CPR_CLASSIFICAION_OID);
		Identifiable patientIdentifiable = new Identifiable(iti18Request.getPatientId(), authority);
		fdq.setPatientId(patientIdentifiable);

		QueryRegistry queryRegistry = new QueryRegistry(fdq);
		queryRegistry.setReturnType(QueryReturnType.LEAF_CLASS);

		QueryRegistryTransformer queryRegistryTransformer = new QueryRegistryTransformer();
		EbXMLAdhocQueryRequest ebxmlAdhocQueryRequest = queryRegistryTransformer.toEbXML(queryRegistry);
		AdhocQueryRequest internal = (AdhocQueryRequest)ebxmlAdhocQueryRequest.getInternal();
		return internal;
	}

	@Override
	public List<Iti18Response> queryForDocument(Iti18QueryParameter iti18Request, DgwsClientInfo dgwsClientInfo) throws DgwsSecurityException {
		try {
			dgwsSoapDecorator.setDgwsClientInfo(dgwsClientInfo);

			var query = createQuery(iti18Request);
			var response = iti18PortType.documentRegistryRegistryStoredQuery(query);
			return createResponse(iti18Request.getPatientId(), response);

		} finally {
			dgwsSoapDecorator.clearSDgwsClientInfo();;
		}
	}
}
