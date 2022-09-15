package dk.kvalitetsit.ihexdsapi.configuration;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.kvalitetsit.ihexdsapi.dgws.impl.AbstractTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@Import(IheXdsConfiguration.class)
@EnableAutoConfiguration
@PropertySource("application.properties")
public class TestConfiguration extends AbstractTest {

   @Autowired
    CredentialService credentialService;

    @PostConstruct
    public void setupDefaultCredentials() throws DgwsSecurityException {

        String publicCertStr = getFileString("/certificates/public-cert13.cer");
        String privateKeyStr = getFileString("/certificates/private-cert1.pem");
        String displayName =  "My certificate";

        CredentialInfo credentialInfo = credentialService.createAndAddCredentialInfo(null, displayName, publicCertStr, privateKeyStr);
    }
}
