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
     Then the date filter should be "2016-07-29" to "2016-08-01"
