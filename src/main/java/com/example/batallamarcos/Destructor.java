package com.example.batallamarcos;

public class Destructor extends Barco{
    public Destructor(ControlJuego controlJuego, String nombre, Equipo equipo, double x, double y) {
        super(controlJuego, nombre, equipo, "/imagenes/destructor" + (equipo.equals(Equipo.ROJO) ?"rojo" : "azul") + ".png" ,90, 4, x, y,
                1300, 145, 120,140,900);
    }
}
