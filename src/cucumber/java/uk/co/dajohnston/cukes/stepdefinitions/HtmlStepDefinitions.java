package uk.co.dajohnston.cukes.stepdefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
        driver.findElement(By.id("submitFileButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='TransactionTable']/tbody/tr")));
    }

    @Then("^I should see (\\d+) transactions$")
    public void verifyAllTransactionsAreOnThePage(int expectedTransactionCount) {
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='TransactionTable']/tbody/tr"));
        int tableRowCount = tableRows.size();

        assertThat(tableRowCount, is(expectedTransactionCount));
    }

    @Then("^the date filter should be \"([^\"]*)\" to \"([^\"]*)\"$")
    public void checkDateFilter(String startDate, String endDate) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='TransactionTable']/tbody/tr")));

        WebElement startDateField = driver.findElement(By.id("dateFilterStart"));
        WebElement endDateField = driver.findElement(By.id("dateFilterEnd"));

        assertThat(startDateField.getAttribute("value"), is(startDate));
        assertThat(endDateField.getAttribute("value"), is(endDate));
    }

    @When("^I filter by date range \"([^\"]*)\" to \"([^\"]*)\"$")
    public void setDateFilters(String startDate, String endDate) {
        WebElement startDateField = driver.findElement(By.id("dateFilterStart"));
        WebElement endDateField = driver.findElement(By.id("dateFilterEnd"));

        startDateField.clear();
        startDateField.sendKeys(startDate);
        endDateField.clear();
        endDateField.sendKeys(endDate);
    }

    @Then("^I should see category \"([^\"]*)\"$")
    public void verifyCategoryIsShown(String name) {
        WebElement categoryList = driver.findElement(By.id("categoryList"));
        List<WebElement> categories = categoryList.findElements(By.className("category"));

        List<String> categoryNames = categories.stream().map(category -> category.getText())
                .collect(Collectors.toList());

        assertThat(categoryNames, contains(name));
    }

}
