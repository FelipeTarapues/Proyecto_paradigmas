package com.sistemexperto.models;

import java.util.List;

public class Enfermedad {
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

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ")";
    }
}
