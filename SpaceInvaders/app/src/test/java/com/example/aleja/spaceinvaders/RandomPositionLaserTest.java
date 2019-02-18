package com.example.aleja.spaceinvaders;

import org.junit.Test;

import org.junit.Assert;

public class RandomPositionLaserTest {

    @Test
    public void testPosition() {
        Laser laser = new Laser(1080);
        laser.shoot(1920/2,1080/2,laser.ABAJO);
        laser.randomMove();
        laser.update(30);
        if (laser.getRandomStatus()){
            Assert.assertTrue(laser.getRandomStatus());
        } else {
            Assert.assertFalse(1920/2 == laser.getX());
        }
    }

}
