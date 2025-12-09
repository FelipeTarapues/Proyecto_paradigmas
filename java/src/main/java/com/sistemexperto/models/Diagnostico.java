package com.sistemexperto.models;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de datos para un diagnóstico
 */
public class Diagnostico {
    private int idDiagnostico;
    private String nombrePaciente;
    private int edadPaciente;
    private List<String> sintomasIngresados;
    private List<String> enfermedadesDetectadas;
    private String recomendacion;
    private LocalDateTime fecha;

    public Diagnostico(String nombrePaciente, int edadPaciente, List<String> sintomasIngresados,
                       List<String> enfermedadesDetectadas, String recomendacion) {
        this.nombrePaciente = nombrePaciente;
        this.edadPaciente = edadPaciente;
        this.sintomasIngresados = sintomasIngresados;
        this.enfermedadesDetectadas = enfermedadesDetectadas;
        this.recomendacion = recomendacion;
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(int idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public int getEdadPaciente() {
        return edadPaciente;
    }

    public void setEdadPaciente(int edadPaciente) {
        this.edadPaciente = edadPaciente;
    }

    public List<String> getSintomasIngresados() {
        return sintomasIngresados;
    }

    public void setSintomasIngresados(List<String> sintomasIngresados) {
        this.sintomasIngresados = sintomasIngresados;
    }

    public List<String> getEnfermedadesDetectadas() {
        return enfermedadesDetectadas;
    }

    public void setEnfermedadesDetectadas(List<String> enfermedadesDetectadas) {
        this.enfermedadesDetectadas = enfermedadesDetectadas;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Diagnostico{" +
                "paciente='" + nombrePaciente + '\'' +
                ", edad=" + edadPaciente +
                ", enfermedades=" + enfermedadesDetectadas +
                ", fecha=" + fecha +
                '}';
    }
}
