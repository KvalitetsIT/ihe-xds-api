package dk.kvalitetsit.ihexdsapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dk.kvalitetsit.ihexdsapi.service.IheXdsService;
import dk.kvalitetsit.ihexdsapi.service.IheXdsServiceImpl;

@Configuration
public class IheXdsConfiguration{
    @Bean
    public IheXdsService helloService() {
        return new IheXdsServiceImpl();
    }
}
