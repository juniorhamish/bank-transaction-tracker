@API
Feature: Rest API for dealing with transactions

  Scenario: Upload transaction file
    When I upload "NationwideSample.csv"
    Then I should get a success response

  Scenario: uploaded transactions should be available
    Given I upload "NationwideSample.csv"
     When I get all transactions
     Then there should be 134 transactions

  Scenario: uploading transactions should return data in response
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in   | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |           | £12345.78 |
      | 29 Jul 2016 | Bank Credit      | Salary      |          | £23456.67 | £78964.34 |
     Then I should have a transaction from "01 Aug 2016" of type "Direct Debit" with description "Mortgage", paid out "£345.67", paid in "£0.00" and a balance of "£12345.78"
      And I should have a transaction from "29 Jul 2016" of type "Bank Credit" with description "Salary", paid out "£0.00", paid in "£23456.67" and a balance of "£78964.34"

  Scenario: getting transactions should include all of the transaction information
    Given I upload a file containing the following transactions
      | Date        | Transaction type | Description | Paid out | Paid in   | Balance   |
      | 01 Aug 2016 | Direct Debit     | Mortgage    | £345.67  |           | £12345.78 |
      | 29 Jul 2016 | Bank Credit      | Salary      |          | £23456.67 | £78964.34 |
     When I get all transactions
     Then I should have a transaction from "01 Aug 2016" of type "Direct Debit" with description "Mortgage", paid out "£345.67", paid in "£0.00" and a balance of "£12345.78"
      And I should have a transaction from "29 Jul 2016" of type "Bank Credit" with description "Salary", paid out "£0.00", paid in "£23456.67" and a balance of "£78964.34"
