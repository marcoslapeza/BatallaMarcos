package com.example.batallamarcos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class ControlJuego extends Application {


    public ArrayList<Barco> barcos;

    private Equipo ganador;
    private AnchorPane root;

    public static void main(String[] args) {
        launch(args);
    }

    public synchronized ArrayList<Barco> getBarcos(){
        return barcos;
    }

    public synchronized Equipo ganador(){
        return ganador;
    }
    public synchronized void  setGanador(Equipo equipo){
        ganador = equipo;
    }


    @Override
    public void start(Stage primaryStage) {
        root = new AnchorPane();

        Image image = new Image(ControlJuego.class.getResource("/imagenes/fondo.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(0, 0, true, true, true, true));
        root.setBackground(new Background(backgroundImage));

        Button button = new Button("¡Que comience la batalla!");
        button.setPrefSize(150, 50);
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(Color.PURPLE, new CornerRadii(10), Insets.EMPTY)));
        button.setFont(Font.font(30));
        AnchorPane.setTopAnchor(button, 30d);
        AnchorPane.setLeftAnchor(button, (root.getWidth() - button.getWidth()) / 2);
        AnchorPane.setRightAnchor(button, (root.getWidth() - button.getWidth()) / 2);
        button.setCursor(Cursor.HAND);

        barcos = new ArrayList<>();
        ganador = null;

        // Barcos rojos
        Destructor destructorRojo = new Destructor(this, "destructorRojo", Equipo.ROJO, 800, 100);
        Lancha lanchaRoja = new Lancha(this, "lanchaRoja", Equipo.ROJO, 800, 250);
        Lancha lanchaRoja2 = new Lancha(this, "lanchaRoja2", Equipo.ROJO, 800, 400);
        Acorazado acorazadoRojo = new Acorazado(this, "acorazadoRojo", Equipo.ROJO, 800, 650);
        Submarino submarinoRojo = new Submarino(this, "submarinoRojo", Equipo.ROJO, 800, 700);

        barcos.add(acorazadoRojo);
        barcos.add(lanchaRoja);
        barcos.add(lanchaRoja2);
        barcos.add(destructorRojo);
        barcos.add(submarinoRojo);

        // Barcos azules
        Destructor destructorAzul = new Destructor(this, "destructorAzul", Equipo.AZUL, 100, 100);
        Lancha lanchaAzul = new Lancha(this, "lanchaAzul", Equipo.AZUL, 100, 250);
        Lancha lanchaAzul2 = new Lancha(this, "lanchaAzul2", Equipo.AZUL, 100, 400);
        Acorazado acorazadoAzul = new Acorazado(this, "acorazadoAzul", Equipo.AZUL, 100, 650);
        Submarino submarinoAzul = new Submarino(this, "submarinoAzul", Equipo.AZUL, 100, 600);

        barcos.add(acorazadoAzul);
        barcos.add(lanchaAzul);
        barcos.add(lanchaAzul2);
        barcos.add(destructorAzul);
        barcos.add(submarinoAzul);



        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < barcos.size(); i++) {
                    Barco b = barcos.get(i);
                    b.start();
                }
                root.getChildren().remove(button);
            }
        });


        root.getChildren().addAll(button);
        primaryStage.setTitle("Batalla Marcos");
        primaryStage.setScene(new Scene(root, 1024, 800));
        primaryStage.show();
        new Thread(new Runnable() {
            int barcosRojos, barcosAzules;
            @Override
            public void run() {

                Equipo ganador = ganador();
                while(ganador == null) {
                    int barcosRojos = 0;
                    int barcosAzules = 0;
                    for(int i = 0; i < getBarcos().size(); i++) {
                        Barco b = getBarcos().get(i);
                        if(b.equipo.equals(Equipo.ROJO) && b.sigueConVida()) {
                            barcosRojos++;
                        }
                        if(b.equipo.equals(Equipo.AZUL) && b.sigueConVida()) {
                            barcosAzules++;
                        }
                    }
                    if(barcosRojos == 0) {
                        setGanador(Equipo.AZUL);
                    }
                    if(barcosAzules == 0) {
                        setGanador(Equipo.ROJO);
                    }
                    ganador = ganador();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // Crear una nueva ventana
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initOwner(root.getScene().getWindow());
                        stage.setResizable(false);

                        // Crear un layout para la ventana
                        VBox layout = new VBox();
                        layout.setPadding(new Insets(20));
                        layout.setSpacing(20);
                        layout.setAlignment(Pos.CENTER);

                        // Crear etiquetas para el encabezado y el contenido de la ventana
                        Label header = new Label("EL GANADOR HA SIDO:");
                        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                        Label content = new Label("El equipo: " + ganador().toString());
                        content.setFont(Font.font("Arial", 16));

                        // Agregar los elementos al layout
                        layout.getChildren().addAll(header, content);

                        // Crear un botón para cerrar la ventana
                        Button closeButton = new Button("Cerrar");
                        closeButton.setOnAction(e -> stage.close());

                        // Agregar el botón al layout
                        layout.getChildren().add(closeButton);

                        // Crear un nuevo Scene y asignarlo a la ventana
                        Scene scene = new Scene(layout);
                        stage.setScene(scene);

                        // Establecer el fondo de pantalla y colores personalizados
                        scene.getRoot().setStyle("-fx-background-color: #3c3f41; -fx-text-fill: white;");

                        // Mostrar la ventana emergente
                        stage.showAndWait();

                    }
                });

            }
        }).start();
    }


    public AnchorPane getRoot(){
        return root;
    }

}