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
