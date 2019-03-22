package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.MissingFormatArgumentException;
import java.util.Random;


public class VistaSpaceInvaders extends SurfaceView implements Runnable {
    Context context;

    Random generator = new Random();

    MediaPlayer mediaPlayer;

    private int hack;

    private boolean tocaD, tocaI, tocaAR, tocaAB;

    private boolean predict1, predict2, predict3;

    // Esta es nuestra secuencia
    private Thread hiloJuego = null;

    // Nuestro SurfaceHolder para bloquear la superficie antes de que dibujemos nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un booleano el cual vamos a activar y desactivar
    // cuando el juego este activo- o no.
    private volatile boolean jugando;

    // El juego esta pausado al iniciar
    private boolean pausado = true;

    // Un objeto de lienzo (Canvas) y de pintar (Paint)
    private Canvas canvas;
    private Paint paint;

    // Esta variable rastrea los cuadros por segundo del juego
    private long fps;

    // Esto se utiliza para ayudar a calcular los cuadros por segundo
    private long timeThisFrame;

    // El tamaño de la pantalla en pixeles
    private int ejeX;
    private int ejeY;

    // Botones de movimiento
    private BotonM BArriba;
    private BotonM BAbajo;
    private BotonM BDerecha;
    private BotonM BIzquierda;

    // La nave del jugador
    private Nave nave;
    // Nave de ayuda
    private Nave esparrin;

    // Laser
    private Laser laser;

    // Laser de invader espotaneo
    private EnemyLaser espLaser;

    // laseres de los marcianitos
    private EnemyLaser[] marcianitoLaser = new EnemyLaser[200];
    private int proxLaser;
    private int maxMarcianitosLaser = 10;

    // Hasta 60 Marcianitos
    Marcianito[] marcianito = new Marcianito[60];
    int numMarcianitos = 0;

    // Marciano espontaneo
    private Marcianito marcianitoEsp;

    // Las guaridas del jugador están construidas a base de ladrillos
    private Bloque[] bloques = new Bloque[400];
    private int numBloque;

    // La puntuación
    int puntuacion = 0;

    // Vidas
    private int vidas = 1;

    // flag que indica si habilita el disparo
    private boolean isAdult;

    // flag que indica si habilita el rebote
    private boolean rebotes;

    // nombre de jugador
    private String name;

    // ¿Chocó el invader contra el lado de la pantalla?
    boolean bumped = false;

    // ¿Ha perdido el jugador?
    boolean pierde = false;


    // Cuando inicializamos (call new()) en gameView
    // Este método especial de constructor se ejecuta
    public VistaSpaceInvaders(Context context, int x, int y, boolean isAdult, String name, boolean rebotes) {
        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
        // !Que amable¡.
        super(context);

        this.isAdult = isAdult;
        this.name = name;
        this.rebotes = rebotes;

        // Hace una copia del "context" disponible globalmete para que la usemos en otro método
        this.context = context;

        // Inicializa los objetos de ourHolder y paint
        ourHolder = getHolder();
        paint = new Paint();

        ejeX = x;
        ejeY = y;

        prepararNivel();
    }

