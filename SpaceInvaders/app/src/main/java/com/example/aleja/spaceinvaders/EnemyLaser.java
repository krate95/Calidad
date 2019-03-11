package com.example.aleja.spaceinvaders;

import java.util.Random;

public class EnemyLaser extends Laser {

    private boolean randomStatus;
    private boolean letal;

    public EnemyLaser(int screenY){
        super(screenY);
    }

    public boolean getRandomStatus(){
        return this.randomStatus;
    }
    public void setRandomStatus(Boolean b) {
        this.randomStatus = b;
    }

    @Override
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
        this.letal = true;
    }
    public boolean isLetal() {
        return this.letal;
    }

    public void randomMove(int ejeX, int percentaje){
        Random randomGen = new Random();
        int randomMoveStatus = randomGen.nextInt(100);
        if (randomMoveStatus < percentaje){
            this.setRandomStatus(true);
            this.setX(randomGen.nextInt(ejeX));

        }else{
            this.setRandomStatus(false);
        }
    }

}
