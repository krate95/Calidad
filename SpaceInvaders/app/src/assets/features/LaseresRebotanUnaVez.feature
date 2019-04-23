Feature: Los laseres solo podran rebotar una vez
  Scenario: Como product owner quiero que los laseres solo puedan rebotar una vez para disminuir el numero de elementos en pantalla
    Given el disparo ya ha rebotado una vez
    When el disparo toque una pared
    Then el disparo desaparecera