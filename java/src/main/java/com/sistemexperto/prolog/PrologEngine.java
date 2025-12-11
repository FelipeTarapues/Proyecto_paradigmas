package com.sistemexperto.prolog;

import java.util.*;
import com.sistemexperto.models.Enfermedad;

public class PrologEngine {
    private List<Enfermedad> enfermedades;
    private List<String> sintomas;

    public PrologEngine() {
        this.enfermedades = new ArrayList<>();
        this.sintomas = new ArrayList<>();
    }

    public void cargarEnfermedades(List<Enfermedad> enfermedadesDB) {
        this.enfermedades = new ArrayList<>(enfermedadesDB);
        System.out.println("Motor Prolog: " + enfermedades.size() + " enfermedades cargadas");
    }

    public void cargarSintomas(List<String> sintomasDB) {
        this.sintomas = new ArrayList<>(sintomasDB);
        System.out.println("Motor Prolog: " + sintomas.size() + " sintomas cargados");
    }

    public List<Enfermedad> diagnostico(List<String> sintomasUsuario) {
        List<Enfermedad> resultados = new ArrayList<>();

        for (Enfermedad enf : enfermedades) {
            for (String sintomaUsr : sintomasUsuario) {
                if (enf.getSintomas().contains(sintomaUsr)) {
                    resultados.add(enf);
                    break;
                }
            }
        }
        return resultados;
    }

    public List<Enfermedad> diagnosticoCategoria(List<String> sintomasUsuario, String categoria) {
        List<Enfermedad> resultados = new ArrayList<>();

        for (Enfermedad enf : enfermedades) {
            if (enf.getCategoria().equalsIgnoreCase(categoria)) {
                for (String sintomaUsr : sintomasUsuario) {
                    if (enf.getSintomas().contains(sintomaUsr)) {
                        resultados.add(enf);
                        break;
                    }
                }
            }
        }
        return resultados;
    }

    public List<Enfermedad> enfermedadesCronicas() {
        List<Enfermedad> resultados = new ArrayList<>();
        for (Enfermedad enf : enfermedades) {
            if (enf.getCategoria().equalsIgnoreCase("cronica")) {
                resultados.add(enf);
            }
        }
        return resultados;
    }

    public List<Enfermedad> enfermedadesPorSintoma(String sintoma) {
        List<Enfermedad> resultados = new ArrayList<>();
        for (Enfermedad enf : enfermedades) {
            if (enf.getSintomas().contains(sintoma)) {
                resultados.add(enf);
            }
        }
        return resultados;
    }

    public String recomendacion(String nombreEnfermedad) {
        for (Enfermedad enf : enfermedades) {
            if (enf.getNombre().equalsIgnoreCase(nombreEnfermedad)) {
                return enf.getRecomendacion();
            }
        }
        return "No se encontro recomendacion";
    }

    public boolean coincideSintomas(List<String> sintomasUsr, List<String> sintomasEnf) {
        for (String sintoma : sintomasEnf) {
            if (!sintomasUsr.contains(sintoma)) {
                return false;
            }
        }
        return true;
    }

    public List<Enfermedad> obtenerTodasEnfermedades() {
        return new ArrayList<>(enfermedades);
    }

    public List<String> obtenerTodosSintomas() {
        return new ArrayList<>(sintomas);
    }
}
