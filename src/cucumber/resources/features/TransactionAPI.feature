@API 
Feature: Rest API for dealing with transactions 

Scenario: Upload transaction file 
    When I upload "NationwideSample.csv" 
    Then I should get a success response 
    
Scenario: uploaded transactions should be available 
    Given I upload "NationwideSample.csv" 
    When I get all transactions 
    Then there should be 134 transactions