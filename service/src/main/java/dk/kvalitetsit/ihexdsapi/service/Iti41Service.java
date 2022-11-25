package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import org.jdom2.JDOMException;
import org.openapitools.model.GeneratedMetaData;
import org.openapitools.model.Iti41UploadResponse;
import org.openapitools.model.ResponseMetaData;

import java.io.IOException;

public interface Iti41Service {


    ResponseMetaData setMetaData(String xml);

    Iti41UploadResponse doUpload(String xmlPayload, DgwsClientInfo dgwsClientInfo);

    GeneratedMetaData getGeneratedMetaData(String xml) throws IOException, JDOMException;
}
