package uk.co.dajohnston.cukes;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "uk.co.dajohnston.cukes.stepdefinitions", features = "classpath:features/")
public class CucumberTests {

}
