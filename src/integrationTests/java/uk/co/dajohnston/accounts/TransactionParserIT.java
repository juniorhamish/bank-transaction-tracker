package uk.co.dajohnston.accounts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.joda.money.Money;
import org.junit.Test;

public class TransactionParserIT {

    @Test
    public void shouldReadAllRecordsFromFile() throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("SampleTransactions.csv");
                Reader input = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            List<Transaction> transactions = new TransactionsParser(input).parse();

            assertThat(transactions.size(), is(134));
        }
    }

    @Test
    public void shouldCorrectlyParseNationwideOutputFormat() throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("SampleTransactions.csv");
                Reader input = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            List<Transaction> transactions = new TransactionsParser(input).parse();

            assertThat(transactions.get(0).date(), is(LocalDate.of(2016, 7, 6)));
            assertThat(transactions.get(0).transactionType(), is("Standing order"));
            assertThat(transactions.get(0).description(), is("GRC"));
            assertThat(transactions.get(0).paidIn(), is(Money.parse("GBP 0.00")));
            assertThat(transactions.get(0).paidOut(), is(Money.parse("GBP 7.00")));
            assertThat(transactions.get(0).balance(), is(Money.parse("GBP 21588.06")));
        }
    }

}
