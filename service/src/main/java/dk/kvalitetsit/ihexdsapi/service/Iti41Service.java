package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import org.openapitools.model.GeneratedMetaData;
import org.openapitools.model.Iti41UploadRequest;
import org.openapitools.model.Iti41UploadResponse;
import org.openapitools.model.ResponseMetaData;

public interface Iti41Service {

    ResponseMetaData getMetaData(String xml);
    Iti41UploadResponse uploadRequest(String xmlPayload, DgwsClientInfo dgwsClientInfo);
}
