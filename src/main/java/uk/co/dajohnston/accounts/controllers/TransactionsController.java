package uk.co.dajohnston.accounts.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uk.co.dajohnston.accounts.Transaction;
import uk.co.dajohnston.accounts.TransactionsParser;

@RestController
public class TransactionsController {

    private List<Transaction> transactions;

    @RequestMapping(method = RequestMethod.POST, path = "/transactionFile")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Transaction> uploadTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        transactions = new TransactionsParser(new InputStreamReader(file.getInputStream())).parse();
        Collections.sort(transactions);

        return transactions;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/transactions")
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
