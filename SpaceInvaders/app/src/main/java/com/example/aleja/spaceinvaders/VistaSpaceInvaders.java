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

import java.util.Random;


public class VistaSpaceInvaders extends SurfaceView implements Runnable {
    Context context;

    Random generator = new Random();

    MediaPlayer mediaPlayer;

    private int hack;

    // Esta es nuestra secuencia
    private Thread hiloJuego = null;

    // Nuestro SurfaceHolder para bloquear la superficie antes de que dibujemos nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un booleano el cual vamos a activar y desactivar
    // cuando el juego este activo- o no.
    private volatile boolean jugando;

    // El juego esta pausado al iniciar
    private boolean pausado = true;

    // Un objeto de lienzo de pintar (Paint)
    private Paint paint;

    // Esta variable rastrea los cuadros por segundo del juego
    private long fps;

    // El tamaño de la pantalla en pixeles
    private int ejeX;
    private int ejeY;

    // Botones de movimiento
    private BotonM upButton;
    private BotonM downButton;
    private BotonM rightButton;
    private BotonM leftButton;

    // La nave del jugador
    private Nave nave;

    // Laser
    private Laser laser;

    // Laser de invader espotaneo
    private EnemyLaser espLaser;

    // laseres de los marcianitos
    private EnemyLaser[] marcianitoLaser = new EnemyLaser[200];
    private int proxLaser;

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

    private static final String ADULT = "adult";
    private static final String REBOTE = "rebote";


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

        // Prepara la bala del jugador
        laser = new Laser(ejeY);

        // Prepara la bala del espontaneo
        espLaser = new EnemyLaser(ejeY);

        // Prepara botones de disparo
        upButton = new BotonM(context, ejeX, ejeY, 1700, 150);
        downButton = new BotonM(context, ejeX, ejeY, 1700, 50);
        rightButton = new BotonM(context, ejeX, ejeY, 1650, 100);
        leftButton = new BotonM(context, ejeX, ejeY, 1750, 100);

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
        // Esto se utiliza para ayudar a calcular los cuadros por segundo
        long timeThisFrame;

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
        Canvas canvas;

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
            canvas.drawBitmap(upButton.getBitmap1(), upButton.getX(), upButton.getY(), paint);
            canvas.drawBitmap(downButton.getBitmap2(), downButton.getX(), downButton.getY(), paint);
            canvas.drawBitmap(rightButton.getBitmap3(), rightButton.getX(), rightButton.getY(), paint);
            canvas.drawBitmap(leftButton.getBitmap4(), leftButton.getX(), leftButton.getY(), paint);

