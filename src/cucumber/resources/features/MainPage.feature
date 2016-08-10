@Web
Feature: show main page 

Scenario: navigate to main page 
    When I view the homepage 
    Then the title should be "Accounts" 
    
Scenario: the main page should have an option to upload a transactions file 
    Given I view the homepage 
    When I upload transactions file "NationwideSample.csv" 
    Then I should see 134 transactions