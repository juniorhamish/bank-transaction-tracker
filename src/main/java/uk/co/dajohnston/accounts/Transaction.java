package uk.co.dajohnston.accounts;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVRecord;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import lombok.EqualsAndHashCode;
import lombok.ToString;
@ToString
@EqualsAndHashCode
public class Transaction {

    public LocalDate date;
    public String transactionType;
    public String description;
    public Money paidOut;
    public Money paidIn;
    public Money balance;

    public Transaction() {
        // Empty for JSON
    }

    public Transaction(CSVRecord record) {
        String dateString = record.get("Date");
        if (!dateString.isEmpty()) {
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }
        transactionType = nullifyString(record.get("Transaction type"));
        description = nullifyString(record.get("Description"));
        paidOut = parseMoney(record.get("Paid out"));
        paidIn = parseMoney(record.get("Paid in"));
        balance = parseMoney(record.get("Balance"));
    }

    public LocalDate date() {
        return date;
    }

    public String transactionType() {
        return transactionType;
    }

    public String description() {
        return description;
    }

    public Money paidOut() {
        return paidOut;
    }

    public Money paidIn() {
        return paidIn;
    }

    public Money balance() {
        return balance;
    }

    private Money parseMoney(String value) {
        if (value.isEmpty()) {
            return Money.zero(CurrencyUnit.GBP);
        }
        value = value.replace("£", "GBP ");
        return Money.parse(value);
    }

    private String nullifyString(String value) {
        return value.isEmpty() ? null : value;
    }
}
