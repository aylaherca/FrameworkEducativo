package Modelo;

import java.io.Serializable;

public class Colegio implements Serializable {

    private static final long serialVersionUID = 1L;
    private int idCentro;
    private String nombre;
    private String municipio;
    private double latitud;
    private double longitud;
    private String codenadaX;
    private String codenadaY;

    //constructores
    public Colegio() {
        // TODO Auto-generated constructor stub
    }

    public Colegio(int idCentro, String nombre, String municipio, double latitud, double longitud, String codenadaX,
                   String codenadaY) {
        super();
        this.idCentro = idCentro;
        this.nombre = nombre;
        this.municipio = municipio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.codenadaX = codenadaX;
        this.codenadaY = codenadaY;
    }

    //getters y setters
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        municipio = municipio;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCodenadaX() {
        return codenadaX;
    }

    public void setCodenadaX(String codenadaX) {
        this.codenadaX = codenadaX;
    }

    public String getCodenadaY() {
        return codenadaY;
    }

    public void setCodenadaY(String codenadaY) {
        this.codenadaY = codenadaY;
    }

}