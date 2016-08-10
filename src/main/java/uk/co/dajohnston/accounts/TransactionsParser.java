package uk.co.dajohnston.accounts;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

public class TransactionsParser {

    private Reader input;

    public TransactionsParser(Reader input) {
        this.input = input;
    }

    public List<Transaction> parse() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        CSVFormat parser = CSVFormat.RFC4180.withFirstRecordAsHeader();
        parser.parse(input).forEach(record -> transactions.add(new Transaction(record)));

        return transactions;
    }

}
