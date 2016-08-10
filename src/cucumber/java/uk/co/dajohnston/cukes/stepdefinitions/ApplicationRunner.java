package uk.co.dajohnston.cukes.stepdefinitions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.java.Before;
import uk.co.dajohnston.accounts.Application;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationRunner {

    @Before
    public static void setup() {
        // Do nothing, just to make the annotations start the server.
    }
}
