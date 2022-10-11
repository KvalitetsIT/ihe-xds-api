package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.service.Iti41Service;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;

public class Iti41ServiceImpl implements Iti41Service {

    private Iti41PortType iti41PortType;
    private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();
    private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();
    public Iti41ServiceImpl(Iti41PortType iti41PortType) {
        this.iti41PortType = iti41PortType;

        Client proxy = ClientProxy.getClient(iti41PortType);
        proxy.getOutInterceptors().add(dgwsSoapDecorator);
    }
}
