package com.example.frameworkeducativoreto2grupo2.Cliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    private static Cliente instance;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private static final String IP = "192.168.56.1";
    //private static final String IP = "10.5.13.59";
    //private static final String IP = "192.168.1.144"; //casa**borrar*
    private static final int PUERTO = 2845;

    private Cliente() throws IOException {
        socket = new Socket(IP, PUERTO);

        //objeto
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        //data
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public static synchronized Cliente getInstance() throws IOException {
        if (instance == null) {
            instance = new Cliente();
        }
        return instance;
    }

    //getters
    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    //desconectar
    public void close() throws IOException {
        if (objectInputStream != null) {
            objectInputStream.close();
        }
        if (objectOutputStream != null) {
            objectOutputStream.close();
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            instance = null;
        }
    }
}
