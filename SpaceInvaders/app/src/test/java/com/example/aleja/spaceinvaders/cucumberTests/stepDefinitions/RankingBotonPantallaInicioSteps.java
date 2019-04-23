package com.example.aleja.spaceinvaders.cucumberTests.stepDefinitions;

import android.content.Intent;

import com.example.aleja.spaceinvaders.MainActivity;
import com.example.aleja.spaceinvaders.RankingActivity;
import com.example.aleja.spaceinvaders.ScoreDdHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RankingBotonPantallaInicioSteps {

    private MainActivity mainActivity = new MainActivity();
    private RankingActivity rankingActivity = mock(RankingActivity.class);
    private Intent intent = new Intent(mainActivity, RankingActivity.class);

    private String name;
    private int age;

    @Given("introduzco pedro en el input name")
    public void introduzco_pedro_en_el_input_name() {
        name = "Pedro";
    }

    @Given("introduzco {int} en el input age")
    public void introduzco_en_el_input_age(Integer int1) {
        age = int1;
    }

    @When("presiono ranking")
    public void presiono_ranking() {
        intent.putExtra("adult",age);
        intent.putExtra("Name", name);
        mainActivity.startActivity(intent);
    }

    @Then("me lleva a la pantalla de ranking")
    public void me_lleva_a_la_pantalla_de_ranking() {
        ScoreDdHelper helper = new ScoreDdHelper(rankingActivity);
        assertNotNull(helper);
    }

    @Then("vere {string}")
    public void vere(String string) {
        assertEquals("Por favor, introduzca la edad",string);
    }
}
