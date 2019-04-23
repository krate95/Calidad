package com.example.aleja.spaceinvaders.cucumberTests.stepDefinitions;

import com.example.aleja.spaceinvaders.Marcianito;
import com.example.aleja.spaceinvaders.SpaceInvaders;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class MarcianitoAumentaVelocidadSteps {
    private com.example.aleja.spaceinvaders.SpaceInvaders SpaceInvaders = mock(SpaceInvaders.class);
    private static int screenX=200;
    private static int screenY=400;
    private Marcianito marcianito = new Marcianito(SpaceInvaders, screenX, screenX);
    private Marcianito marcianitoRapido = new Marcianito(SpaceInvaders, screenX, screenX);

    @Given("los marcianitos van bajando")
    public void los_marcianitos_van_bajando() {
        marcianito.dropDownAndReverse();
        marcianitoRapido.dropDownAndReverse();
        assertTrue(marcianito.getY() < (screenY/2));
    }

    @When("los marcianitos bajan hasta la mitad de la pantalla")
    public void los_marcianitos_bajan_hasta_la_mitad_de_la_pantalla() {
        while(marcianitoRapido.getY() <= (screenY/2)) { //Mover marcianito rapido a la segunda mitad de la pantalla
            marcianitoRapido.dropDownAndReverse();
        }
        assertTrue(marcianitoRapido.getY()> (screenY/2));
    }

    @Then("los marcianitos aumentan su velocidad")
    public void los_marcianitos_aumentan_su_velocidad() {
        float velocidadMarcianitoRapido = (float) marcianitoRapido.getShipSpeed();
        float velocidadMarcianito = (float) marcianito.getShipSpeed();
        assertTrue(velocidadMarcianitoRapido > velocidadMarcianito);

    }
}
