package com.example.frameworkeducativoreto2grupo2.Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public boolean conectar(String IP, int puerto) {
        try {
            socket = new Socket(IP, puerto);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void desconectar() {
        try {
            if (socket != null) socket.close();
            if (dis != null) dis.close();
            if (dos != null) dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getInputStream() {
        return dis;
    }

    public DataOutputStream getOutputStream() {
        return dos;
    }

    public boolean isConectar() {
        return socket != null && socket.isConnected();
    }
}
