package dk.kvalitetsit.ihexdsapi.configuration;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class InitConfiguration {
    @Autowired
    private CredentialService credentialService;


    @PostConstruct
    public void setupDefaultCredentials() throws URISyntaxException, IOException, DgwsSecurityException {
         String key1 = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/private-cert1.pem").toURI()));
         String cert1 = Files.readString(Paths.get(getClass().getClassLoader().getResource("certificates/public-cert1.cer").toURI()));

         // Need better variable names
        credentialService.createAndAddCredentialInfo("  ", "Standard1", " ", " ", cert1, key1
                );

    }
}
