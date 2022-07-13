package dk.kvalitetsit.ihexdsapi.service;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import dk.kvalitetsit.ihexdsapi.configuration.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("test.properties")
@ContextConfiguration(
        classes = { TestConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
abstract public class AbstractTest {

}

