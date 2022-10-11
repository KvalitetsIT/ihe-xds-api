package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

public class CredentialInfo {

    private CredentialVault credentialVault;

    private String displayName;

    private String serialNumber;

    private String type;


    // TODO ENUM type eller String




    public CredentialInfo(CredentialVault credentialVault, String displayName) {
        this.credentialVault = credentialVault;
        this.displayName = displayName;
        this.type = setType();
        this.serialNumber = setSerialNumber();

    }
    // TODO muligvis
    private String setSerialNumber() {
       String rawString = credentialVault.getSystemCredentialPair().getCertificate().getSubjectX500Principal().toString();

       if (this.type.equals("SYSTEM")) {
           return rawString.substring(rawString.indexOf('=') + 1, rawString.indexOf('+')).trim();
       } else {
           return rawString.substring(rawString.indexOf('=', rawString.indexOf('+'))+1, rawString.indexOf(','));
       }

    }

    private String setType() {
        String rawString = credentialVault.getSystemCredentialPair().getCertificate().getSubjectX500Principal().toString();
        if (rawString.contains("UID")) {
            return "SYSTEM";
        }
        else {
            return "HEALTHCAREPROFESSIONAL";
        }
    }

    public CredentialVault getCredentialVault() {
        return credentialVault;
    }
    public String getDisplayName() {
        return displayName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getType() {
        return type;
    }
}

