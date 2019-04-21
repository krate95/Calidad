Feature: Mover la navecita hacia la derecha
  #NO estamos en el limite derecho
  Scenario: Pulsamos el boton a la derecha y la nave NO esta en el limite derecho de la pantalla
    Given muevo la navecita hacia la derecha
    And la nave no esta en el limite derecho de la pantalla
    Then la nave se mueve a la derecha

  Scenario: Pulsamos el boton a la derecha y la navecita esta en el limite izquierdo de la pantalla
    Given muevo la navecita hacia la derecha
    And  la nave no esta en el limite derecho de la pantalla
    Then la nave se mueve a la derecha

  Scenario: Pulsamos el boton a la derecha y la nave esta en el limite superior de la pantalla
    Given muevo la navecita hacia la derecha
    And la nave esta en el limite superior de la pantalla
    Then la nave se mueve a la derecha

  Scenario: Pulsamos el boton a la derecha y la nave esta en el limite inferior de la pantalla
    Given muevo la navecita hacia la derecha
    And la nave esta en el limite inferior de la pantalla
    Then la nave se mueve a la derecha

  #SI estamos en el limite derecho
  Scenario: Pulsamos el boton a la derecha y la nave esta en el limite derecho de la pantalla
    Given muevo la navecita hacia la derecha
    And la nave esta en el limite derecho de la pantalla
    Then la nave se queda quieta
