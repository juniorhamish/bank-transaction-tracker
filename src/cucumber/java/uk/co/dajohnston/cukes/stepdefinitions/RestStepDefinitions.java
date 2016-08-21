package uk.co.dajohnston.cukes.stepdefinitions;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import uk.co.dajohnston.accounts.model.Category;
import uk.co.dajohnston.accounts.model.Transaction;

public class RestStepDefinitions {

    @LocalServerPort
    public int port;

    private ResponseEntity<?> result;

    private List<Transaction> transactions;
    private List<Category> categories;
    private Category category;

    private RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // Do nothing
            }
        });
        deleteAllCategories();
    }

    @When("^I upload \"([^\"]*)\"$")
    public void uploadTransactionFile(String fileName) throws URISyntaxException {
        URL transactionFileURL = getClass().getClassLoader().getResource(fileName);
        File transactionFile = new File(transactionFileURL.toURI());
        uploadFile(transactionFile);
    }

    private void uploadFile(File transactionFile) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(transactionFile));
        ResponseEntity<List<Transaction>> response = restTemplate.exchange(
                "http://localhost:" + port + "/transactionFile", HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(parts),
                new ParameterizedTypeReference<List<Transaction>>() {
                });
        transactions = response.getBody();
        result = response;
    }

    @Then("^I should get a (\\d+) response code$")
    public void verifyResponseCode(int expectedResponseCode) {
        assertThat(result.getStatusCode().value(), is(expectedResponseCode));
    }

    @When("^I get all transactions$")
    public void getAllTransactions() {
        transactions = restTemplate.exchange("http://localhost:" + port + "/transactions", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Transaction>>() {
                }).getBody();
    }

    @Then("^there should be (\\d+) transactions$")
    public void there_should_be_transactions(int expectedTransactionCount) {
        assertThat(transactions.size(), is(expectedTransactionCount));
    }

    @Given("^I upload a file containing the following transactions$")
    public void uploadTransactions(DataTable transactions) throws IOException {
        List<List<String>> transactionData = transactions.raw();
        File tempTransactionFile = File.createTempFile("Transactions", ".csv");
        try (Writer output = new OutputStreamWriter(new FileOutputStream(tempTransactionFile))) {
            for (List<String> row : transactionData) {
                output.write(StringUtils.join(row, ','));
                output.write(System.lineSeparator());
            }
        }
        uploadFile(tempTransactionFile);
    }

    @Then("^I should have a transaction from \"([^\"]*)\" of type \"([^\"]*)\" with description \"([^\"]*)\", paid out \"([^\"]*)\", paid in \"([^\"]*)\" and a balance of \"([^\"]*)\"$")
    public void verifyTransactionsFromResponse(String date, String transactionType, String description, String paidOut,
            String paidIn, String balance) {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy"));
        expectedTransaction.transactionType = transactionType;
        expectedTransaction.description = description;
        expectedTransaction.paidOut = new BigDecimal(paidOut.replace("£", ""));
        expectedTransaction.paidIn = new BigDecimal(paidIn.replace("£", ""));
        expectedTransaction.balance = new BigDecimal(balance.replace("£", ""));

        assertThat(transactions, hasItem(expectedTransaction));
    }

    @Given("^I add category \"([^\"]*)\"$")
    public void addCategory(String categoryName) {
        result = restTemplate.postForEntity("http://localhost:" + port + "/categories", new Category(categoryName),
                Category.class);
    }

    @When("^I get all categories$")
    public void getAllCategories() {
        categories = restTemplate.exchange("http://localhost:" + port + "/categories", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Category>>() {
                }).getBody();
    }

    @Then("^I should have category \"([^\"]*)\"$")
    public void verifyCategoryExists(String categoryName) {
        List<String> allNames = categories.stream().map(category -> category.getName()).collect(Collectors.toList());
        assertThat(allNames, contains(categoryName));
    }

    @When("^I get category \"([^\"]*)\"$")
    public void getCategoryByName(String name) {
        ResponseEntity<Category> response = restTemplate
                .getForEntity("http://localhost:" + port + "/categories/" + name, Category.class);
        category = response.getBody();
        result = response;
    }

    @Then("^the response should contain a category with name \"([^\"]*)\"$")
    public void verifyCategoryName(String expectedName) {
        if (category != null) {
            assertThat(category.getName(), is(expectedName));
        } else {
            verifyCategoryExists(expectedName);
        }
    }

    @When("^I delete all categories$")
    public void deleteAllCategories() {
        restTemplate.delete("http://localhost:" + port + "/categories");
    }

    @Then("^there should be no categories$")
    public void verifyNoCategories() {
        getAllCategories();

        assertThat(categories.size(), is(0));
    }

    @When("^I delete category \"([^\"]*)\"$")
    public void deleteCategory(String name) {
        categories = restTemplate.exchange("http://localhost:" + port + "/categories/" + name, HttpMethod.DELETE, null,
                new ParameterizedTypeReference<List<Category>>() {
                }).getBody();
    }

    @When("^I set matchers for category \"([^\"]*)\" to:$")
    public void setMatchersForCategory(String name, List<String> matchers) {
        restTemplate.put("http://localhost:" + port + "/categories/" + name, matchers);
    }

    @Then("^the category should have matchers:$")
    public void verifyCategoryHasMatchers(List<String> expectedMatchers) {
        assertThat(category.getMatchers(), containsInAnyOrder(expectedMatchers.toArray()));
    }
}