    // Aquí vamos a inicializar todos los objetos del juego
    private void prepararNivel() {

        //Iniciar musica
        mediaPlayer = MediaPlayer.create(context, R.raw.musica1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //Inicializar hack a 0
        hack = 0;

        // Haz una nave espacial para un jugador nuevo
        nave = new Nave(context, ejeX, ejeY);
        // Inicializa el esparril
        esparrin = new Nave(context, ejeX, ejeY);

        // Prepara la bala del jugador
        laser = new Laser(ejeY);

        // Prepara la bala del espontaneo
        espLaser = new EnemyLaser(ejeY);

        // Prepara botones de disparo
        BArriba = new BotonM(context, ejeX, ejeY, 1700, 150);
        BAbajo = new BotonM(context, ejeX, ejeY, 1700, 50);
        BDerecha = new BotonM(context, ejeX, ejeY, 1650, 100);
        BIzquierda = new BotonM(context, ejeX, ejeY, 1750, 100);

        // Inicializa la formación de invadersBullets
        for (int i = 0; i < marcianitoLaser.length; i++) {
            marcianitoLaser[i] = new EnemyLaser(ejeY);
        }

        // Construye un ejercito de invaders
        numMarcianitos = 0;
        for (int column = 0; column < 6; column++) {
            for (int row = 1; row < 5; row++) {
                marcianito[numMarcianitos] = new Marcianito(context, row, column, ejeX, ejeY);
                numMarcianitos++;
            }
        }

        // Construye el invader espontaneo
        marcianitoEsp = new Marcianito(context, ejeX, ejeY);

        // Construye las guaridas
        numBloque = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 9; column++) {
                for (int row = 0; row < 4; row++) {
                    bloques[numBloque] = new Bloque(row, column, shelterNumber, ejeX, ejeY);
                    numBloque++;
                }
            }
        }
    }
    @Override
    public void run() {
        while (jugando) {

            // Captura el tiempo actual en milisegundos en startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Actualiza el cuadro
            if (!pausado) {
                update();
            }

            // Dibuja el cuadro
            dibujar();

            // Calcula los cuadros por segundo de este cuadro
            // Ahora podemos usar los resultados para
            // medir el tiempo de animaciones y otras cosas más.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }


    }

    private void dibujar() {

        if (ourHolder.getSurface().isValid()) {


            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            // Dibuja el color del fondo
            canvas.drawColor(Color.argb(255, 0, 0, 0)/*, PorterDuff.Mode.CLEAR*/);
            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Dibuja a la nave espacial del jugador
            canvas.drawBitmap(nave.getBitmap(), nave.getX(), nave.getY(), paint);

            // Dibuja Botones disparo
            canvas.drawBitmap(BArriba.getBitmap1(), BArriba.getX(), BArriba.getY(), paint);
            canvas.drawBitmap(BAbajo.getBitmap2(), BAbajo.getX(), BAbajo.getY(), paint);
            canvas.drawBitmap(BDerecha.getBitmap3(), BDerecha.getX(), BDerecha.getY(), paint);
            canvas.drawBitmap(BIzquierda.getBitmap4(), BIzquierda.getX(), BIzquierda.getY(), paint);

            // Dibuja invader espontaneo
            if (marcianitoEsp.getVisibility()){
                canvas.drawBitmap(marcianitoEsp.getBitmap(), marcianitoEsp.getX(), marcianitoEsp.getY(),paint);
            }

            // Dibuja a los invaders
            for (int i = 0; i < numMarcianitos; i++) {
                if (marcianito[i].getVisibility()) {
                    canvas.drawBitmap(marcianito[i].getBitmap(), marcianito[i].getX(), marcianito[i].getY(), paint);
                }
            }

            // Dibuja los ladrillos si están visibles
            for (int i = 0; i < numBloque; i++) {
                if (bloques[i].getVisibility()) {
                    canvas.drawRect(bloques[i].getRect(), paint);
                }
            }

            // Dibuja la bala del jugador si está activa
            if (laser.getStatus()) {
                canvas.drawRect(laser.getRect(), paint);
            }

            // Dibuja la bala del espontaneo si está activa
            if (espLaser.getStatus()) {
                canvas.drawRect(espLaser.getRect(), paint);
            }

            // Dibuja todas las balas de los invaders si están activas
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    canvas.drawRect(marcianitoLaser[i].getRect(), paint);
                }
            }

            // Dibuja la puntuación y las vidas restantes
            // Cambia el color de la brocha
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Puntuacion: " + puntuacion + " Vidas: " + vidas, 10, 50, paint);

            // Extrae todo a la pantalla
            ourHolder.unlockCanvasAndPost(canvas);
        }


    }

    private Marcianito updateInvader(Marcianito marcianito) {
        if (marcianito.getVisibility()) {
            // Mueve el siguiente invader
            marcianito.update(fps);

            // ¿Quiere hacer un disparo?
            if (marcianito.takeAim(nave.getX(),
                    nave.getLength())) {

                // Si sí, intentalo y genera una bala
                if (marcianitoLaser[proxLaser].shoot(marcianito.getX()
                                + marcianito.getLength() / 2,
                        marcianito.getY(), laser.ABAJO)) {

                    // Disparo realizado
                    // Preparete para el siguiente disparo
                    proxLaser++;

                    // Inicia el ciclo repetitivo otra vez al
                    // primero si ya hemos llegado al último.
                    if (proxLaser == maxMarcianitosLaser) {
                        // Esto detiene el disparar otra bala hasta
                        // que una haya completado su trayecto.
                        // Por que si bullet 0 todavia está activo,
                        // shoot regresa a false.
                        proxLaser = 0;
                    }
                }
            }

            // Si ese movimiento causó que golpearan la pantalla,
            // cambia bumped a true.
            if (marcianito.getX() > ejeX - marcianito.getLength()
                    || marcianito.getX() < 0) {

                bumped = true;

            }
        }
        return marcianito;
    }

    private Marcianito bumpedInvader(Marcianito marcianito) {
        marcianito.dropDownAndReverse();
        // Han aterrizado los invaders
        if (marcianito.getVisibility()) {
            if (marcianito.getY() > ejeY - marcianito.getHeight()) {
                pierde = true;
            }
        }
        return marcianito;
    }

    private boolean checkIfLost() {
        for (int i = 0; i < numBloque; i++) {
            if (bloques[i].getVisibility()) {
                if (RectF.intersects(nave.getRect(), bloques[i].getRect())) {
                    return true;
                }
            }
        }
        for (int i = 0; i < numMarcianitos; i++) {
            if (marcianito[i].getVisibility()) {
                if (RectF.intersects(marcianito[i].getRect(), nave.getRect())) {
                    return true;
                }
            }
        }
        if (marcianitoEsp.getVisibility()) {
            if (RectF.intersects(marcianitoEsp.getRect(), nave.getRect())) {
                return true;
            }
        }
        return false;
    }

    private EnemyLaser updateInvaderBullet(EnemyLaser marcianitoLaser) {
        if (marcianitoLaser.getStatus()) {
            marcianitoLaser.update(fps);
        }
        return marcianitoLaser;
    }

    private Nave randomTeleportSpaceShip(Nave nave){
        int randomN = generator.nextInt(200);
        if (randomN == 1){
            predict1 = false;
            predict2 = false;
            predict3 = false;
            int newX = generator.nextInt(ejeX);
            int newY = generator.nextInt(ejeY);
            esparrin.setX(newX);
            esparrin.setY(newY);
            esparrin.update();

            for (int i = 0; i < numMarcianitos; i++) {
                if (marcianito[i].getVisibility()) {
                    if ((RectF.intersects(marcianito[i].getRect(), esparrin.getRect()))) {
                        predict1 = true;
                    }
                }
            }

            for (int j = 0; j < numBloque; j++) {
                if (bloques[j].getVisibility()) {
                    if ((RectF.intersects(esparrin.getRect(), bloques[j].getRect()))) {
                        predict2 = true;
                    }
                }
            }

            if (marcianitoEsp.getVisibility()) {
                if (RectF.intersects(marcianitoEsp.getRect(), esparrin.getRect())) {
                    predict3 = true;
                }
            }

            if (!((predict1)||(predict2)||(predict3))){
                nave.setX(newX);
                nave.setY(newY);
            }
        }

        return nave;
    }

    private Bloque martianBarrerColision(Bloque bloque, Marcianito marcianito){
        if (bloque.getVisibility()) {
            if (RectF.intersects(marcianito.getRect(), bloque.getRect())) {
                bloque.setInvisible();
            }
        }

        return bloque;
    }

    private Laser laserHitUpAndDownScreen(Laser laser, int ejeY){
        if (laser.getImpactPointY() < 0) {
            laser.changeDir();
        } else if (laser.getImpactPointY() > ejeY) {
            laser.changeDir();
        }

        return laser;
    }

    private EnemyLaser enemyLaserHitUpAndDownScreen(EnemyLaser laser, int ejeY){
        if (laser.getImpactPointY() > ejeY) {
            laser.changeDir();
            if (!(laser.isLetal())) {
                laser.hacerLetal();
            }
        } else if (laser.getImpactPointY() < 0) {
            laser.changeDir();
        }

        return laser;
    }

    private Bloque laserImpactBlock(Bloque bloque, Laser laser){
        if (bloque.getVisibility()) {
            if (RectF.intersects(laser.getRect(), bloque.getRect())) {
                laser.setInactive();
                bloque.setInvisible();
                this.updateBitmap(false);
            }
        }
        return bloque;
    }

    private void updateBitmap(Boolean random){
        if (random) {
            for (int m = 0; m < numMarcianitos; m++) {
                int randomNumber = generator.nextInt(2);
                if(randomNumber == 1) {
                    marcianito[m].changeBitmap();
                }
            }
        }else{
            for (int v = 0; v < numMarcianitos; v++) {
                marcianito[v].changeBitmap();
            }
        }
        nave.changeBitmap();
        marcianitoEsp.changeBitmap();
    }

    private Bloque[] laserSimultaneousImpactBlocks(Bloque[] bloques, Laser[] lasers){
        if ((bloques[0].getVisibility()) && (bloques[1].getVisibility())) {
            if ((RectF.intersects(lasers[0].getRect(), bloques[0].getRect()))
                    && (RectF.intersects(lasers[1].getRect(), bloques[1].getRect()))) {
                for (Laser laser : lasers){
                    laser.setInactive();
                }
                for (Bloque bloque : bloques){
                    bloque.setInvisible();
                }
                this.updateBitmap(true);
            }
        }
        return bloques;
    }

    private void update() {

        // Mueve la nave espacial del jugador

        tocaD = false;
        tocaI = false;
        tocaAB = false;
        tocaAR = false;


        if (nave.getX() > ejeX - nave.getLength()) {
            tocaD = true;
        }

        if (nave.getX() < 0) {
            tocaI = true;
        }

        if (nave.getY() < 0) {
            tocaAR = true;
        }

        if (nave.getY() > ejeY - nave.getHeight()) {
            tocaAB = true;
        }

        nave.update(fps, tocaD, tocaI, tocaAR, tocaAB);

        marcianitoEsp.update(fps);

        if (marcianitoEsp.getX() > ejeX - marcianitoEsp.getLength()) {
            marcianitoEsp.reinicio();
        }

        if (marcianitoEsp.getVisibility()){
            if (marcianitoEsp.takeAimEsp()){
                espLaser.shoot(marcianitoEsp.getX() + marcianitoEsp.getLength() / 2,
                        marcianitoEsp.getY(), laser.ABAJO);
            }
        }

        // Actualiza a todos los invaders si están visibles
        for (int i = 0; i < numMarcianitos; i++) {
            marcianito[i] = updateInvader(marcianito[i]);
        }

        // ¿Chocó algún invader en el extremo de la pantalla?

        if (bumped) {
            // Mueve a todos los invaders hacia abajo y cambia la dirección
            for (int i = 0; i < numMarcianitos; i++) {
                marcianito[i] = bumpedInvader(marcianito[i]);
            }
        }

        // Desaparicion y aparicion aleatoria de nave
        //nave = this.randomTeleportSpaceShip(nave);

        // Ha impactado la nave con la barrera o con un invader
        pierde = checkIfLost();

        // Ha impactado un marciano con la barrera
        for (int i = 0; i < numMarcianitos; i++) {
            if (marcianito[i].getVisibility()){
                for (int j = 0; j < numBloque; j++) {
                    this.martianBarrerColision(bloques[j], marcianito[i]);
                }
            }
        }

        if (pierde) {
            mediaPlayer.pause();

            final Activity activity = (Activity) getContext();
            Intent intent = new Intent(activity, MenuActivity.class);
            intent.putExtra(getResources().getString(R.string.victory), false);
            intent.putExtra(getResources().getString(R.string.score), puntuacion);
            intent.putExtra(getResources().getString(R.string.name), this.name);
            intent.putExtra("adult", isAdult);
            intent.putExtra("rebote", rebotes);
            activity.finish();
            activity.startActivity(intent);
            Thread.currentThread().interrupt();
        }

        if (hack == 3) {
            mediaPlayer.pause();

            final Activity activity = (Activity) getContext();
            Intent intent = new Intent(activity, MenuActivity.class);
            intent.putExtra(getResources().getString(R.string.victory), true);
            intent.putExtra(getResources().getString(R.string.score), puntuacion);
            intent.putExtra(getResources().getString(R.string.name), this.name);
            intent.putExtra("adult", isAdult);
            intent.putExtra("rebote", rebotes);
            activity.finish();
            activity.startActivity(intent);
            Thread.currentThread().interrupt();
        }

        if (this.isAdult) {

            // Actualiza la bala del jugador
            if (laser.getStatus()) {
                laser.update(fps);
            }

            // Actualiza la bala del espontaneo
            espLaser.randomMove(ejeX, 50);
            espLaser.update(fps);


            // Actualiza todas las balas de los invaders si están activas
            for (int i = 0; i < marcianitoLaser.length; i++) {
                marcianitoLaser[i] = updateInvaderBullet(marcianitoLaser[i]);
            }

            if (this.rebotes) {
                // Ha tocado la parte alta de la pantalla la bala del jugador
                laser = this.laserHitUpAndDownScreen(laser, ejeY);

                // Ha tocado la parte baja de la pantalla la bala del invader espontaneo
                espLaser = this.enemyLaserHitUpAndDownScreen(espLaser, ejeY);

                // Ha tocado la parte baja de la pantalla la bala del invader
                for (int i = 0; i < marcianitoLaser.length; i++) {
                    marcianitoLaser[i] = this.enemyLaserHitUpAndDownScreen(marcianitoLaser[i], ejeY);
                }

                // Si la bala ha tocado suelo es letal para los invader
                for (int i = 0; i < marcianitoLaser.length; i++) {
                    if ((marcianitoLaser[i].isLetal()) && (marcianitoLaser[i].getStatus())) {
                        for (int j = 0; j < numMarcianitos; j++) {
                            if (marcianito[j].getVisibility()) {
                                if (RectF.intersects(marcianitoLaser[i].getRect(), marcianito[j].getRect())) {
                                    marcianitoLaser[i].setInactive();
                                    marcianito[j].setInvisible();
                                    puntuacion = puntuacion + 100;
                                    // Ha ganado el jugador
                                    if (puntuacion == numMarcianitos * 100) {
                                        mediaPlayer.pause();

                                        final Activity activity = (Activity) getContext();
                                        Intent intent = new Intent(activity, MenuActivity.class);
                                        intent.putExtra(getResources().getString(R.string.name), this.name);
                                        intent.putExtra(getResources().getString(R.string.victory), true);
                                        intent.putExtra(getResources().getString(R.string.score), puntuacion);
                                        intent.putExtra("adult", isAdult);
                                        intent.putExtra("rebote", rebotes);
                                        activity.finish();
                                        activity.startActivity(intent);
                                        Thread.currentThread().interrupt();
                                    }
                                }
                                if (RectF.intersects(marcianitoLaser[i].getRect(), marcianitoEsp.getRect())) {
                                    marcianitoLaser[i].setInactive();
                                    marcianitoEsp.setInvisible();
                                }
                            }
                        }
                    }
                }

                if((espLaser.isLetal()) && (espLaser.getStatus())){
                    for (int i = 0; i < numMarcianitos; i++) {
                        if (marcianito[i].getVisibility()) {
                            if (RectF.intersects(espLaser.getRect(), marcianito[i].getRect())) {
                                espLaser.setInactive();
                                marcianito[i].setInvisible();
                                puntuacion = puntuacion + 100;
                                // Ha ganado el jugador
                                if (puntuacion == numMarcianitos * 100) {
                                    mediaPlayer.pause();

                                    final Activity activity = (Activity) getContext();
                                    Intent intent = new Intent(activity, MenuActivity.class);
                                    intent.putExtra(getResources().getString(R.string.name), this.name);
                                    intent.putExtra(getResources().getString(R.string.victory), true);
                                    intent.putExtra(getResources().getString(R.string.score), puntuacion);
                                    intent.putExtra("adult", isAdult);
                                    intent.putExtra("rebote", rebotes);
                                    activity.finish();
                                    activity.startActivity(intent);
                                    Thread.currentThread().interrupt();
                                }
                            }
                            if (RectF.intersects(espLaser.getRect(), marcianitoEsp.getRect())) {
                                espLaser.setInactive();
                                marcianitoEsp.setInvisible();
                            }
                        }
                    }
                }

            } else {
                // Ha tocado la parte alta de la pantalla la bala del jugador
                if (laser.getImpactPointY() < 0) {
                    laser.setInactive();
                }

                // Ha tocado la parte baja de la pantalla la bala del invader
                for (int i = 0; i < marcianitoLaser.length; i++) {
                    if (marcianitoLaser[i].getImpactPointY() > ejeY) {
                        marcianitoLaser[i].setInactive();
                    }
                }

                // Ha tocado la parte baja de la pantalla la bala del invader espontaneo
                if (espLaser.getImpactPointY() > ejeY) {
                    espLaser.setInactive();
                }
            }

            // Ha tocado la bala del jugador a algún invader
            if (laser.getStatus()) {
                for (int i = 0; i < numMarcianitos; i++) {
                    if (marcianito[i].getVisibility()) {
                        if (RectF.intersects(laser.getRect(), marcianito[i].getRect())) {
                            marcianito[i].setInvisible();
                            laser.setInactive();
                            puntuacion = puntuacion + 100;

                            // Ha ganado el jugador
                            if (puntuacion == numMarcianitos * 100) {
                                mediaPlayer.pause();

                                final Activity activity = (Activity) getContext();
                                Intent intent = new Intent(activity, MenuActivity.class);
                                intent.putExtra(getResources().getString(R.string.name), this.name);
                                intent.putExtra(getResources().getString(R.string.victory), true);
                                intent.putExtra(getResources().getString(R.string.score), puntuacion);
                                intent.putExtra("adult", isAdult);
                                intent.putExtra("rebote", rebotes);
                                activity.finish();
                                activity.startActivity(intent);
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            }

            // Ha tocado la bala del jugador al invader espontaneo
            if (laser.getStatus()) {
                if (marcianitoEsp.getVisibility()) {
                    if (RectF.intersects(laser.getRect(), marcianitoEsp.getRect())) {
                        marcianitoEsp.setInvisible();
                        laser.setInactive();
                    }
                }
            }

            // Ha impactado una bala alienígena a un ladrillo de la guarida
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    for (int j = 0; j < numBloque; j++) {
                        bloques[j] = this.laserImpactBlock(bloques[j], marcianitoLaser[i]);
                    }
                }

            }

            // Ha impactado una bala alienígena esponataneo a un ladrillo de la guarida
            if (espLaser.getStatus()){
                for (int j = 0; j < numBloque; j++) {
                    bloques[j] = this.laserImpactBlock(bloques[j], espLaser);
                }
            }

            // Ha impactado una bala del jugador a un ladrillo de la guarida
            if (laser.getStatus()) {
                for (int i = 0; i < numBloque; i++) {
                    bloques[i] = this.laserImpactBlock(bloques[i], laser);
                }
            }

            // Dos o mas lasers impactan a la vez en la barrera
            for (int j = 0; j < marcianitoLaser.length; j++) {
                if ((espLaser.getStatus()) && (marcianitoLaser[j].getStatus())) {
                    for (int k = 0; k < numBloque; k++) {
                        for (int v = 0; v < numBloque; v++) {
                                Bloque[] twoBlocks = {bloques[k],bloques[v]};
                                Laser[] twoLasers = {espLaser, marcianitoLaser[j]};
                                twoBlocks = laserSimultaneousImpactBlocks(twoBlocks, twoLasers);
                        }
                    }
                }
            }

            for (int j = 0; j < marcianitoLaser.length; j++) {
                if ((laser.getStatus()) && (marcianitoLaser[j].getStatus())) {
                    for (int k = 0; k < numBloque; k++) {
                        for (int v = 0; v < numBloque; v++) {
                            Bloque[] twoBlocks = {bloques[k],bloques[v]};
                            Laser[] twoLasers = {laser, marcianitoLaser[j]};
                            twoBlocks = laserSimultaneousImpactBlocks(twoBlocks, twoLasers);
                        }
                    }
                }
            }

            if ((laser.getStatus()) && (espLaser.getStatus())) {
                for (int k = 0; k < numBloque; k++) {
                    for (int v = 0; v < numBloque; v++) {
                        Bloque[] twoBlocks = {bloques[k],bloques[v]};
                        Laser[] twoLasers = {laser, espLaser};
                        twoBlocks = laserSimultaneousImpactBlocks(twoBlocks, twoLasers);
                    }
                }
            }

            // Ha impactado una bala de un invader a la nave espacial del jugador
            for (int i = 0; i < marcianitoLaser.length; i++) {
                if (marcianitoLaser[i].getStatus()) {
                    if (RectF.intersects(nave.getRect(), marcianitoLaser[i].getRect())) {
                        marcianitoLaser[i].setInactive();
                        vidas--;

                        // Se acabó el juego
                        if (vidas == 0) {
                            mediaPlayer.pause();

                            final Activity activity = (Activity) getContext();
                            Intent intent = new Intent(activity, MenuActivity.class);
                            intent.putExtra(getResources().getString(R.string.name), this.name);
                            intent.putExtra(getResources().getString(R.string.victory), false);
                            intent.putExtra(getResources().getString(R.string.score), puntuacion);
                            intent.putExtra("adult", isAdult);
                            intent.putExtra("rebote", rebotes);
                            activity.finish();
                            activity.startActivity(intent);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            // Ha impactado una bala del invader espontaneo a la nave espacial del jugador
            if (espLaser.getStatus()){
                if (RectF.intersects(nave.getRect(), espLaser.getRect())) {
                    espLaser.setInactive();
                    vidas--;

                    // Se acabó el juego
                    if (vidas == 0) {
                        mediaPlayer.pause();

                        final Activity activity = (Activity) getContext();
                        Intent intent = new Intent(activity, MenuActivity.class);
                        intent.putExtra(getResources().getString(R.string.name), this.name);
                        intent.putExtra(getResources().getString(R.string.victory), false);
                        intent.putExtra(getResources().getString(R.string.score), puntuacion);
                        intent.putExtra("adult", isAdult);
                        intent.putExtra("rebote", rebotes);
                        activity.finish();
                        activity.startActivity(intent);
                        Thread.currentThread().interrupt();
                    }
                }
            }

        }
    }

    // Si SpaceInvadersActivity es pausado/detenido
    // apaga nuestra secuencia.
    public void pause() {
        jugando = false;
        try {
            hiloJuego.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
            Thread.currentThread().interrupt();
        }

    }

    // Si SpaceInvadersActivity es iniciado entonces
    // inicia nuestra secuencia.
    public void resume() {
        jugando = true;
        hiloJuego = new Thread(this);
        hiloJuego.start();
    }

    // La clase de SurfaceView implementa a onTouchListener
    // Así es que podemos anular este método y detectar toques a la pantalla.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // El jugador ha tocado la pantalla
            case MotionEvent.ACTION_DOWN:
                pausado = false;

                if ((motionEvent.getY() < ejeY) && (motionEvent.getX() > ejeX / 2)) {
                    // Disparos lanzados
                    if (laser.shoot(nave.getX() + nave.getLength() / 2, nave.getY(), laser.ARRIBA)) {
                    }

                }

                // Tocar marciano espontaneo tres veces para hack
                if ((motionEvent.getX() > marcianitoEsp.getX())&&(motionEvent.getX() < marcianitoEsp.getX()+ marcianitoEsp.getLength())&&
                        (motionEvent.getY() > marcianitoEsp.getY())&&(motionEvent.getY() < marcianitoEsp.getY()+ marcianitoEsp.getHeight())){
                    hack ++;
                }

                // Movimiento arriba
                if ((motionEvent.getX() > BArriba.getX())&&(motionEvent.getX() < BArriba.getX()+ BArriba.getLength())&&
                        (motionEvent.getY() > BArriba.getY())&&(motionEvent.getY() < BArriba.getY()+ BArriba.getHeight())){
                    nave.setMovementState(nave.up);
                }
                // Movimiento abajo
                if ((motionEvent.getX() > BAbajo.getX())&&(motionEvent.getX() < BAbajo.getX()+ BAbajo.getLength())&&
                        (motionEvent.getY() > BAbajo.getY())&&(motionEvent.getY() < BAbajo.getY()+ BAbajo.getHeight())){
                    nave.setMovementState(nave.down);
                }
                // Movimiento derecha
                if ((motionEvent.getX() > BDerecha.getX())&&(motionEvent.getX() < BDerecha.getX()+ BDerecha.getLength())&&
                        (motionEvent.getY() > BDerecha.getY())&&(motionEvent.getY() < BDerecha.getY()+ BDerecha.getHeight())){
                    nave.setMovementState(nave.right);
                }
                // Movimiento izquierda
                if ((motionEvent.getX() > BIzquierda.getX())&&(motionEvent.getX() < BIzquierda.getX()+ BIzquierda.getLength())&&
                        (motionEvent.getY() > BIzquierda.getY())&&(motionEvent.getY() < BIzquierda.getY()+ BIzquierda.getHeight())){
                    nave.setMovementState(nave.left);
                }

                break;

            // El jugador ha retirado su dedo de la pantalla
            case MotionEvent.ACTION_UP:
                if ((motionEvent.getX() < ejeX / 2)) {
                    nave.setMovementState(nave.stopped);
                }

                break;
        }
        return true;
    }
}
