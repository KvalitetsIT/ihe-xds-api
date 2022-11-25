package dk.kvalitetsit.ihexdsapi.dgws;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dk.nsi.hsuid._2016._08.hsuid_1_1.HsuidHeader;
import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import dk.nsi.hsuid.ActingUserCivilRegistrationNumberAttribute;
import dk.nsi.hsuid.Attribute;
import dk.nsi.hsuid.CitizenCivilRegistrationNumberAttribute;
import dk.nsi.hsuid.ConsentOverrideAttribute;
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderUtil;
import dk.nsi.hsuid.OperationsOrganisationNameAttribute;
import dk.nsi.hsuid.OrganisationIdentifierAttribute;
import dk.nsi.hsuid.ResponsibleUserAuthorizationCodeAttribute;
import dk.nsi.hsuid.ResponsibleUserCivilRegistrationNumberAttribute;
import dk.nsi.hsuid.SystemNameAttribute;
import dk.nsi.hsuid.SystemVendorNameAttribute;
import dk.nsi.hsuid.SystemVersionAttribute;
import dk.nsi.hsuid.UserTypeAttribute;
import dk.nsi.hsuid._2016._08.hsuid_1_1.SubjectIdentifierType;

public class DgwsSoapDecorator extends AbstractSoapInterceptor {

    private static final String SYSTEM_OWNER = "Test";
    private static final String SYSTEM_NAME = "Test";
    private static final String SYSTEM_VERSION = "1.0";
    private static final String OPERATIONS_ORGANISATION_NAME = "TestOrg";
    private static final String ISSUER = "Issuer";

    private boolean enableHSUID;
    private ThreadLocal<DgwsClientInfo> dgwsClientInfo = new ThreadLocal<>();

    public DgwsSoapDecorator() {
        super(Phase.PRE_STREAM);
    }

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public void setDgwsClientInfo(DgwsClientInfo dci, boolean enableHSUID) {
        dgwsClientInfo.set(dci);
        this.enableHSUID = enableHSUID;
    }

    public void clearSDgwsClientInfo() {
        dgwsClientInfo.remove();
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        // DGWS is SOAP11
        message.setVersion(Soap11.getInstance());

        DgwsClientInfo clientInfo = dgwsClientInfo.get();

        // Add the DGWS headers
        NodeList children = clientInfo.getSosi().getDocumentElement().getFirstChild().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node element = children.item(i);
            QName qname = new QName(element.getNamespaceURI(), element.getLocalName());
            Header dgwsHeader = new Header(qname, element);
            message.getHeaders().add(dgwsHeader);
        }

        // Add HSUID for type18 and 43

        if (enableHSUID) {
        try {
            message.getHeaders().add(getHsuid(clientInfo));
        } catch (ParserConfigurationException | JAXBException e) {
            throw new RuntimeException(e);
        }}

    }

    private Header getHsuid(DgwsClientInfo clientInfo) throws JAXBException, ParserConfigurationException {


        Boolean vaerdispring = clientInfo.getConsentOverride();

        HsuidHeader hsuidHeader = getHsuidHeaderForHealthCareProfessionalWithAuthorization(clientInfo.getPatientId(), clientInfo.getCpr(), clientInfo.getCpr(), clientInfo.getAuthorizationCode(), clientInfo.getOrganisationCode(), vaerdispring);

        Document doc = dbf.newDocumentBuilder().newDocument();
        JAXBContext jaxbContext = JAXBContext.newInstance(HsuidHeader.class);
        jaxbContext.createMarshaller().marshal(hsuidHeader, doc);
        Node hsUidElement = doc.getDocumentElement().getFirstChild();
        QName hsUidQName = new QName(hsUidElement.getNamespaceURI(), hsUidElement.getLocalName());
        Header hsUidHeader = new Header(hsUidQName, doc.getFirstChild());
        return hsUidHeader;
    }

    public HsuidHeader getHsuidHeaderForHealthCareProfessionalWithAuthorization(String patientId, String actingUserCpr, String responsibleUserCpr, String authorizationCode, String sorCode, boolean consentOverride) {
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new CitizenCivilRegistrationNumberAttribute(patientId));
        attributes.add(new UserTypeAttribute(UserTypeAttribute.LegalValues.HEALTHCAREPROFESSIONAL.getValue()));
        attributes.add(new ActingUserCivilRegistrationNumberAttribute(actingUserCpr));
        attributes.add(new OrganisationIdentifierAttribute(sorCode, SubjectIdentifierType.NSI_SORCODE.toString()));
        attributes.add(new SystemVendorNameAttribute(SYSTEM_OWNER));
        attributes.add(new SystemNameAttribute(SYSTEM_NAME));
        attributes.add(new SystemVersionAttribute(SYSTEM_VERSION));
        attributes.add(new OperationsOrganisationNameAttribute(OPERATIONS_ORGANISATION_NAME));
        if (consentOverride) {
            attributes.add(new ConsentOverrideAttribute(true));
        }
        attributes.add(new ResponsibleUserCivilRegistrationNumberAttribute(responsibleUserCpr != null ? responsibleUserCpr : actingUserCpr));
        if (authorizationCode != null) {
            // TODO Grethe ..
            attributes.add(new ResponsibleUserAuthorizationCodeAttribute(authorizationCode));
        }

        try {
            return HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(ISSUER, attributes);

        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
