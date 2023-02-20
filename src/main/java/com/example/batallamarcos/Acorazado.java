package com.example.batallamarcos;

public class Acorazado extends Barco{
    public Acorazado(ControlJuego controlJuego, String nombre, Equipo equipo, double x, double y) {
        super(controlJuego, nombre, equipo, "/imagenes/acorazado" + (equipo.equals(Equipo.ROJO) ?"rojo" : "azul") + ".png" ,200, 3.5, x, y,
                2000, 350, 85,220,2000);
    }
}

