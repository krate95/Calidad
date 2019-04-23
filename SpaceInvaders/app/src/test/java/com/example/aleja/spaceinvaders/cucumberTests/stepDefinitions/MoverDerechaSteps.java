package com.example.aleja.spaceinvaders.cucumberTests.stepDefinitions;

import com.example.aleja.spaceinvaders.Nave;
import com.example.aleja.spaceinvaders.SpaceInvaders;
import static org.junit.Assert.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.mockito.Mockito.mock;

public class MoverDerechaSteps {
    private SpaceInvaders SpaceInvaders = mock(SpaceInvaders.class);
    private static int screenMaxWidth = 100;
    private static int screenMaxHeight = 100;
    private Nave nave = new Nave(SpaceInvaders,screenMaxWidth, screenMaxHeight);
    private float posX = nave.getX();

    @Given("muevo la navecita hacia la derecha")
    public void muevo_la_navecita_hacia_la_derecha() {
        nave.setMovementState(2); //2 Para la derecha
        nave.update(30, false, false, false, false);
        assertTrue(nave.getShipMoving()==nave.RIGHT);
    }

    @Given("la nave no esta en el limite derecho de la pantalla")
    public void la_nave_no_esta_en_el_limite_derecho_de_la_pantalla() {
        nave.update(30, false, false, false, false);
        assertFalse(nave.getX()==(long) screenMaxWidth);
    }

    @Then("la nave se mueve a la derecha")
    public void la_nave_se_mueve_a_la_derecha() {
        nave.setX(posX+10);
        nave.update(30, false, false, false, false);
        assertTrue(nave.getX() > posX);
    }

    @Given("la nave esta en el limite superior de la pantalla")
    public void la_nave_esta_en_el_limite_superior_de_la_pantalla() {
        nave.setY(screenMaxHeight);
        nave.update(30, false, false, true, false);
        assertTrue(nave.getY() == screenMaxHeight);
    }

    @Given("la nave esta en el limite inferior de la pantalla")
    public void la_nave_esta_en_el_limite_inferior_de_la_pantalla() {
        nave.setY(0);
        nave.update(30, false, false, false, true);
        assertTrue(nave.getY() == 0);
    }

    @Given("la nave esta en el limite derecho de la pantalla")
    public void la_nave_esta_en_el_limite_derecho_de_la_pantalla() {
        nave.setX(screenMaxWidth);
        posX = nave.getX();
        nave.setMovementState(nave.STOPPED);
        nave.update(30, true, false, false, false);
        assertTrue(nave.getX() == screenMaxWidth);
    }

    @Then("la nave se queda quieta")
    public void la_nave_se_queda_quieta() {
        assertTrue(nave.getShipMoving() == nave.STOPPED);
    }


}
