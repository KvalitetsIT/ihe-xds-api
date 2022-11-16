package dk.kvalitetsit.ihexdsapi.dgws;

import dk.sosi.seal.vault.CredentialVault;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialInfo {

    private CredentialVault credentialVault;

    private String displayName;

    private String serialNumber;

    private String type;

    private String organisationName;

    private String cvr;


    private final String SYSTEM = "SYSTEM";
    private final String HEALTHCAREPROFESSIONAL = "HEALTHCAREPROFESSIONAL";




    public CredentialInfo(CredentialVault credentialVault, String displayName) {
        this.credentialVault = credentialVault;
        this.displayName = displayName;
        this.type = setType();
        this.serialNumber = setSerialNumber();

    }
    // TODO muligvis anden måde at tjekke
    private String setSerialNumber() {
       String rawString = credentialVault.getSystemCredentialPair().getCertificate().getSubjectX500Principal().toString();

       if (this.type.equals(SYSTEM)) {
           return rawString.substring(rawString.indexOf('=') + 1, rawString.indexOf('+')).trim();
       } else {
           return rawString.substring(rawString.indexOf('=', rawString.indexOf('+'))+1, rawString.indexOf(','));
       }

    }

    private void setOrg(String org) {
        this.organisationName = org;
    }

    private void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getCvr() {
        return cvr;
    }

    private String setType() {
        String rawString = credentialVault.getSystemCredentialPair().getCertificate().getSubjectX500Principal().toString();
        if (rawString.contains("UID")) {
            setCVRAndOrg(rawString);

            return SYSTEM;
        }
        else {
            return HEALTHCAREPROFESSIONAL;
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

    private void setCVRAndOrg(String raw) {

        System.out.println(raw);

        Pattern pattern = Pattern.compile("O=.* /");

        Matcher m = pattern.matcher(raw);

        String result = "";
        while (m.find()) {
            result = m.group(0);
        }

        setOrg(result.substring(2, result.length()-2));

        pattern = Pattern.compile(" CVR:.*,");

        m = pattern.matcher(raw);
        while (m.find()) {
            result = m.group(0);
        }

        setCvr(result.trim().substring(4, result.length()-2));


    }
}

