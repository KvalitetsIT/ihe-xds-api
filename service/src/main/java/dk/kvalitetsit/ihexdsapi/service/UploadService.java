package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import org.jdom2.JDOMException;
import org.openapitools.model.GeneratedMetaData;
import org.openapitools.model.Iti41UploadResponse;
import org.openapitools.model.ResponseMetaData;

import java.io.IOException;

public interface UploadService {
    ResponseMetaData getMetaData(String xml);
    Iti41UploadResponse uploadRequest(String xmlPayload, DgwsClientInfo dgwsClientInfo);

    GeneratedMetaData getGeneratedMetaData(String xml) throws IOException, JDOMException, ItiException;
}
