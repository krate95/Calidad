package com.example.aleja.spaceinvaders;

import android.graphics.RectF;

public class Laser {
    protected float x;
    protected float y;
    protected RectF rect;

    // En qué dirección se está disparando
    public static final int ARRIBA = 0;
    public static final int ABAJO = 1;

    // No vas a ningún lado
    int heading = -1;
    float velocidad =  350;

    protected int width = 5;
    protected int height;
    protected boolean isActive;

    private int reboundCount;

    public Laser(int screenY) {

        height = screenY / 20;
        isActive = false;
        reboundCount = 0;

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
            heading = direction;
            isActive = true;
            return true;
        }

        // La bala ya está activa
        return false;
    }

    public void changeDir(){
        if(this.heading == ABAJO){
            this.heading = ARRIBA;
            this.reboundCount++;
        } else{
            this.heading = ABAJO;
            this.reboundCount++;
        }
    }

    public void update(long fps){

        // Solo se mueve para arriba o abajo
        if(heading == ARRIBA){
            y = y - velocidad / fps;
        }else{
            y = y + velocidad / fps;
        }

        //Desactiva el disparo si ha rebotado mas de una vez
        this.isActive = (this.reboundCount < 1);

        // Actualiza rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }
}