            // Dibuja invader espontaneo
            if (marcianitoEsp.getVisibility()) {
                canvas.drawBitmap(marcianitoEsp.getBitmap(), marcianitoEsp.getX(), marcianitoEsp.getY(), paint);
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
            for (EnemyLaser aMarcianitoLaser : marcianitoLaser) {
                if (aMarcianitoLaser.getStatus()) {
                    canvas.drawRect(aMarcianitoLaser.getRect(), paint);
                }
            }

            // Dibuja la puntuación y las vidas restantes
            // Cambia el color de la brocha
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Puntuacion: " + puntuacion + " Vidas: " + vidas, 10, 50, paint);

            // Extrae los elementos a la pantalla
            ourHolder.unlockCanvasAndPost(canvas);
        }


    }

    private Marcianito updateInvader(Marcianito marcianito) {
        if (marcianito.getVisibility()) {
            // Mueve el siguiente invader
            marcianito.update(fps);

            // ¿Quiere hacer un disparo?
            if (marcianito.takeAim(nave.getX(),
                    nave.getLength()) && marcianitoLaser[proxLaser].shoot(marcianito.getX()
                            + marcianito.getLength() / 2,
                    marcianito.getY(), Laser.ABAJO)) {

                // Disparo realizado
                // Preparete para el siguiente disparo
                proxLaser++;

                // Inicia el ciclo repetitivo otra vez al
                // primero si ya hemos llegado al último.
                int maxMarcianitosLaser = 10;
                if (proxLaser == maxMarcianitosLaser) {
                    // Esto detiene el disparar otra bala hasta
                    // que una haya completado su trayecto.
                    // Por que si bullet 0 todavia está activo,
                    // shoot regresa a false.
                    proxLaser = 0;
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
        if (marcianito.getVisibility() && marcianito.getY() > ejeY - marcianito.getHeight()) {
            pierde = true;
        }
        return marcianito;
    }

    private boolean checkIfLost() {
        for (int i = 0; i < numBloque; i++) {
            if (bloques[i].getVisibility() && RectF.intersects(nave.getRect(), bloques[i].getRect())) {
                return true;
            }
        }
        for (int i = 0; i < numMarcianitos; i++) {
            if (marcianito[i].getVisibility() && RectF.intersects(marcianito[i].getRect(), nave.getRect())) {
                return true;
            }
        }
        return marcianitoEsp.getVisibility() && RectF.intersects(marcianitoEsp.getRect(), nave.getRect());
    }

    private EnemyLaser updateInvaderBullet(EnemyLaser marcianitoLaser) {
        if (marcianitoLaser.getStatus()) {
            marcianitoLaser.update(fps);
        }
        return marcianitoLaser;
    }

    private Nave randomTeleportSpaceShip(Nave nave) {
        // Nave de ayuda
        // Inicializa el esparril
        Nave esparrin = new Nave(context, ejeX, ejeY);
        boolean predict1;
        boolean predict2;
        boolean predict3;

        int randomN = generator.nextInt(200);
        if (randomN == 1) {
            predict1 = false;
            predict2 = false;
            predict3 = false;
            int newX = generator.nextInt(ejeX);
            int newY = generator.nextInt(ejeY);
            esparrin.setX(newX);
            esparrin.setY(newY);
            esparrin.update();

            for (int i = 0; i < numMarcianitos; i++) {
                if (marcianito[i].getVisibility() && RectF.intersects(marcianito[i].getRect(), esparrin.getRect())) {
                    predict1 = true;
                }
            }

            for (int j = 0; j < numBloque; j++) {
                if (bloques[j].getVisibility() && RectF.intersects(esparrin.getRect(), bloques[j].getRect())) {
                    predict2 = true;
                }
            }

            if (marcianitoEsp.getVisibility() && RectF.intersects(marcianitoEsp.getRect(), esparrin.getRect())) {
                predict3 = true;
            }

            if (!((predict1) || (predict2) || (predict3))) {
                nave.setX(newX);
                nave.setY(newY);
            }
        }

        return nave;
    }

    private Bloque martianBarrierColision(Bloque bloque, Marcianito marcianito) {
        if (bloque.getVisibility() && RectF.intersects(marcianito.getRect(), bloque.getRect())) {
            bloque.setInvisible();
        }

        return bloque;
    }

    private Laser laserHitUpAndDownScreen(Laser laser, int ejeY) {
        if ((laser.getImpactPointY() < 0) || (laser.getImpactPointY() > ejeY)) {
            laser.changeDir();
        }
        return laser;
    }

    private EnemyLaser enemyLaserHitUpAndDownScreen(EnemyLaser laser, int ejeY) {
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

    private Bloque laserImpactBlock(Bloque bloque, Laser laser) {
        if (bloque.getVisibility() && RectF.intersects(laser.getRect(), bloque.getRect())) {
            laser.setInactive();
            bloque.setInvisible();
            this.updateBitmap(false);
        }
        return bloque;
    }

    private void updateBitmap(Boolean random) {
        if (random) {
            for (int m = 0; m < numMarcianitos; m++) {
                int randomNumber = generator.nextInt(2);
                if (randomNumber == 1) {
                    marcianito[m].changeBitmap();
                }
            }
        } else {
            for (int v = 0; v < numMarcianitos; v++) {
                marcianito[v].changeBitmap();
            }
        }
        nave.changeBitmap();
        marcianitoEsp.changeBitmap();
    }

    private Bloque[] laserSimultaneousImpactBlocks(Bloque[] bloques, Laser[] lasers) {
        Boolean visibilityBlocks = (bloques[0].getVisibility()) && (bloques[1].getVisibility());
        Boolean laserIntersects = RectF.intersects(lasers[0].getRect(), bloques[0].getRect())
                && RectF.intersects(lasers[1].getRect(), bloques[1].getRect());

        if (visibilityBlocks && laserIntersects) {
            for (Laser simLaser : lasers) {
                simLaser.setInactive();
            }
            for (Bloque bloque : bloques) {
                bloque.setInvisible();
            }
            this.updateBitmap(true);
        }
        return bloques;
    }

    private void gameOver() {
        if (puntuacion == numMarcianitos * 100 || vidas == 0 || pierde || hack == 3) {
            mediaPlayer.pause();

            final Activity activity = (Activity) getContext();
            Intent intent = new Intent(activity, MenuActivity.class);
            intent.putExtra(getResources().getString(R.string.name), this.name);
            intent.putExtra(getResources().getString(R.string.victory), true);
            intent.putExtra(getResources().getString(R.string.score), puntuacion);
            intent.putExtra(ADULT, isAdult);
            intent.putExtra(REBOTE, rebotes);
            activity.finish();
            activity.startActivity(intent);
            Thread.currentThread().interrupt();
        }
    }

    private int checkIfKill(Laser laser, Marcianito marcianito, int newScore) {
        if (RectF.intersects(laser.getRect(), marcianito.getRect())) {
            laser.setInactive();
            marcianito.setInvisible();
            return newScore;
        }
        return 0;
    }

    private void playerHit(Laser laser) {
        if (laser.getStatus() && RectF.intersects(nave.getRect(), laser.getRect())) {
            laser.setInactive();
            vidas--;

            // Se acabó el juego
            gameOver();
        }
    }

    private void update() {

        // Mueve la nave espacial del jugador

        boolean tocaD = false;
        boolean tocaI = false;
        boolean tocaAB = false;
        boolean tocaAR = false;


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

        if (marcianitoEsp.getVisibility() && marcianitoEsp.takeAimEsp()) {
            espLaser.shoot(marcianitoEsp.getX() + marcianitoEsp.getLength() / 2,
                    marcianitoEsp.getY(), Laser.ABAJO);
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

            bumped = false;
        }

        // Desaparicion y aparicion aleatoria de nave
        nave = this.randomTeleportSpaceShip(nave);

        // Ha impactado la nave con la barrera o con un invader
        pierde = checkIfLost();

        // Ha impactado un marciano con la barrera
        for (int i = 0; i < numMarcianitos; i++) {
            if (marcianito[i].getVisibility()) {
                for (int j = 0; j < numBloque; j++) {
                    this.martianBarrierColision(bloques[j], marcianito[i]);
                }
            }
        }

        gameOver();

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
                for (EnemyLaser aMarcianitoLaser : marcianitoLaser) {
                    if ((aMarcianitoLaser.isLetal()) && (aMarcianitoLaser.getStatus())) {
                        for (int j = 0; j < numMarcianitos; j++) {
                            if (marcianito[j].getVisibility()) {
                                puntuacion += checkIfKill(aMarcianitoLaser, marcianito[j], 100);
                                puntuacion += checkIfKill(aMarcianitoLaser, marcianitoEsp, 0);
                                gameOver();

                            }
                        }
                    }
                }

                if ((espLaser.isLetal()) && (espLaser.getStatus())) {
                    for (int i = 0; i < numMarcianitos; i++) {
                        if (marcianito[i].getVisibility()) {
                            puntuacion += checkIfKill(espLaser, marcianito[i], 100);
                            puntuacion += checkIfKill(espLaser, marcianitoEsp, 0);
                            gameOver();

                        }
                    }
                }

            } else {
                // Ha tocado la parte alta de la pantalla la bala del jugador
                if (laser.getImpactPointY() < 0) {
                    laser.setInactive();
                }

                // Ha tocado la parte baja de la pantalla la bala del invader
                for (EnemyLaser aMarcianitoLaser : marcianitoLaser) {
                    if (aMarcianitoLaser.getImpactPointY() > ejeY) {
                        aMarcianitoLaser.setInactive();
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
                        puntuacion += checkIfKill(laser, marcianito[i], 100);

                        // Ha ganado el jugador
                        gameOver();
                    }
                }
            }

            // Ha tocado la bala del jugador al invader espontaneo
            if (laser.getStatus() && marcianitoEsp.getVisibility()) {
                puntuacion += checkIfKill(laser, marcianitoEsp, 0);
            }

            // Ha impactado una bala alienígena a un ladrillo de la guarida
            for (EnemyLaser aMarcianitoLaser2 : marcianitoLaser) {
                if (aMarcianitoLaser2.getStatus()) {
                    for (int j = 0; j < numBloque; j++) {
                        bloques[j] = this.laserImpactBlock(bloques[j], aMarcianitoLaser2);
                    }
                }

            }

            // Ha impactado una bala alienígena esponataneo a un ladrillo de la guarida
            if (espLaser.getStatus()) {
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
            for (EnemyLaser aMarcianitoLaser2 : marcianitoLaser) {
                if ((espLaser.getStatus()) && (aMarcianitoLaser2.getStatus())) {
                    for (int k = 0; k < numBloque; k++) {
                        for (int v = 0; v < numBloque; v++) {
                            Bloque[] twoBlocks = {bloques[k], bloques[v]};
                            Laser[] twoLasers = {espLaser, aMarcianitoLaser2};
                            laserSimultaneousImpactBlocks(twoBlocks, twoLasers);
                        }
                    }
                }
            }

            for (EnemyLaser aMarcianitoLaser1 : marcianitoLaser) {
                if ((laser.getStatus()) && (aMarcianitoLaser1.getStatus())) {
                    for (int k = 0; k < numBloque; k++) {
                        for (int v = 0; v < numBloque; v++) {
                            Bloque[] twoBlocks = {bloques[k], bloques[v]};
                            Laser[] twoLasers = {laser, aMarcianitoLaser1};
                            laserSimultaneousImpactBlocks(twoBlocks, twoLasers);
                        }
                    }
                }
            }

            if ((laser.getStatus()) && (espLaser.getStatus())) {
                for (int k = 0; k < numBloque; k++) {
                    for (int v = 0; v < numBloque; v++) {
                        Bloque[] twoBlocks = {bloques[k], bloques[v]};
                        Laser[] twoLasers = {laser, espLaser};
                        laserSimultaneousImpactBlocks(twoBlocks, twoLasers);
                    }
                }
            }

            // Ha impactado una bala de un invader a la nave espacial del jugador
            for (EnemyLaser aMarcianitoLaser : marcianitoLaser) {
                playerHit(aMarcianitoLaser);
            }

            // Ha impactado una bala del invader espontaneo a la nave espacial del jugador
            playerHit(espLaser);

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
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // El jugador ha tocado la pantalla

            pausado = false;


            //MIRAR JESUS!!!!!!!!!!!!!!!!!!!!!!!
            if ((motionEvent.getY() < ejeY) && (motionEvent.getX() > ejeX / 2) && laser.shoot(nave.getX() + nave.getLength() / 2, nave.getY(), Laser.ARRIBA)) {
                // Disparos lanzados
            }

            // Tocar marciano espontaneo tres veces para hack
            if ((motionEvent.getX() > marcianitoEsp.getX()) && (motionEvent.getX() < marcianitoEsp.getX() + marcianitoEsp.getLength()) &&
                    (motionEvent.getY() > marcianitoEsp.getY()) && (motionEvent.getY() < marcianitoEsp.getY() + marcianitoEsp.getHeight())) {
                hack++;
            }

            // Movimiento arriba
            if ((motionEvent.getX() > upButton.getX()) && (motionEvent.getX() < upButton.getX() + upButton.getLength()) &&
                    (motionEvent.getY() > upButton.getY()) && (motionEvent.getY() < upButton.getY() + upButton.getHeight())) {
                nave.setMovementState(Nave.UP);
            }
            // Movimiento abajo
            if ((motionEvent.getX() > downButton.getX()) && (motionEvent.getX() < downButton.getX() + downButton.getLength()) &&
                    (motionEvent.getY() > downButton.getY()) && (motionEvent.getY() < downButton.getY() + downButton.getHeight())) {
                nave.setMovementState(Nave.DOWN);
            }
            // Movimiento derecha
            if ((motionEvent.getX() > rightButton.getX()) && (motionEvent.getX() < rightButton.getX() + rightButton.getLength()) &&
                    (motionEvent.getY() > rightButton.getY()) && (motionEvent.getY() < rightButton.getY() + rightButton.getHeight())) {
                nave.setMovementState(Nave.RIGHT);
            }
            // Movimiento izquierda
            if ((motionEvent.getX() > leftButton.getX()) && (motionEvent.getX() < leftButton.getX() + leftButton.getLength()) &&
                    (motionEvent.getY() > leftButton.getY()) && (motionEvent.getY() < leftButton.getY() + leftButton.getHeight())) {
                nave.setMovementState(Nave.LEFT);
            }


            // El jugador ha retirado su dedo de la pantalla
        } else {
            if ((motionEvent.getX() < ejeX / 2)) {
                nave.setMovementState(Nave.STOPPED);
            }


        }
        return true;
    }
}
