package com.example.frameworkeducativoreto2grupo2.Metodos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;

public class Metodos {
    private static final String MAYUS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMS = "0123456789";
    private static final String CARACT_ESP = "!@#$%^&*()-_=+<>?";
    private static final String TODOS_CARACT = MAYUS + MINUS + NUMS + CARACT_ESP;
    private static final int LENGTH_CONTRASENA = 12; // Change the length as needed

    //comprobar campos vacios
    public boolean comprobarLoginCamposVacios(String user, String contrasena) {
        boolean esCorrecto;
        if (user.isEmpty() || contrasena.isEmpty()) {
            esCorrecto = false;
        } else {
            esCorrecto = true;
        }
        return esCorrecto;
    }

    //comprobar si el usuario esta vacio
    public boolean comprobarUserVacio(String user) {
        boolean esCorrecto;
        if (user.isEmpty()) {
            esCorrecto = false;
        } else {
            esCorrecto = true;
        }
        return esCorrecto;
    }

    //generar una nueva contraseña
    public static String generarNuevaContraseña() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        //asegurar minimo un caracter de cada
        password.append(MAYUS.charAt(random.nextInt(MAYUS.length())));
        password.append(MINUS.charAt(random.nextInt(MINUS.length())));
        password.append(NUMS.charAt(random.nextInt(NUMS.length())));
        password.append(CARACT_ESP.charAt(random.nextInt(CARACT_ESP.length())));

        //rellena el resto aleatoriamente
        for (int i = 4; i < LENGTH_CONTRASENA; i++) {
            password.append(TODOS_CARACT.charAt(random.nextInt(TODOS_CARACT.length())));
        }

        //shuffle a la contraseña para evitar patrones predecibles
        return shuffleContrasena(password.toString());
    }

    private static String shuffleContrasena(String contrasena) {
        char[] characters = contrasena.toCharArray();
        SecureRandom random = new SecureRandom();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

}
