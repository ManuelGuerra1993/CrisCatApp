package com.manu.criscatapp.modelo;

public class Horario {
    String id;
    String horario;

    public Horario(String id, String horario) {
        this.id = id;
        this.horario = horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
