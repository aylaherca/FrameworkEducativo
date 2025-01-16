package com.example.frameworkeducativoreto2grupo2.Clases;

public enum TipoUsuario {
    ESTUDIANTE("Estudiante"),
    PROFESOR("Profesor");

    private final String tipoUser;

    //constructor
    TipoUsuario(String user) {
        this.tipoUser = user;
    }

    //getter
    public String getTipoUser() {
        return tipoUser;
    }
}
