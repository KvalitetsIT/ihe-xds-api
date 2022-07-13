package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

public class CredentialInfo {

    private CredentialVault credentialVault;

    private String cvr;

    private String organisationName;

    public CredentialInfo(CredentialVault credentialVault, String cvr, String organisationName) {
        this.credentialVault = credentialVault;
        this.cvr = cvr;
        this.organisationName = organisationName;
    }

    public CredentialVault getCredentialVault() {
        return credentialVault;
    }

    public String getCvr() {
        return cvr;
    }

    public String getOrganisationName() {
        return organisationName;
    }
}
