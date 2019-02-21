package com.example.aleja.spaceinvaders;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class LaserTest {

    Laser laser = new Laser(1080);

    @Test
    public void shootTest() {
        assertEquals(true, laser.shoot(50, 0, laser.ABAJO));
    }

    @Test
    public void movementTest() {

        laser.update(30);
        assertEquals(11, laser.getImpactPointY(), 1);
        laser.update(30);
        assertEquals(23, laser.getImpactPointY(), 1);
    }
}