package com.example.aleja.spaceinvaders;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.println("Prueba tests");
        Log.d("Prueba", "Prueba test con log");
        assertEquals("Prueba test sencillo",4, 2 + 2);
    }
}