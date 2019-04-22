Feature: Login feature

  Scenario: As a valid user I can play witout bounce
    Given I enter text "Pedro" into field with id "name"
    And I go back
    And I enter text "21" into field with id "age"
    And I go back
    When I press "confirm"
    Then I wait

  Scenario: As a valid user I can play with bounce
    Given I enter text "Pedro" into field with id "name"
    And I go back
    And I enter text "21" into field with id "age"
    And I go back
    And I press "switch1"
    When I press "confirm"
    Then I wait
  
  Scenario: As a user I cant play without put my age
    Given I enter text "Pedro" into field with id "name"
    And I go back
    When I press "confirm"
    Then I see "Error"

  Scenario: As a user I can play as anonym
    Given I enter text "21" into field with id "age"
    And I go back
    When I press "confirm"
    Then I wait

  Scenario: As a user I can play if I'm younger
    Given I enter text "Pedro" into field with id "name"
    And I go back
    And I enter text "1" into field with id "age"
    And I go back
    And I press "switch1"
    When I press "confirm"
    Then I wait
