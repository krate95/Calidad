Feature: Ranking Home Button

  Scenario: As a valid user I can view the ranking menu
    Given I enter text "Pedro" into field with id "name"
    And I go back
    And I enter text "21" into field with id "age"
    And I go back
    When I press "ranking"
    Then I wait for "ranking_view" to appear

  Scenario: As a valid user I can view the ranking without put my name but introducing my age
    Given I enter text "21" into field with id "age"
    And I go back
    When I press "ranking"
    Then I wait for "ranking_view" to appear
  
  Scenario: As a user I cant view the rankings if I put my name and not my age
    Given I enter text "Pedro" into field with id "name"
    And I go back
    When I press "ranking"
    Then I see "Por favor, introduzca la edad"

  Scenario: As a user I can view the ranking as anonym
    When I press "ranking"
    Then I wait for "ranking_view" to appear

  Scenario: As a user I can't view the ranking if I'm younger
    Given I enter text "Pedro" into field with id "name"
    And I go back
    And I enter text "1" into field with id "age"
    And I go back
    When I press "ranking"
    Then I wait for "ranking_view" to appear
