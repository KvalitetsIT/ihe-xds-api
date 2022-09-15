package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

public class CredentialInfo {

    private CredentialVault credentialVault;

    private String displayName;


    public CredentialInfo(CredentialVault credentialVault, String displayName) {
        this.credentialVault = credentialVault;
        this.displayName = displayName;
    }

    public CredentialVault getCredentialVault() {
        return credentialVault;
    }
    public String getDisplayName() {
        return displayName;
    }
}

