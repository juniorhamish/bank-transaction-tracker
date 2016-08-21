package uk.co.dajohnston.accounts.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class TransactionsParserTest {

    @Test
    public void shouldReadEmptyDateAsNull() throws IOException {
        StringReader input = new StringReader(
                "\"Date\",\"Balance\",\"Paid in\",\"Paid out\",\"Description\",\"Transaction type\"\n" + ",,,,,");
        TransactionsParser transactionsParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionsParser.parse();

        assertThat(transactions.get(0).date(), is(nullValue()));
    }

    @Test
    public void shouldReadEmptyTransactionTypeAsNull() throws IOException {
        StringReader input = new StringReader(
                "\"Transaction type\",\"Date\",\"Balance\",\"Paid in\",\"Paid out\",\"Description\"\n" + ",,,,,");
        TransactionsParser transactionParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionParser.parse();

        assertThat(transactions.get(0).transactionType(), is(nullValue()));
    }

    @Test
    public void shouldReadEmptyDescriptionAsNull() throws IOException {
        StringReader input = new StringReader(
                "\"Transaction type\",\"Date\",\"Balance\",\"Paid in\",\"Paid out\",\"Description\"\n" + ",,,,,");
        TransactionsParser transactionParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionParser.parse();

        assertThat(transactions.get(0).description(), is(nullValue()));
    }

    @Test
    public void shouldReadAllValuesFromInput() throws IOException {
        StringReader input = new StringReader(
                "\"Balance\",\"Date\",\"Paid in\",\"Paid out\",\"Description\",\"Transaction type\"\n"
                        + "\"£99.99\",\"21 Oct 1981\",\"£1.11\",\"£3.56\",\"Some shop somewhere\",\"Direct Debit\"");
        TransactionsParser transactionParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionParser.parse();

        assertThat(transactions.get(0).balance(), is(BigDecimal.valueOf(99.99)));
        assertThat(transactions.get(0).date(), is(LocalDate.of(1981, 10, 21)));
        assertThat(transactions.get(0).paidIn(), is(BigDecimal.valueOf(1.11)));
        assertThat(transactions.get(0).paidOut(), is(BigDecimal.valueOf(3.56)));
        assertThat(transactions.get(0).description(), is("Some shop somewhere"));
        assertThat(transactions.get(0).transactionType(), is("Direct Debit"));
    }

    @Test
    public void shouldReadEachLineOfInputIntoNewTransaction() throws IOException {
        StringReader input = new StringReader(
                "\"Transaction type\",\"Date\",\"Balance\",\"Paid in\",\"Paid out\",\"Description\"\n"
                        + ",,,,,\"Mulberry Street\"\n" + ",,,,,\"Sainsburys\"\n" + ",,,,,\"Tesco\"");
        TransactionsParser transactionParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionParser.parse();

        List<String> descriptions = transactions.stream().map(transaction -> transaction.description())
                .collect(Collectors.toList());
        assertThat(descriptions, contains("Mulberry Street", "Sainsburys", "Tesco"));
    }

    @Test
    public void shouldParseEmptyPaidInAmountAsZero() throws IOException {
        StringReader input = new StringReader(
                "\"Transaction type\",\"Date\",\"Balance\",\"Paid in\",\"Paid out\",\"Description\"\n" + ",,,,,");
        TransactionsParser transactionParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionParser.parse();

        assertThat(transactions.get(0).paidIn(), is(new BigDecimal("0.00")));
    }

    @Test
    public void shouldParseEmptyPaidOutAmountAsZero() throws IOException {
        StringReader input = new StringReader(
                "\"Transaction type\",\"Date\",\"Balance\",\"Paid in\",\"Paid out\",\"Description\"\n" + ",,,,,");
        TransactionsParser transactionParser = new TransactionsParser(input);

        List<Transaction> transactions = transactionParser.parse();

        assertThat(transactions.get(0).paidOut(), is(new BigDecimal("0.00")));
    }

}
