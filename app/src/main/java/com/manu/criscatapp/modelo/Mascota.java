package com.manu.criscatapp.modelo;

public class Mascota {
    private String id;
    private String imageURL;
    private String nombre;
    private int especie;
    private String raza;
    private int sexo;
    private int anioNacimiento;
    private String propietario;
    private String estado;

    @Override
    public String toString() {
        return "nombre='" + nombre + '\'' +
                ", imageURL=" + imageURL + '\'' +
                ", especie=" + especie +
                ", raza='" + raza + '\'' +
                ", sexo=" + sexo +
                ", anioNacimiento=" + anioNacimiento +
                ", propietario='" + propietario + '\'' +
                ", estado='" + estado + '\'';
    }

    public Mascota() {
    }

    public Mascota(String id, String imageURL, String nombre, int especie, String raza, int sexo, int anioNacimiento, String propietario, String estado) {
        this.id = id;
        this.imageURL = imageURL;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.anioNacimiento = anioNacimiento;
        this.propietario = propietario;
        this.estado = estado;
    }

    public Mascota(String propietario) {
        this.propietario = propietario;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEspecie() {
        return especie;
    }

    public void setEspecie(int especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
