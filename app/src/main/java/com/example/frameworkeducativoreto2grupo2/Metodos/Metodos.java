package com.example.frameworkeducativoreto2grupo2.Metodos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Metodos {

    private Socket cliente;

    public Metodos() {
        try {
            // Connect to the server
            cliente = new Socket("YOUR_SERVER_IP", 2845); // Replace "YOUR_SERVER_IP" with your server's IP address
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String login(String username, String password, String tipoUsuario) {
        try {
            if (cliente == null || !cliente.isConnected()) {
                return "No hay conexión con el servidor.";
            }

            // Send login details to the server
            DataOutputStream outputStream = new DataOutputStream(cliente.getOutputStream());
            DataInputStream inputStream = new DataInputStream(cliente.getInputStream());

            outputStream.writeInt(1); // Action code for login
            outputStream.writeUTF(username);
            outputStream.writeUTF(password);
            outputStream.writeUTF(tipoUsuario);
            outputStream.flush();

            // Read server response
            String status = inputStream.readUTF();
            if ("SUCCESS".equals(status)) {
                String responseUsername = inputStream.readUTF();
                String responseTipoUsuario = inputStream.readUTF();
                return "Welcome, " + responseUsername + "! Type: " + responseTipoUsuario;
            } else {
                String errorMessage = inputStream.readUTF();
                return "Login failed: " + errorMessage;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error durante el login.";
        }
    }

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
