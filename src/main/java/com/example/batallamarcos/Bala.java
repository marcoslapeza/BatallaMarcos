package com.example.batallamarcos;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class Bala extends Thread{
    double xi;
    double yi;
    double xf;
    double yf;
    ControlJuego juego;
    ImageView image;
    private Barco obetivo;
    private Barco destino;

    public Bala(ControlJuego juego, Barco destino, Barco objetivo) {
        this.juego = juego;
        this.yi = destino.getY() + destino.vBox.getBoundsInLocal().getCenterY();
        this.xf = objetivo.getX() + objetivo.vBox.getBoundsInLocal().getCenterX();
        this.xi = destino.getX() + destino.vBox.getBoundsInLocal().getCenterX();
        this.yf = objetivo.getY() + objetivo.vBox.getBoundsInLocal().getCenterY();
        this.obetivo = objetivo;
        this.destino = destino;

        image = new ImageView(new Image(ControlJuego.class.getResource("/imagenes/misil.jpg").toExternalForm()));
        image.setPreserveRatio(true);
        image.setTranslateX(xi);
        image.setTranslateY(yi);
        image.setFitWidth(30);
    }
    public static int generarNumAleatorio(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                juego.getRoot().getChildren().addAll(image);
                moveLabel();
            }
        });
    }

    private void moveLabel() {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(image.translateXProperty(), xf);
        KeyValue keyValueY = new KeyValue(image.translateYProperty(), yf);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(90), keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(false);
        timeline.play();

        timeline.setOnFinished(event -> {
            juego.getRoot().getChildren().remove(image);
            double tramo1 = 25 * destino.precision/100;
            double tramo2 = 50 * destino.precision/100;
            double tramo3 = 75 * destino.precision/100;

            int aciertaDisparo = generarNumAleatorio(1,100);
            if(aciertaDisparo < tramo1){
                obetivo.recibirDisparo(destino.danno);
            }else if(aciertaDisparo < tramo2){
                obetivo.recibirDisparo(destino.danno*0.5);
            }else if(aciertaDisparo < tramo3) {
                obetivo.recibirDisparo(destino.danno*0.25);
            }
        });

    }
}
