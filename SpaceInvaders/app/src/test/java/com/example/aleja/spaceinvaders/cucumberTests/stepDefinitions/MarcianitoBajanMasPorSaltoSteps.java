package com.example.aleja.spaceinvaders.cucumberTests.stepDefinitions;

import com.example.aleja.spaceinvaders.Marcianito;
import com.example.aleja.spaceinvaders.SpaceInvaders;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;


public class MarcianitoBajanMasPorSaltoSteps {
    private com.example.aleja.spaceinvaders.SpaceInvaders SpaceInvaders = mock(SpaceInvaders.class);
    private static int screenX=200;
    private static int screenY=400;
    private Marcianito marcianito = new Marcianito(SpaceInvaders, screenX, screenX);
    private Marcianito marcianitoRapido = new Marcianito(SpaceInvaders, screenX, screenX);

    @Given("los marcianitos bajan")
    public void los_marcianitos_bajan() {
        marcianito.dropDownAndReverse();
        marcianitoRapido.dropDownAndReverse();
    }

    @When("los marcianitos llegan a la mitad de la pantalla")
    public void los_marcianitos_llegan_a_la_mitad_de_la_pantalla() {
        while(marcianitoRapido.getY() < (screenY/2)) {
            marcianitoRapido.dropDownAndReverse();
        }
    }

    @Then("los marcianitos bajan mas por salto")
    public void los_marcianitos_bajan_mas_por_salto() {
        float yMarcianito = marcianito.getY();
        float yMarcianitoRapido = marcianitoRapido.getY();

        marcianito.dropDownAndReverse();
        marcianitoRapido.dropDownAndReverse();

        float avanceMarcianito  = marcianito.getY()-yMarcianito; //Lo que han bajado los dos marcianitos normales
        float avanceMarcianitoRapido = marcianitoRapido.getY() - yMarcianitoRapido; //Lo que han bajado los marcianitos que estan por debajo de la mitad

        assertTrue(avanceMarcianitoRapido > avanceMarcianito);

    }

}
