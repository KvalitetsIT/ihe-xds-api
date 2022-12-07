package dk.kvalitetsit.ihexdsapi.service;

import dk.kvalitetsit.ihexdsapi.dgws.DgwsClientInfo;
import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import org.jdom2.JDOMException;
import org.openapitools.model.GeneratedMetaData;
import org.openapitools.model.Iti41UploadRequest;
import org.openapitools.model.Iti41UploadResponse;
import org.openapitools.model.ResponseMetaData;

import java.io.IOException;

public interface Iti41Service {


    ResponseMetaData setMetaData(Iti41UploadRequest iti41UploadRequest) throws IOException, JDOMException;

    Iti41UploadResponse doUpload(Iti41UploadRequest iti41UploadRequest, DgwsClientInfo dgwsClientInfo) throws ItiException;

    GeneratedMetaData getGeneratedMetaData(String xml) throws IOException, JDOMException;
}
