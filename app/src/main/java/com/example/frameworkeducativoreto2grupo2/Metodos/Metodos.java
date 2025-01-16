package com.example.frameworkeducativoreto2grupo2.Metodos;

public class Metodos {

    //comprobar campos vacios
    public boolean comprobarLogin(String user, String contrasena) {
        boolean correcto;
        if(user.isEmpty() || contrasena.isEmpty()) {
            correcto = false;
        } else {
            correcto = true;
        }
        return correcto;
    }



}
