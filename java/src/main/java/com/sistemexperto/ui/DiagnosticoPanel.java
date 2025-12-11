package com.sistemexperto.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.sistemexperto.db.DatabaseConnection;
import com.sistemexperto.models.Enfermedad;
import com.sistemexperto.prolog.PrologEngine;

public class DiagnosticoPanel extends JPanel {
    private DatabaseConnection dbConnection;
    private PrologEngine prologEngine;

    private JTextField nombrePacienteField;
    private JSpinner edadSpinner;
    private JComboBox<String> categoriaCombo;
    private JList<String> sintomasDisponiblesList;
    private JList<String> sintomasSeleccionadosList;
    private JTextArea resultadoTextArea;
    private DefaultListModel<String> modeloDisponibles;
    private DefaultListModel<String> modeloSeleccionados;

    public DiagnosticoPanel(DatabaseConnection dbConnection, PrologEngine prologEngine) {
        this.dbConnection = dbConnection;
        this.prologEngine = prologEngine;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de entrada de paciente
        add(crearPanelPaciente(), BorderLayout.NORTH);

        // Panel central con síntomas
        add(crearPanelSintomas(), BorderLayout.CENTER);

        // Panel de resultados
        add(crearPanelResultados(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelPaciente() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));

        // Fila 1
        panel.add(new JLabel("Nombre:"));
        nombrePacienteField = new JTextField();
        panel.add(nombrePacienteField);
        panel.add(new JLabel("")); // Espacio vacío

        // Fila 2
        panel.add(new JLabel("Edad:"));
        edadSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 120, 1));
        panel.add(edadSpinner);
        
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.add(new JLabel("Filtrar por categoria:"));
        categoriaCombo = new JComboBox<>();
        categoriaCombo.addItem("Todas");
        filtroPanel.add(categoriaCombo);
        panel.add(filtroPanel);

        return panel;
    }

    private JPanel crearPanelSintomas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Selecciona Síntomas"));

        // Panel con dos listas
        JPanel listasPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        // Lista de síntomas disponibles
        JPanel dispPanel = new JPanel(new BorderLayout());
        dispPanel.setBorder(BorderFactory.createTitledBorder("Síntomas Disponibles"));
        modeloDisponibles = new DefaultListModel<>();
        sintomasDisponiblesList = new JList<>(modeloDisponibles);
        sintomasDisponiblesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dispPanel.add(new JScrollPane(sintomasDisponiblesList), BorderLayout.CENTER);
        listasPanel.add(dispPanel);

        // Botones de movimiento
        JPanel botonesPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnAgregar = new JButton("→ Agregar");
        btnAgregar.addActionListener(e -> agregarSintoma());
        JButton btnRemover = new JButton("← Remover");
        btnRemover.addActionListener(e -> removerSintoma());
        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnRemover);
        listasPanel.add(botonesPanel);

        // Lista de síntomas seleccionados
        JPanel selPanel = new JPanel(new BorderLayout());
        selPanel.setBorder(BorderFactory.createTitledBorder("Síntomas Seleccionados"));
        modeloSeleccionados = new DefaultListModel<>();
        sintomasSeleccionadosList = new JList<>(modeloSeleccionados);
        selPanel.add(new JScrollPane(sintomasSeleccionadosList), BorderLayout.CENTER);
        listasPanel.add(selPanel);

        panel.add(listasPanel, BorderLayout.CENTER);

        // Botón de diagnóstico
        JButton btnDiagnostico = new JButton("OBTENER DIAGNÓSTICO");
        btnDiagnostico.setFont(new Font("Arial", Font.BOLD, 12));
        btnDiagnostico.setBackground(new Color(34, 139, 34));
        btnDiagnostico.setForeground(Color.WHITE);
        btnDiagnostico.addActionListener(e -> realizarDiagnostico());
        panel.add(btnDiagnostico, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resultados del Diagnóstico"));

        resultadoTextArea = new JTextArea();
        resultadoTextArea.setEditable(false);
        resultadoTextArea.setLineWrap(true);
        resultadoTextArea.setWrapStyleWord(true);
        resultadoTextArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        resultadoTextArea.setText("Selecciona síntomas y haz clic en 'OBTENER DIAGNÓSTICO'");

        panel.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);

        // Botón de guardar
        JButton btnGuardar = new JButton("Guardar Diagnóstico");
        btnGuardar.addActionListener(e -> guardarDiagnostico());
        panel.add(btnGuardar, BorderLayout.SOUTH);

        panel.setPreferredSize(new Dimension(0, 150));
        return panel;
    }

    private void agregarSintoma() {
        int index = sintomasDisponiblesList.getSelectedIndex();
        if (index >= 0) {
            String sintoma = modeloDisponibles.getElementAt(index);
            modeloSeleccionados.addElement(sintoma);
            modeloDisponibles.remove(index);
        }
    }

    private void removerSintoma() {
        int index = sintomasSeleccionadosList.getSelectedIndex();
        if (index >= 0) {
            String sintoma = modeloSeleccionados.getElementAt(index);
            modeloDisponibles.addElement(sintoma);
            modeloSeleccionados.remove(index);
        }
    }

    private void realizarDiagnostico() {
        // Validar datos
        if (nombrePacienteField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa el nombre del paciente",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modeloSeleccionados.size() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona al menos un síntoma",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener síntomas seleccionados
        List<String> sintomasSeleccionados = new ArrayList<>();
        for (int i = 0; i < modeloSeleccionados.size(); i++) {
            sintomasSeleccionados.add(modeloSeleccionados.getElementAt(i));
        }

        String categoriaSeleccionada = (String) categoriaCombo.getSelectedItem();
        
        List<Enfermedad> enfermedades;
        if (categoriaSeleccionada == null || categoriaSeleccionada.equals("Todas")) {
            enfermedades = prologEngine.diagnostico(sintomasSeleccionados);
        } else {
            enfermedades = prologEngine.diagnosticoCategoria(sintomasSeleccionados, categoriaSeleccionada);
        }

        // Mostrar resultados
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== DIAGNOSTICO ===\n");
        resultado.append("Paciente: ").append(nombrePacienteField.getText()).append("\n");
        resultado.append("Edad: ").append(edadSpinner.getValue()).append(" anos\n");
        resultado.append("Sintomas: ").append(sintomasSeleccionados).append("\n");
        if (categoriaSeleccionada != null && !categoriaSeleccionada.equals("Todas")) {
            resultado.append("Categoria filtrada: ").append(categoriaSeleccionada).append("\n");
        }
        resultado.append("\n");

        if (enfermedades.isEmpty()) {
            resultado.append("No se encontraron enfermedades coincidentes.\n");
        } else {
            resultado.append("POSIBLES DIAGNÓSTICOS:\n");
            resultado.append("=====================\n\n");

            for (Enfermedad enf : enfermedades) {
                resultado.append("• ").append(enf.getNombre()).append(" (").append(enf.getCategoria()).append(")\n");
                resultado.append("  Síntomas: ").append(enf.getSintomas()).append("\n");
                resultado.append("  Recomendación: ").append(enf.getRecomendacion()).append("\n\n");
            }
        }

        resultado.append("ADVERTENCIA:\n");
        resultado.append("Este es un diagnóstico asistido. Consulta siempre con un médico profesional.");

        resultadoTextArea.setText(resultado.toString());
    }

    private void guardarDiagnostico() {
        if (resultadoTextArea.getText().equals("Selecciona síntomas y haz clic en 'OBTENER DIAGNÓSTICO'")) {
            JOptionPane.showMessageDialog(this, "Primero debes realizar un diagnóstico",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<String> sintomasSeleccionados = new ArrayList<>();
        for (int i = 0; i < modeloSeleccionados.size(); i++) {
            sintomasSeleccionados.add(modeloSeleccionados.getElementAt(i));
        }

        if (sintomasSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay síntomas seleccionados",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String categoriaSeleccionada = (String) categoriaCombo.getSelectedItem();
        List<Enfermedad> enfermedades;
        if (categoriaSeleccionada == null || categoriaSeleccionada.equals("Todas")) {
            enfermedades = prologEngine.diagnostico(sintomasSeleccionados);
        } else {
            enfermedades = prologEngine.diagnosticoCategoria(sintomasSeleccionados, categoriaSeleccionada);
        }
        
        if (enfermedades.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay diagnósticos para guardar",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String enfermedad = enfermedades.get(0).getNombre();
        String observaciones = "Síntomas: " + sintomasSeleccionados.toString();

        boolean guardado = dbConnection.registrarDiagnostico(
                nombrePacienteField.getText(),
                (int) edadSpinner.getValue(),
                sintomasSeleccionados,
                enfermedad,
                observaciones
        );

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Diagnóstico guardado exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el diagnóstico",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarDatos() {
        modeloDisponibles.clear();
        for (String sintoma : prologEngine.obtenerTodosSintomas()) {
            modeloDisponibles.addElement(sintoma);
        }
        
        if (dbConnection != null && dbConnection.isConnected()) {
            categoriaCombo.removeAllItems();
            categoriaCombo.addItem("Todas");
            for (String categoria : dbConnection.obtenerCategorias()) {
                categoriaCombo.addItem(categoria);
            }
        }
    }
}
