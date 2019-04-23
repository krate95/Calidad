package com.example.aleja.spaceinvaders.cucumberTests.stepDefinitions;

import com.example.aleja.spaceinvaders.Laser;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

public class LaseresRebotanUnaVezSteps {
    private static int FPS = 30;
    private static int SCREENY = 200;
    private Laser laser = new Laser(SCREENY);

    @Given("el disparo ya ha rebotado una vez")
    public void el_disparo_ya_ha_rebotado_una_vez() {
        laser.shoot(10,10,Laser.ABAJO);
        laser.changeDir(); //Rebote
        assertTrue(laser.getStatus());
    }

    @When("el disparo toque una pared")
    public void el_disparo_toque_una_pared(){
        laser.changeDir();
        assertTrue(laser.getStatus());
    }

    @Then("el disparo desaparecera")
    public void el_disparo_desaparecera() {
        laser.update(FPS);
        assertFalse(laser.getStatus());
    }
}
