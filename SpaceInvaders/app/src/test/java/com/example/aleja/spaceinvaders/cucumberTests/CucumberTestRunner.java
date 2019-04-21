package com.example.aleja.spaceinvaders.cucumberTests;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(features="src/assets/features",plugin = {"pretty"})
public class CucumberTestRunner{
}
