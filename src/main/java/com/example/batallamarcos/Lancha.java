package com.example.batallamarcos;

public class Lancha extends Barco{
    public Lancha(ControlJuego juego, String nombre, Equipo equipo, double x, double y) {
        super(juego, nombre, equipo, "/imagenes/lancha" + (equipo.equals(Equipo.ROJO) ?"roja" : "azul") + ".png" ,120, 6, x, y,
                700, 50, 120,60,150);
    }
}
