Feature: Marcianito aumenta la velocidad cuando llegan a la mitad de la pantalla
  Scenario: Como usuario quiero que cuando los marcianitos lleguen a la mitad de la pantalla aumente su velocidad para aumentar la dificultad del juego
    Given los marcianitos van bajando
    When los marcianitos bajan hasta la mitad de la pantalla
    Then los marcianitos aumentan su velocidad