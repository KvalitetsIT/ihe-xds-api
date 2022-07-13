package dk.kvalitetsit.ihexdsapi.dgws;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DgwsSoapDecorator extends AbstractSoapInterceptor {

	private ThreadLocal<DgwsClientInfo> dgwsClientInfo = new ThreadLocal<>();
	
	public DgwsSoapDecorator() {
		super(Phase.PRE_STREAM);
	}

	public void setDgwsClientInfo(DgwsClientInfo dci) {
		dgwsClientInfo.set(dci);
	}
	
	public void clearSDgwsClientInfo() {
		dgwsClientInfo.remove();
	}
	
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		// DGWS is SOAP11
		message.setVersion(Soap11.getInstance());

		// Add the DGWS headers
		NodeList children = dgwsClientInfo.get().getSosi().getDocumentElement().getFirstChild().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node element = children.item(i);
			QName qname = new QName(element.getNamespaceURI(), element.getLocalName());
			Header dgwsHeader = new Header(qname, element);
			message.getHeaders().add(dgwsHeader);
		}			
	}
}
