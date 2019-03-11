package com.example.aleja.spaceinvaders;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class NaveTest {
    private SpaceInvaders SpaceInvaders = mock(SpaceInvaders.class);
    private Nave nave = new Nave(SpaceInvaders, 100, 100);

    @Test
    public void update() {
        assertEquals(50, nave.getX(), 1);
        float Xtemp = nave.getX();


        nave.setMovementState(nave.right);
        nave.update(30, false, false, false, false);
        assertEquals(Xtemp+11.6, nave.getX(), 1);
        Xtemp += 11.6;


        nave.update(30, false, false, false, false);
        assertEquals(Xtemp+11.6, nave.getX(), 2);
        Xtemp += 11.6;


        nave.update(30, true, false, false, false);
        assertEquals(Xtemp, nave.getX(), 1);
    }
}