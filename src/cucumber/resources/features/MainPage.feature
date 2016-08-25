@Web
Feature: show main page

  Scenario: navigate to main page
    When I view the homepage
    Then the title should be "Accounts"

  Scenario: the main page should have an option to upload a transactions file
    Given I view the homepage
     When I upload transactions file "/Users/djohnston/dev/katas/accounts/src/cucumber/resources/NationwideSample.csv"
     Then I should see 134 transactions

  Scenario: the date filter fields should default to the earliest and latest transactions in the uploaded file
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in   | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |           | £12345.78 |
      | 29 Jul 2016 | Bank Credit      | Salary      |          | £23456.67 | £78964.34 |
     When I view the homepage
     Then the date filter should be "29 Jul 2016" to "01 Aug 2016"

  Scenario: filter by date range
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit     | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | Direct Debit     | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit     | Alpha       | £1.12    |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit     | Beta        | £4.56    |         | £12.34    |
      And I view the homepage
     When I filter by date range "23 Jul 2016" to "05 Aug 2016"
     Then I should see 2 transactions

  Scenario: add a category
    Given I add category "Eating Out"
     When I view the homepage
     Then I should see category "Eating Out"

  Scenario: filter by category
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit     | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | Direct Debit     | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit     | Sky TV      | £71.12   |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit     | Beta        | £4.56    |         | £12.34    |
      And I add category "Bills"
      And I set matchers for category "Bills" to:
      | Mortgage |
      | Sky TV   |
      And I view the homepage
     When I filter by category "Bills"
     Then I should see 2 transactions

  Scenario: reset category filter
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit     | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | Direct Debit     | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit     | Sky TV      | £71.12   |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit     | Beta        | £4.56    |         | £12.34    |
      And I add category "Bills"
      And I set matchers for category "Bills" to:
      | Mortgage |
      | Sky TV   |
      And I view the homepage
      And I filter by category "Bills"
     When I reset the category filter
     Then I should see 5 transactions

  Scenario: filter matches nothing
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit     | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | Direct Debit     | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit     | Sky TV      | £71.12   |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit     | Beta        | £4.56    |         | £12.34    |
      And I add category "Bills"
      And I set matchers for category "Bills" to:
      | Mortgage |
      | Sky TV   |
      And I add category "Abc"
      And I set matchers for category "Abc" to:
      | A |
      | B |
      And I view the homepage
     When I filter by category "Abc"
     Then I should see 0 transactions

  Scenario: sort by description
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit     | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | Direct Debit     | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit     | Sky TV      | £71.12   |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit     | Beta        | £4.56    |         | £12.34    |
      And I view the homepage
     When I sort by "Description"
     Then the first category will be "Bar"

  Scenario: sort by date in reverse
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit     | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | Direct Debit     | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit     | Sky TV      | £71.12   |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit     | Beta        | £4.56    |         | £12.34    |
      And I view the homepage
      And I sort by "Description"
      And I sort by "Date"
     When I sort by "Date"
     Then the first category will be "Sky TV"

  Scenario: sort by transaction type
    Given I upload a file containing the following transactions
      | Date        | Transaction type    | Description | Paid out | Paid in | Balance   |
      | 01 Aug 2016 | Direct Debit        | Mortgage    | £345.67  |         | £12345.78 |
      | 23 Jul 2016 | Direct Debit        | Foo         | £123.45  |         | £78964.34 |
      | 01 Jul 2016 | ATM Withdrawal LINK | Bar         | £454.56  |         | £34234.23 |
      | 23 Aug 2016 | Direct Debit        | Sky TV      | £71.12   |         | £53453.23 |
      | 01 Jan 1970 | Direct Debit        | Beta        | £4.56    |         | £12.34    |
      And I view the homepage
     When I sort by "Transaction Type"
     Then the first category will be "Bar"

  Scenario: sort by paid in
    Given I upload a file containing the following transactions
      | Date        | Transaction type    | Description | Paid out | Paid in   | Balance   |
      | 01 Aug 2016 | Direct Debit        | Mortgage    | £345.67  |           | £12345.78 |
      | 23 Jul 2016 | Direct Debit        | Foo         | £123.45  |           | £78964.34 |
      | 01 Jul 2016 | ATM Withdrawal LINK | Bar         | £454.56  |           | £34234.23 |
      | 23 Aug 2016 | Direct Debit        | Sky TV      | £71.12   |           | £53453.23 |
      | 01 Jan 1970 | Credit              | Wages       |          | £12345.67 | £12.34    |
      And I view the homepage
      And I sort by "Paid In"
     When I sort by "Paid In"
     Then the first category will be "Wages"

  Scenario: sort by paid out
    Given I upload a file containing the following transactions
      | Date        | Transaction type    | Description | Paid out | Paid in   | Balance   |
      | 01 Aug 2016 | Direct Debit        | Mortgage    | £345.67  |           | £12345.78 |
      | 23 Jul 2016 | Direct Debit        | Foo         | £123.45  |           | £78964.34 |
      | 01 Jul 2016 | ATM Withdrawal LINK | Bar         | £454.56  |           | £34234.23 |
      | 23 Aug 2016 | Direct Debit        | Sky TV      | £71.12   |           | £53453.23 |
      | 01 Jan 1970 | Credit              | Wages       |          | £12345.67 | £12.34    |
      And I view the homepage
      And I sort by "Paid Out"
     When I sort by "Paid Out"
     Then the first category will be "Bar"

  Scenario: sort by balance
    Given I upload a file containing the following transactions
      | Date        | Transaction type    | Description | Paid out | Paid in   | Balance   |
      | 01 Aug 2016 | Direct Debit        | Mortgage    | £345.67  |           | £12345.78 |
      | 23 Jul 2016 | Direct Debit        | Foo         | £123.45  |           | £78964.34 |
      | 01 Jul 2016 | ATM Withdrawal LINK | Bar         | £454.56  |           | £34234.23 |
      | 23 Aug 2016 | Direct Debit        | Sky TV      | £71.12   |           | £53453.23 |
      | 01 Jan 1970 | Credit              | Wages       |          | £12345.67 | £12.34    |
      And I view the homepage
     When I sort by "Balance"
     Then the first category will be "Wages"

  Scenario: create new category
    Given I view the homepage
     When I create a category "Credit Cards"
     Then I should see category "Credit Cards"

  Scenario: sort categories alphabetically
    Given I view the homepage
      And I create a category "Foo"
     When I create a category "Bar"
     Then I should see categories
      | Bar |
      | Foo |
