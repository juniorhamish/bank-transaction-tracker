@API
Feature: Rest API for categorising transactions

  Scenario: Add a category
    Given I add category "Eating Out"
     When I get all categories
     Then I should have category "Eating Out"

  Scenario: can't add a category with a name that already exists
    Given I add category "Foo"
     When I add category "Foo"
     Then I should get a 400 response code

  Scenario: get a category by name
    Given I add category "Eating Out"
     When I get category "Eating Out"
     Then the response should contain a category with name "Eating Out"

  Scenario: get a category by name that doesn't exist
    When I get category "Eating Out"
    Then I should get a 404 response code

  Scenario: delete all categories
    Given I add category "Eating Out"
     When I delete all categories
     Then there should be no categories

  Scenario: delete category by name
    Given I add category "FooBar"
     When I delete category "FooBar"
     Then there should be no categories

  Scenario: delete category should return list of all categories
    Given I add category "A"
      And I add category "B"
     When I delete category "A"
     Then the response should contain a category with name "B"

  Scenario: set category matchers
    Given I add category "Shopping"
     When I set matchers for category "Shopping" to:
      | Tesco      |
      | Sainsburys |
      | ASDA       |
      And I get category "Shopping"
     Then the category should have matchers:
      | ASDA       |
      | Sainsburys |
      | Tesco      |

  Scenario: set category matchers should overwrite existing matchers
    Given I add category "Foo"
      And I set matchers for category "Foo" to:
      | A |
      | B |
      | C |
     When I set matchers for category "Foo" to:
      | D |
      And I get category "Foo"
     Then the category should have matchers:
      | D |

  Scenario: cannot create a category with empty string
    When I add category ""
    Then I should get a 400 response code

  Scenario: cannot create a category with empty string
    When I add category "    "
    Then I should get a 400 response code
