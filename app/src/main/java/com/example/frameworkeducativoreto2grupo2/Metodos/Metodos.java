package com.example.frameworkeducativoreto2grupo2.Metodos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Metodos {

    //comprobar campos vacios
    public boolean comprobarLoginCamposVacios(String user, String contrasena) {
        boolean esCorrecto;
        if(user.isEmpty() || contrasena.isEmpty()) {
            esCorrecto = false;
        } else {
            esCorrecto = true;
        }
        return esCorrecto;
    }





}
