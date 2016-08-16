package uk.co.dajohnston.accounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVRecord;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Transaction implements Comparable<Transaction> {

    public LocalDate date;
    public String transactionType;
    public String description;
    public BigDecimal paidOut;
    public BigDecimal paidIn;
    public BigDecimal balance;

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

    public BigDecimal paidOut() {
        return paidOut;
    }

    public BigDecimal paidIn() {
        return paidIn;
    }

    public BigDecimal balance() {
        return balance;
    }

    private BigDecimal parseMoney(String value) {
        if (value.isEmpty()) {
            return new BigDecimal("0.00");
        }
        value = value.replace("£", "");
        return new BigDecimal(value);
    }

    private String nullifyString(String value) {
        return value.isEmpty() ? null : value;
    }

    @Override
    public int compareTo(Transaction other) {
        return this.date.compareTo(other.date);
    }
}
