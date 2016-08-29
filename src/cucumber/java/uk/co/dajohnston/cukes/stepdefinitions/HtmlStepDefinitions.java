package uk.co.dajohnston.cukes.stepdefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
    public void verifyCategoryIsShown(String name) throws InterruptedException {
        Thread.sleep(100);
        WebElement categoryList = driver.findElement(By.id("categoryList"));
        List<WebElement> categories = categoryList.findElements(By.className("category"));

        List<String> categoryNames = categories.stream().map(category -> category.getText())
                .collect(Collectors.toList());

        assertThat(categoryNames, contains(name));
    }

    @When("^I filter by category \"([^\"]*)\"$")
    public void filterByCategory(String category) {
        Select categoryFilter = new Select(driver.findElement(By.id("categoryFilter")));

        categoryFilter.selectByVisibleText(category);
    }

    @When("^I reset the category filter$")
    public void resetCategoryFilter() {
        Select categoryFilter = new Select(driver.findElement(By.id("categoryFilter")));

        categoryFilter.selectByIndex(0);
    }

    @When("^I sort by \"([^\"]*)\"$")
    public void sortBy(String column) {
        Optional<WebElement> columnHeader = driver.findElements(By.tagName("th")).stream()
                .filter(header -> header.getText().equals(column)).findFirst();

        columnHeader.get().click();
    }

    @Then("^the first category will be \"([^\"]*)\"$")
    public void verifyFirstCategoryDescription(String description) {
        int descriptionIndex = getDescriptionColumnIndex();

        List<WebElement> firstRowColumns = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).get(0)
                .findElements(By.tagName("td"));

        assertThat(firstRowColumns.get(descriptionIndex).getText(), is(description));
    }

    private int getDescriptionColumnIndex() {
        List<WebElement> headers = driver.findElement(By.tagName("thead")).findElements(By.tagName("th"));
        int descriptionIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().equals("Description")) {
                descriptionIndex = i;
                break;
            }
        }
        return descriptionIndex;
    }

    @When("^I create a category \"([^\"]*)\"$")
    public void createCategory(String name) {
        WebElement categoryTextField = driver.findElement(By.id("categoryName"));
        WebElement createCategoryButton = driver.findElement(By.id("createCategoryButton"));

        categoryTextField.clear();
        categoryTextField.sendKeys(name);
        createCategoryButton.click();
    }

    @Then("^I should see categories$")
    public void verifyCategoryOrder(List<String> categories) {
        List<WebElement> categoryList = driver.findElements(By.className("category"));
        List<String> categoryNames = categoryList.stream()
                .map(category -> category.findElement(By.className("ng-scope")).getText()).collect(Collectors.toList());

        assertThat(categoryNames, contains(categories.toArray()));
    }

    @When("^I delete the category \"([^\"]*)\"$")
    public void deleteCategory(String name) {
        WebElement categoryElement = getCategoryElement(name);

        WebElement deleteButton = categoryElement.findElement(By.tagName("button"));
        deleteButton.click();
    }

    @When("^I add matcher \"([^\"]*)\" to category \"([^\"]*)\"$")
    public void addMatcherToCategory(String matcher, String categoryName) {
        WebElement categoryElement = getCategoryElement(categoryName);
        ensureMatchersDisplayed(categoryElement);

        WebElement matcherInput = categoryElement.findElement(By.id("matcherInput"));
        matcherInput.clear();
        matcherInput.sendKeys(matcher);
        WebElement addMatcherButton = categoryElement.findElement(By.id("addMatcherButton"));
        addMatcherButton.click();
    }

    @Then("^category \"([^\"]*)\" should have matchers$")
    public void verifyMatchersInCategory(String categoryName, List<String> matchers) {
        WebElement categoryElement = getCategoryElement(categoryName);
        ensureMatchersDisplayed(categoryElement);

        List<WebElement> matcherElements = categoryElement.findElements(By.className("matcher"));
        List<String> actualMatchers = matcherElements.stream().map(matcher -> matcher.getText())
                .collect(Collectors.toList());

        assertThat(actualMatchers, contains(matchers.toArray()));
    }

    private void ensureMatchersDisplayed(WebElement categoryElement) {
        WebElement categoryElementHeading = categoryElement.findElement(By.className("panel-heading"));
        if ("false".equals(categoryElementHeading.getAttribute("aria-selected"))) {
            categoryElement.findElement(By.tagName("a")).click();
        }
    }

    private WebElement getCategoryElement(String categoryName) {
        List<WebElement> categoryList = driver.findElements(By.className("category"));
        WebElement categoryElement = categoryList.stream()
                .filter(category -> category.findElement(By.className("ng-scope")).getText().equals(categoryName))
                .findFirst().get();
        return categoryElement;
    }

}
