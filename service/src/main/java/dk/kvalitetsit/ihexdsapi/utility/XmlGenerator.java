package dk.kvalitetsit.ihexdsapi.utility;

import dk.s4.hl7.cda.convert.CDAMetadataXmlCodec;
import dk.s4.hl7.cda.model.cdametadata.CDAMetadata;
import org.joda.time.DateTime;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class XmlGenerator {

    private Document xmlDocument = null;
    private Element rootElement = null;


    public XmlGenerator(String xml) throws IOException, ParserConfigurationException, SAXException {
        xmlDocument = createXmlDocument(xml);
    }


    private Document createXmlDocument(String rawXML) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(rawXML));
        Document document = builder.parse(is);
        rootElement = document.getDocumentElement();
        return document;

    }

    public String[] findAttribute(String target) {
        return  null;
    }
/*
        CDAMetadataXmlCodec codec = new CDAMetadataXmlCodec();
        CDAMetadata cdaMetadataDecoded = codec.decode(document);



        for (int i = 1; i < 10; i++) {

                    }


        String[] resultArray = null;
        switch (target) {
            case "id":
                // TODO figure out if this is sufficent or if there can be other assignees

                NamedNodeMap idNodeAtrributes = rootElement.getElementsByTagName(target).item(0).getAttributes();

                if (idNodeAtrributes.item(0).getNodeValue().equals("MedCom")) {
                    resultArray = new String[2];
                    resultArray[0] = idNodeAtrributes.item(1).getNodeValue();
                    resultArray[1] = idNodeAtrributes.item(2).getNodeValue();
                }
                break;
            case "representedOrganization":
                NodeList authorOrganization = rootElement.getElementsByTagName(target).item(0).getChildNodes();
                Node author = rootElement.getElementsByTagName("author").item(0);

                if (author.getChildNodes().getLength() != 0 || rootElement.getElementsByTagName(target).getLength() != 0) {
                    resultArray = new String[3];
                    resultArray[0] = authorOrganization.item(3).getTextContent();
                    resultArray[1] = authorOrganization.item(1).getAttributes().item(1).getNodeValue();
                    resultArray[2] = authorOrganization.item(1).getAttributes().item(2).getNodeValue();

                }
                break;
            case "assignedPerson":
                NodeList authorPerson = rootElement.getElementsByTagName(target).item(0).getChildNodes();


                if (rootElement.getElementsByTagName(target).getLength() != 0) {
                    String firstName = "";
                    String lastName = "";
                    String furtherNames = "";

                    for (int i = 0; i < authorPerson.item(1).getChildNodes().getLength(); i++) {
                        if (authorPerson.item(1).getChildNodes().item(i).toString().contains("given") && i < 6) {
                            firstName = authorPerson.item(1).getChildNodes().item(i).getTextContent();
                        } else if (authorPerson.item(1).getChildNodes().item(i).toString().contains("given")) {
                            furtherNames = furtherNames + authorPerson.item(1).getChildNodes().item(i).getTextContent() + "&";
                        } else if (authorPerson.item(1).getChildNodes().item(i).toString().contains("family")) {
                            lastName = authorPerson.item(1).getChildNodes().item(i).getTextContent();
                        }
                    }
                    resultArray = new String[3];
                    resultArray[0] = firstName;
                    resultArray[1] = lastName;
                    resultArray[2] = furtherNames.substring(0, furtherNames.length() - 1);
                    break;
                }
            case "confidentialityCode":
                NamedNodeMap confidentialityCodeNodeAtrributes = rootElement.getElementsByTagName(target).item(0).getAttributes();
              if (rootElement.getElementsByTagName(target).getLength() != 0) {
                    resultArray = new String[2];
                    resultArray[0] = confidentialityCodeNodeAtrributes.item(0).getNodeValue();
                    resultArray[1] = confidentialityCodeNodeAtrributes.item(1).getNodeValue();
                }
                break;
            case "effectiveTime":
                NamedNodeMap creationTimeCodeNodeAtrributes = rootElement.getElementsByTagName(target).item(0).getAttributes();
                if (rootElement.getElementsByTagName(target).getLength()!= 0) {

                    try {
                        resultArray = new String[1];
                        resultArray[0] = turnStringToUTCDate(creationTimeCodeNodeAtrributes.item(0).getNodeValue());
                    } catch (ParseException e) {
                        //proper error
                        throw new RuntimeException(e);
                    }
                }

                break;
            case "eventCode":
                //NamedNodeMap eventCodeNodeAtrributes = rootElement.getElementsByTagName("serviceEvent").item(0).getChildNodes().
                break;
            case "formatCode":
                break;
        }

        return resultArray;
    }

    private String turnStringToUTCDate(String time) throws ParseException {

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssZ");
        Date dt = sf.parse(time);
        Instant newDt =  dt.toInstant();
        long newDate = (newDt.toEpochMilli());

        Date date = new Date(newDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);

        return formattedDate;
    }*/
}
