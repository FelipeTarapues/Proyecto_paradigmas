package com.sistemexperto.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import com.sistemexperto.db.DatabaseConnection;

public class HistorialPanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private DatabaseConnection db;
    private JTextField buscarField;

    public HistorialPanel(DatabaseConnection db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con título y búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Historial de Diagnosticos");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(titulo, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.add(new JLabel("Buscar paciente:"));
        buscarField = new JTextField(15);
        panelBusqueda.add(buscarField);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPaciente());
        panelBusqueda.add(btnBuscar);
        JButton btnTodos = new JButton("Todos");
        btnTodos.addActionListener(e -> cargarHistorial());
        panelBusqueda.add(btnTodos);
        panelSuperior.add(panelBusqueda, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Paciente", "Edad", "Enfermedad", "Categoria", "Fecha", "Observaciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setFillsViewportHeight(true);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarHistorial());
        
        JButton btnEstadisticas = new JButton("Estadísticas");
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        
        JButton btnResumen = new JButton("Resumen Paciente");
        btnResumen.addActionListener(e -> mostrarResumen());
        
        JButton btnExportar = new JButton("Exportar CSV");
        btnExportar.addActionListener(e -> exportarCSV());

        panelBotones.add(btnActualizar);
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnResumen);
        panelBotones.add(btnExportar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarHistorial() {
        modeloTabla.setRowCount(0);
        
        if (db == null || !db.isConnected()) {
            return;
        }

        List<String[]> historial = db.obtenerHistorial();
        for (String[] fila : historial) {
            modeloTabla.addRow(fila);
        }

        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay diagnosticos registrados",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportarCSV() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte CSV");
        fileChooser.setSelectedFile(new java.io.File("reporte_diagnosticos.csv"));

        int resultado = fileChooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                // Encabezados
                pw.println("Paciente,Edad,Enfermedad,Categoria,Fecha,Observaciones");

                // Datos
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    StringBuilder linea = new StringBuilder();
                    for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                        Object valor = modeloTabla.getValueAt(i, j);
                        String texto = valor != null ? valor.toString().replace(",", ";") : "";
                        linea.append(texto);
                        if (j < modeloTabla.getColumnCount() - 1) {
                            linea.append(",");
                        }
                    }
                    pw.println(linea);
                }

                JOptionPane.showMessageDialog(this, 
                        "Archivo exportado: " + archivo.getAbsolutePath(),
                        "Exito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPaciente() {
        String nombre = buscarField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un nombre de paciente",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);
        if (db == null || !db.isConnected()) {
            return;
        }

        List<String[]> historial = db.obtenerHistorialPaciente(nombre);
        for (String[] fila : historial) {
            modeloTabla.addRow(fila);
        }

        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron diagnósticos para: " + nombre,
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarEstadisticas() {
        if (db == null || !db.isConnected()) {
            return;
        }

        List<String[]> estadisticas = db.obtenerEstadisticas();
        if (estadisticas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay estadísticas disponibles",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder texto = new StringBuilder();
        texto.append("=== ESTADÍSTICAS DE DIAGNÓSTICOS ===\n\n");
        texto.append("ENFERMEDADES MÁS COMUNES:\n");
        texto.append("==========================\n\n");

        for (String[] fila : estadisticas) {
            texto.append("• ").append(fila[0]).append(": ").append(fila[1]).append(" diagnóstico(s)\n");
        }

        JTextArea areaTexto = new JTextArea(texto.toString());
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scroll, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarResumen() {
        String nombre = buscarField.getText().trim();
        if (nombre.isEmpty()) {
            nombre = JOptionPane.showInputDialog(this, "Ingresa el nombre del paciente:",
                    "Resumen de Paciente", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null || nombre.trim().isEmpty()) {
                return;
            }
        }

        if (db == null || !db.isConnected()) {
            return;
        }

        String resumen = db.obtenerResumenPaciente(nombre);
        JTextArea areaTexto = new JTextArea(resumen);
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scroll, "Resumen: " + nombre, JOptionPane.INFORMATION_MESSAGE);
    }
}
