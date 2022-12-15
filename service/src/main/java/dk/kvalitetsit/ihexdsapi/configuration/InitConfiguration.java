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

    @Value("${default.cert.private}")
    private String keyPath;
    @Value("${default.cert.public}")
    private String publicPath;

    @Value("${default.cert.private.two}")
    private String keyPath2;
    @Value("${default.cert.public.two}")
    private String publicPath2;

    @Value("${default.cert.private.three}")
    private String keyPath3;
    @Value("${default.cert.public.three}")
    private String publicPath3;


    @PostConstruct
    public void setupDefaultCredentials() throws URISyntaxException, IOException, DgwsSecurityException {
        String key1 = Files.readString(Paths.get(this.keyPath));
        String cert1 = Files.readString(Paths.get(this.publicPath));
        String key2 = Files.readString(Paths.get(this.keyPath2));
        String cert2 = Files.readString(Paths.get(this.publicPath2));
        String key3 = Files.readString(Paths.get(this.keyPath3));
        String cert3 = Files.readString(Paths.get(this.publicPath3));

        /*if (credentialService.getIds(" ").size() == 0) {
            System.out.println(credentialService.getIds(" "));*/
        // Need better variable names
        try {
            credentialService.createAndAddCredentialInfo(null, "Sonja Bech", cert1, key1
            );
            credentialService.createAndAddCredentialInfo(null, "Grethe Pedersen", cert2, key2
            );

            credentialService.createAndAddCredentialInfo(null, "Default VOCES", cert3, key3
            );
        }
        catch (DgwsSecurityException e) {
            System.out.println(e);
        }

    }
   /* }
       /* System.out.println(credentialService.getIds(" "));}*/
}
