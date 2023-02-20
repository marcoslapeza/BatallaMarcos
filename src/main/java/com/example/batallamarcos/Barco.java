package com.example.batallamarcos;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Random;

public class Barco  extends Thread{
    VBox vBox;
    private ProgressBar vidaRestante;
    private Label nombreVida;
    private ControlJuego juego;
    private String nombre;

    Equipo equipo;
    ImageView image;
    private double sonar;

    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY; 
    private double inclinacion = 45;
    double danno;
    double precision;
    double vida,vidaMaxima;

    long espera;
    public Barco(ControlJuego juego, String nombre, Equipo equipo,String rutaImg, int sonar, double velocidad, double x, double y,double vida, double danno,
                 double precision, int tamanno, long espera) {
        this.y = y;
        this.danno = danno;
        this.precision = precision;
        this.juego = juego;
        this.sonar = sonar;
        this.nombre = nombre;
        this.equipo = equipo;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.x = x;
        velocidadX = -velocidad;
        velocidadY = -velocidad;
        this.espera = espera;

        image = new ImageView(new Image(ControlJuego.class.getResource(rutaImg).toExternalForm()));
        image.setPreserveRatio(true);
        image.setFitWidth(tamanno);
        
        vBox = new VBox();
        vidaRestante = new ProgressBar();
        vidaRestante.setProgress(1);
        vidaRestante.setPrefHeight(10);
        vidaRestante.setPrefWidth(tamanno);
        vBox.getChildren().addAll(vidaRestante,image);
        vBox.setTranslateX(x);
        vBox.setTranslateY(y);
        vBox.setRotate(inclinacion);
    }

    private Thread recargar(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(espera);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    public synchronized boolean sigueConVida(){
        return vida > 0;
    }

    public static double distancia(double x1,double y1,double x2,double y2){
        double xDistancia = x2 -x1;
        double yDistancia = y2 - y1;
        return Math.sqrt(Math.pow(xDistancia,2) + Math.pow(yDistancia,2));
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                juego.getRoot().getChildren().addAll(vBox);
                moverBarco();
                detectar().start();
            }
        });
    }
    private synchronized void quitarVida(double danno){
        vida = vida - danno;
        if(vida < 1){
            vida = 0;
        }
    }
    private void moverBarco() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(40), event -> {
            setX(x+velocidadX);
            setY(y+velocidadY);
            vBox.setTranslateX(vBox.getTranslateX() + velocidadX);
            vBox.setTranslateY(vBox.getTranslateY() + velocidadY);

            Bounds bounds = vBox.getBoundsInParent();
            if (bounds.getMaxX() >= 1024 || bounds.getMinX() <= 0) {
                velocidadX = -velocidadX;
                if(generarNumAleatorio(0,1) == 1){
                    velocidadY = -velocidadY;
                    inclinacion = 180 + inclinacion;
                }else{
                    inclinacion = 180 - inclinacion;
                }
                vBox.setRotate(inclinacion);
            }
            if (bounds.getMaxY() >= 768 || bounds.getMinY() <= 0) {
                velocidadY = -velocidadY;
                if(generarNumAleatorio(0,1) == 1){
                    velocidadX = -velocidadX;
                    inclinacion = 180 + inclinacion;
                }else{
                    inclinacion = 360 - inclinacion;
                }
                vBox.setRotate(inclinacion);
            }
            if(sigueConVida()){
                moverBarco();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    private void disparar(Barco enemigo){
        Bala bala = new Bala(juego,this,enemigo);
        bala.start();
    }

    public synchronized void recibirDisparo( double danno){
        quitarVida(danno);
        vidaRestante.setProgress(vida/vidaMaxima);
    }

    private Thread detectar(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (sigueConVida() && juego.ganador() == null){
                    for (int i = 0; i < juego.getBarcos().size(); i++) {
                        Barco barco = juego.getBarcos().get(i);
                        if (distancia(getX(), getY(), barco.getX(), barco.getY()) < sonar &&
                                !nombre.equals(barco.nombre) && !equipo.equals(barco.equipo) && barco.sigueConVida()) {
                            disparar(barco);
                            Thread recargar = recargar();
                            recargar.start();
                            try {
                                recargar.join();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        image.setImage(new Image(ControlJuego.class.getResource("/imagenes/explosion.png").toExternalForm()));
                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        juego.getRoot().getChildren().remove(vBox);
                    }
                });
            }
        });
    }

    public synchronized double getX(){
        return x;
    }

    public synchronized double getY(){
        return y;
    }

    public synchronized void setX(double x){
        this.x = x;
    }

    public synchronized void setY(double y){
        this.y = y;
    }

    public static int generarNumAleatorio(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
