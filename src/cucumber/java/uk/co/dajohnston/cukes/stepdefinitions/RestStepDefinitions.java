package uk.co.dajohnston.cukes.stepdefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import uk.co.dajohnston.accounts.Transaction;

public class RestStepDefinitions {

    @LocalServerPort
    public int port;

    protected RestTemplate restTemplate = new RestTemplate();

    private ResponseEntity<Void> result;

    private List<Transaction> transactions;

    @When("^I upload \"([^\"]*)\"$")
    public void uploadTransactionFile(String fileName) throws URISyntaxException {
        URL transactionFileURL = getClass().getClassLoader().getResource(fileName);
        File transactionFile = new File(transactionFileURL.toURI());
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(transactionFile));
        result = restTemplate.exchange("http://localhost:" + port + "/transactionFile", HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(parts), Void.class);
    }

    @Then("^I should get a success response$")
    public void verifySuccessResponse() {
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
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
}
