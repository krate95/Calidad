Feature: Los marcianitos cuando pasen de la mitad de la pantalla avanzaran el doble de lejos que antes
  Scenario: Como usuario quiero que cuando los marcianitos pasen de la mitad de la pantalla avancen el doble para aumentar la dificultad
    Given los marcianitos bajan
    When los marcianitos llegan a la mitad de la pantalla
    Then los marcianitos bajan mas por salto