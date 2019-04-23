Feature: Boton que te lleva a ranking desde el menu de inicio

  Scenario: Como un usuario valido si pulso el boton de ranking me lleva a la pantalla de ranking
    Given introduzco pedro en el input name
    And  introduzco 21 en el input age
    When presiono ranking
    Then me lleva a la pantalla de ranking

  Scenario: Como un usuario si pulso el boton de ranking sin poner mi nombre pero introducioendo mi edad me lleva a la pantalla de ranking
    Given introduzco 21 en el input age
    When presiono ranking
    Then me lleva a la pantalla de ranking
  
  Scenario: Como usuario no puedo entrar en la pantalla de ranking si introduzco mi nombre y no mi edad
    Given introduzco pedro en el input name
    When presiono ranking
    Then vere "Por favor, introduzca la edad"

  Scenario: Como usuario puedo ver el ranking siendo anonimo
    When presiono ranking
    Then me lleva a la pantalla de ranking

  Scenario: Como usuario puedo ver el ranking si soy menor de edad
    Given introduzco pedro en el input name
    And  introduzco 1 en el input age
    When presiono ranking
    Then me lleva a la pantalla de ranking