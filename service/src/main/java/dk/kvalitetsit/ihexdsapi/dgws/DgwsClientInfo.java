package dk.kvalitetsit.ihexdsapi.dgws;

import org.w3c.dom.Document;

public class DgwsClientInfo {

    private Document sosi;
    private String patientId;
    private boolean consentOverride = false;
    private String actingUserId;
    private String responsibleUserId;
    private String authorizationCode;
    private String organisationCode;


    private String cpr;

    public DgwsClientInfo(Document sosi, String cpr, String patientId, String authorizationCode, String organisationCode, boolean consentOverride) {
        this.sosi = sosi;
        this.cpr = cpr;
        this.patientId = patientId;
        this.authorizationCode = authorizationCode;
        this.organisationCode = organisationCode;
        this.consentOverride = consentOverride;

    }

    public Document getSosi() {
        return sosi;
    }

    public void setSosi(Document sosi) {
        this.sosi = sosi;
    }

    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public boolean getConsentOverride() {
        return consentOverride;
    }
    public void setConsentOverride(boolean consentOverride) {
        this.consentOverride = consentOverride;
    }
/*
    public String getActingUserId() {
        return actingUserId;
    }

    public void setActingUserId(String actingUserId) {
        this.actingUserId = actingUserId;
    }

    public String getResponsibleUserId() {
        return responsibleUserId;
    }
    public void setResponsibleUserId(String responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getOrganisationCode() {
        return organisationCode;
    }

    public void setOrganisationCode(String organisationCode) {
        this.organisationCode = organisationCode;
    }

*/
    public String getCpr() {
        return cpr;
    }

    public String getAuthorizationCode() {
        return  this.authorizationCode;
    }

    public String getOrganisationCode() {
        return  this.organisationCode;
    }
}

