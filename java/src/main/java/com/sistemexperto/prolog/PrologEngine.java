package com.sistemexperto.prolog;

import java.util.*;
import com.sistemexperto.models.Enfermedad;

/**
 * Clase para interactuar con el motor de Prolog
 * Simula la carga de enfermedades y síntomas en tiempo de ejecución
 */
public class PrologEngine {
    private List<Enfermedad> enfermedades;
    private List<String> sintomas;

    public PrologEngine() {
        this.enfermedades = new ArrayList<>();
        this.sintomas = new ArrayList<>();
    }

    /**
     * Carga las enfermedades desde la base de datos
     */
    public void cargarEnfermedades(List<Enfermedad> enfermedadesDB) {
        this.enfermedades = new ArrayList<>(enfermedadesDB);
        System.out.println("✓ Motor Prolog: " + enfermedades.size() + " enfermedades cargadas");
    }

    /**
     * Carga los síntomas disponibles
     */
    public void cargarSintomas(List<String> sintomasDB) {
        this.sintomas = new ArrayList<>(sintomasDB);
        System.out.println("✓ Motor Prolog: " + sintomas.size() + " síntomas cargados");
    }

    /**
     * Predicado: diagnostico(ListaSintomas, Enfermedades)
     * Encuentra enfermedades que coincidan con al menos un síntoma
     */
    public List<Enfermedad> diagnostico(List<String> sintomasUsuario) {
        List<Enfermedad> resultados = new ArrayList<>();

        for (Enfermedad enf : enfermedades) {
            // Verifica si hay al menos un síntoma en común
            for (String sintomaUsr : sintomasUsuario) {
                if (enf.getSintomas().contains(sintomaUsr)) {
                    resultados.add(enf);
                    break; // Evitar duplicados
                }
            }
        }
        return resultados;
    }

    /**
     * Predicado: diagnostico_categoria(ListaSintomas, Categoria, Enfermedades)
     * Filtra diagnósticos por categoría específica
     */
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

    /**
     * Predicado: enfermedades_cronicas(Lista)
     * Retorna todas las enfermedades crónicas
     */
    public List<Enfermedad> enfermedadesCronicas() {
        List<Enfermedad> resultados = new ArrayList<>();
        for (Enfermedad enf : enfermedades) {
            if (enf.getCategoria().equalsIgnoreCase("cronica")) {
                resultados.add(enf);
            }
        }
        return resultados;
    }

    /**
     * Predicado: enfermedades_por_sintoma(Sintoma, Lista)
     * Retorna todas las enfermedades que tienen un síntoma específico
     */
    public List<Enfermedad> enfermedadesPorSintoma(String sintoma) {
        List<Enfermedad> resultados = new ArrayList<>();
        for (Enfermedad enf : enfermedades) {
            if (enf.getSintomas().contains(sintoma)) {
                resultados.add(enf);
            }
        }
        return resultados;
    }

    /**
     * Predicado: recomendacion(Enfermedad, Recomendacion)
     * Obtiene la recomendación de una enfermedad específica
     */
    public String recomendacion(String nombreEnfermedad) {
        for (Enfermedad enf : enfermedades) {
            if (enf.getNombre().equalsIgnoreCase(nombreEnfermedad)) {
                return enf.getRecomendacion();
            }
        }
        return "No se encontró recomendación";
    }

    /**
     * Predicado: coincide_sintomas(SintomasUsr, SintomasEnf)
     * Verifica si el usuario tiene TODOS los síntomas de la enfermedad
     */
    public boolean coincideSintomas(List<String> sintomasUsr, List<String> sintomasEnf) {
        for (String sintoma : sintomasEnf) {
            if (!sintomasUsr.contains(sintoma)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene todas las enfermedades cargadas
     */
    public List<Enfermedad> obtenerTodasEnfermedades() {
        return new ArrayList<>(enfermedades);
    }

    /**
     * Obtiene todos los síntomas cargados
     */
    public List<String> obtenerTodosSintomas() {
        return new ArrayList<>(sintomas);
    }
}
