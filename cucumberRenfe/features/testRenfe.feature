Feature: Visitor can navigate on the web
  Scenario: As a viwer I can find the "Ministerio de Fomento" logo
    Given I am on the Renfe homepage
    Then I should see the Ministerio de Fomento logo

  Scenario: As a viwer I can see the login link
      Given I am on the Renfe homepage
      Then I should see CLIENTES RENFE. IDENTIFÍCATE. in a link

  Scenario: As a user I can navigate to the login page
    Given I am on the Renfe homepage
    When I click on the login link
    Then I should see the login page

  Scenario: As a user I can see the login form
    Given I am on the Renfe login page
    Then I should see the field Usuario / email
    And I should see the field Contraseña

  Scenario: As a user I can succesfully login
    Given I am on the Renfe login page
    When I fill in Usuario / email with email
    And I fill in Contraseña with pass
    And I click Entrar
    Then I should see the Ventas homepage