package dk.kvalitetsit.ihexdsapi.service.impl;

import java.io.IOException;
import java.io.StringWriter;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSoapDecorator;
import dk.kvalitetsit.ihexdsapi.dgws.Iti43Exception;
import dk.kvalitetsit.ihexdsapi.service.Iti43Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.openapitools.model.Iti43QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRetrieveDocumentSetResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.RetrieveDocumentSetResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43PortType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Iti43ServiceImpl implements Iti43Service {

    private Iti43PortType iti43PortType;
    private DgwsSoapDecorator dgwsSoapDecorator = new DgwsSoapDecorator();
    private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

    public Iti43ServiceImpl(Iti43PortType iti43PortType) {
        this.iti43PortType = iti43PortType;

        Client proxy = ClientProxy.getClient(iti43PortType);
        proxy.getOutInterceptors().add(dgwsSoapDecorator);
    }

    @Override
    public String getDocument(Iti43QueryParameter queryParameter, DgwsClientInfo clientInfo) throws Iti43Exception {
        try {
            dgwsSoapDecorator.setDgwsClientInfo(clientInfo);
            // TODO FIX null/index out of bounds
            RetrievedDocumentSet documentResponse = fetchDocument(queryParameter.getDocumentId(), queryParameter.getRepositoryId());
            System.out.println(documentResponse.getErrors());
            System.out.println("HELLO");
            //String xml = documentResponse.getDocuments().get(0).getDataHandler();
            String xml = formatXML(documentResponse.getDocuments().get(0).getDataHandler());
            return xml;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Iti43Exception(e, 1000, "Failed to retrieve document");
        } finally {
            dgwsSoapDecorator.clearSDgwsClientInfo();
        }
    }


    private RetrievedDocumentSet fetchDocument(String documentId, String repositoryId) {
        RetrieveDocumentSetResponseType repositoryResponse = iti43PortType.documentRepositoryRetrieveDocumentSet(buildRetrieveDocumentSetRequestType(documentId, repositoryId, null));

        RetrieveDocumentSetResponseTransformer retrieveDocumentSetResponseTransformer = new RetrieveDocumentSetResponseTransformer(getEbXmlFactory());
        EbXMLRetrieveDocumentSetResponse30 ebXmlResponse = new EbXMLRetrieveDocumentSetResponse30(repositoryResponse);
        RetrievedDocumentSet rds = retrieveDocumentSetResponseTransformer.fromEbXML(ebXmlResponse);
        return rds;
    }

    private RetrieveDocumentSetRequestType buildRetrieveDocumentSetRequestType(String documentId, String repositoryId, String homecommunityid) {
        RetrieveDocumentSetRequestType retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
        RetrieveDocumentSetRequestType.DocumentRequest documentRequest = new RetrieveDocumentSetRequestType.DocumentRequest();
        documentRequest.setRepositoryUniqueId(repositoryId);
        documentRequest.setDocumentUniqueId(documentId);

        // redundant code
        if (homecommunityid != null) {
            documentRequest.setHomeCommunityId(homecommunityid);
        }
        retrieveDocumentSetRequestType.getDocumentRequest().add(documentRequest);
        return retrieveDocumentSetRequestType;
    }

    // TODO kig på at få inputsource til string
    private String formatXML(DataHandler dataHandler) throws TransformerException, SAXException, IOException, ParserConfigurationException, XPathExpressionException {
        Document xmlDocument = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(dataHandler.getInputStream()));

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", xmlDocument, XPathConstants.NODESET);

        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            node.getParentNode().removeChild(node);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        transformer.transform(new DOMSource(xmlDocument), streamResult);
        return stringWriter.toString();

    }

    private EbXMLFactory getEbXmlFactory() {
        return ebXMLFactory;
    }

}
