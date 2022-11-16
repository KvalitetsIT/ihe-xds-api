package dk.kvalitetsit.ihexdsapi.service.model;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;

public class ProvideAndRegisterDocumentSetRequest {
    private String externalDocumentId;

    private ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequestType;

    public ProvideAndRegisterDocumentSetRequest(String extenalDocumentId, ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequestType) {
        this.externalDocumentId = extenalDocumentId;
        this.provideAndRegisterDocumentSetRequestType = provideAndRegisterDocumentSetRequestType;
    }

    public String getExternalDocumentId() {
        return externalDocumentId;
    }

    public void setExternalDocumentId(String externalDocumentId) {
        this.externalDocumentId = externalDocumentId;
    }

    public ProvideAndRegisterDocumentSetRequestType getProvideAndRegisterDocumentSetRequestType() {
        return provideAndRegisterDocumentSetRequestType;
    }

    public void setProvideAndRegisterDocumentSetRequestType(
            ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequestType) {
        this.provideAndRegisterDocumentSetRequestType = provideAndRegisterDocumentSetRequestType;
    }
}
