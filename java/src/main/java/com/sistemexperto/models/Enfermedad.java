package com.sistemexperto.models;

import java.util.List;

public class Enfermedad {
    private int idEnfermedad;
    private String nombre;
    private String categoria;
    private List<String> sintomas;
    private String recomendacion;

    public Enfermedad(String nombre, String categoria, List<String> sintomas, String recomendacion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.sintomas = sintomas;
        this.recomendacion = recomendacion;
    }

    public Enfermedad(int idEnfermedad, String nombre, String categoria, List<String> sintomas, String recomendacion) {
        this.idEnfermedad = idEnfermedad;
        this.nombre = nombre;
        this.categoria = categoria;
        this.sintomas = sintomas;
        this.recomendacion = recomendacion;
    }

    public int getIdEnfermedad() {
        return idEnfermedad;
    }

    public void setIdEnfermedad(int idEnfermedad) {
        this.idEnfermedad = idEnfermedad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ")";
    }
}
