package com.example.batallamarcos;

public class Submarino extends Barco{
    public Submarino(ControlJuego controlJuego, String nombre, Equipo equipo, double x, double y) {
        super(controlJuego, nombre, equipo, "/imagenes/submarino" + (equipo.equals(Equipo.ROJO) ?"rojo" : "azul") + ".png" ,900, 2.5, x, y,
                600, 600, 120,120,4000);
    }
}
