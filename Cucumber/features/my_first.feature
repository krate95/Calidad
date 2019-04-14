Feature: Login feature

  Scenario: As a valid user I can log into my app
    Given I enter text "Pedro" into field with id "name"
    And I enter "21" into input field with id "age"
    And I go back
    When I press "confirm"
    Then I see "Welcome to coolest app ever"
