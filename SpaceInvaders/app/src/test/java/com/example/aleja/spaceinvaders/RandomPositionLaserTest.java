package com.example.aleja.spaceinvaders;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class RandomPositionLaserTest {
    private Laser laser;

    @Before
    public void multipleInit(){
        this.laser = new Laser(1080);
        laser.shoot(1920/2,1080/2,laser.ABAJO);
    }

    //Prueba si la posición del laser es correcta si la probabilidad de que ocurra un randomMove sea 0%
    @Test
    public void testRandomPosition0Prob() {
        laser.randomMove(1920, 0);
        laser.update(30);

        Assert.assertFalse(laser.getRandomStatus());
    }

    //Prueba si la posición del laser es correcta si la probabilidad de que ocurra un randomMove sea 50%
    @Test
    public void testRandomPosition50Prob(){
        laser.randomMove(1920, 0);
        laser.update(30);
        if (!laser.getRandomStatus()){
            Assert.assertFalse(laser.getRandomStatus());
        } else {
            Assert.assertFalse(1920/2 == laser.getX());
            Assert.assertTrue(laser.getX() <= 1920 && laser.getX() >= 0);
        }
    }

    //Prueba si la posición del laser es correcta si la probabilidad de que ocurra un randomMove sea 100%
    @Test
    public void testRandomPosition100Prob() {
        laser.randomMove(1920, 100);
        laser.update(30);

        Assert.assertFalse(1920/2 == laser.getX());
        Assert.assertTrue(laser.getX() <= 1920 && laser.getX() >= 0);
    }
}