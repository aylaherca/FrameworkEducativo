package Modelo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Reuniones implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idReunion;
    private Users usersByProfesorId;
    private Users usersByAlumnoId;
    private String estado;
    private String estadoEus;
    private String idCentro;
    private String titulo;
    private String asunto;
    private String aula;
    private Timestamp fecha;

    public Reuniones() {
    }

    public Reuniones(Users usersByProfesorId, Users usersByAlumnoId) {
        this.usersByProfesorId = usersByProfesorId;
        this.usersByAlumnoId = usersByAlumnoId;
    }

    public Reuniones(Users usersByProfesorId, Users usersByAlumnoId, String estado, String estadoEus, String idCentro,
                     String titulo, String asunto, String aula, Timestamp fecha) {
        this.usersByProfesorId = usersByProfesorId;
        this.usersByAlumnoId = usersByAlumnoId;
        this.estado = estado;
        this.estadoEus = estadoEus;
        this.idCentro = idCentro;
        this.titulo = titulo;
        this.asunto = asunto;
        this.aula = aula;
        this.fecha = fecha;
    }

    public Integer getIdReunion() {
        return this.idReunion;
    }

    public void setIdReunion(Integer idReunion) {
        this.idReunion = idReunion;
    }

    public Users getUsersByProfesorId() {
        return this.usersByProfesorId;
    }

    public void setUsersByProfesorId(Users usersByProfesorId) {
        this.usersByProfesorId = usersByProfesorId;
    }

    public Users getUsersByAlumnoId() {
        return this.usersByAlumnoId;
    }

    public void setUsersByAlumnoId(Users usersByAlumnoId) {
        this.usersByAlumnoId = usersByAlumnoId;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoEus() {
        return this.estadoEus;
    }

    public void setEstadoEus(String estadoEus) {
        this.estadoEus = estadoEus;
    }

    public String getIdCentro() {
        return this.idCentro;
    }

    public void setIdCentro(String idCentro) {
        this.idCentro = idCentro;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAsunto() {
        return this.asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAula() {
        return this.aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public Timestamp getFecha() {
        return this.fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
