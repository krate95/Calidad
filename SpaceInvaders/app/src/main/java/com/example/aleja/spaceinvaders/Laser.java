package com.example.aleja.spaceinvaders;

import android.graphics.RectF;
import java.util.Random;

public class Laser {
    private float x;
    private float y;

    private RectF rect;

    // En qué dirección se está disparando
    public final int ARRIBA = 0;
    public final int ABAJO = 1;

    // No vas a ningún lado
    int heading = -1;
    float velocidad =  350;

    private int width = 5;
    private int height;

    private boolean isActive;

    private boolean randomStatus;

    // Ha tocado ya un borde ?
    private boolean letal;

    public Laser(int screenY) {

        height = screenY / 20;
        isActive = false;

        rect = new RectF();
    }
    public RectF getRect(){
        return  rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public float getImpactPointY(){
        if (heading == ABAJO){
            return y + height;
        }else{
            return  y;
        }

    }

    public boolean getRandomStatus(){
        return this.randomStatus;
    }
    public void setRabdomStatus(Boolean b) {
        this.randomStatus = b;
    }

    public float getX(){
        return this.x;
    }
    public void setX(float x){
        this.x = x;
    }

    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            letal = false;   // Solo sirve para los laser de invaders
            heading = direction;
            isActive = true;
            return true;
        }

        // La bala ya está activa
        return false;
    }

    public void hacerLetal(){
        letal = true;
    }

    public void changeDir(){
        if(this.heading == ABAJO){
            this.heading = ARRIBA;
        } else{
            this.heading = ABAJO;
        }
    }

    public void update(long fps){

        // Solo se mueve para arriba o abajo
        if(heading == ARRIBA){
            y = y - velocidad / fps;
        }else{
            y = y + velocidad / fps;
        }

        // Actualiza rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }

    public boolean isLetal() {
        return letal;
    }

    public void randomMove(int ejeX, int porcentaje){
        Random randomGen = new Random();
        int randomMoveStatus = randomGen.nextInt(100);
        if (randomMoveStatus < porcentaje){
            this.setRabdomStatus(true);
            this.setX(randomGen.nextInt(ejeX));

        }else{
            this.setRabdomStatus(false);
        }
    }
}
