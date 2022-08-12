package dk.kvalitetsit.ihexds.integrationtest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;

@SpringBootApplication
public class TestApplication extends SpringBootServletInitializer {
    public static void main(String[] args) throws IOException {
        new ServiceStarter().startServices();
    }
}
