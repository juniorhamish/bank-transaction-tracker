package uk.co.dajohnston.cukes.stepdefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.context.embedded.LocalServerPort;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HtmlStepDefinitions {

    @LocalServerPort
    public int port;

    private WebDriver driver;

    @Before(value = "@Web")
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @After(value = "@Web")
    public void tearDown() {
        driver.close();
    }

    @When("^I view the homepage$")
    public void viewHomepage() {
        driver.get("http://localhost:" + port);
    }

    @Then("^the title should be \"([^\"]*)\"$")
    public void verifyPageTitle(String expectedTitle) {
        assertThat(driver.getTitle(), is(expectedTitle));
    }

    @When("^I upload transactions file \"([^\"]*)\"$")
    public void uploadTransactionsFile(String fileName) {
        WebElement uploadButton = driver.findElement(By.id("transactionFile"));
        uploadButton.sendKeys(fileName);
    }

    @Then("^I should see (\\d+) transactions$")
    public void verifyAllTransactionsAreOnThePage(int expectedTransactionCount) {
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='TransactionTable']/tbody/tr"));
        int tableRowCount = tableRows.size();

        assertThat(tableRowCount, is(expectedTransactionCount));
    }

}
