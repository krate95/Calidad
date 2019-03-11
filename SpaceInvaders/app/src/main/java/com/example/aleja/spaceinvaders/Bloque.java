package com.example.aleja.spaceinvaders;

import android.graphics.RectF;

public class Bloque {
    private RectF rect;

    private boolean isVisible;

    public Bloque(int row, int column, int shelterNumber, int screenX, int screenY){

        int width = screenX / 90;
        int height = screenY / 40;

        isVisible = true;

        int brickPadding = 1;

        // El número de guaridas
        int shelterPadding = screenX / 9;
        int startHeight = (int)(screenY - ((double)screenY /8 * 2.2));

        rect = new RectF(column * width + brickPadding +
                (shelterPadding * shelterNumber) +
                (float)shelterPadding + shelterPadding * shelterNumber,
                row * height + brickPadding + (float)startHeight,
                column * width + width - brickPadding +
                        (shelterPadding * shelterNumber) +
                        shelterPadding + (float)shelterPadding * shelterNumber,
                row * height + height - brickPadding + (float)startHeight);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
