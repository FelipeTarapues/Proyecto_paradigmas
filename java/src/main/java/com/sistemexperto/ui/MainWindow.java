package com.sistemexperto.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.sistemexperto.db.DatabaseConnection;
import com.sistemexperto.prolog.PrologEngine;

public class MainWindow extends JFrame {
    private DatabaseConnection dbConnection;
    private PrologEngine prologEngine;
    private DiagnosticoPanel diagnosticoPanel;
    private HistorialPanel historialPanel;

    public MainWindow() {
        setTitle("Sistema Experto Médico - Diagnóstico por Síntomas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Inicializar conexión a base de datos
        dbConnection = new DatabaseConnection();
        prologEngine = new PrologEngine();

        // Crear interfaz
        crearInterfaz();

        // Manejar cierre de aplicación
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dbConnection.desconectar();
                System.exit(0);
            }
        });

        setVisible(true);

        // Cargar datos después de mostrar la ventana
        cargarDatos();
    }

    private void crearInterfaz() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de diagnóstico
        diagnosticoPanel = new DiagnosticoPanel(dbConnection, prologEngine);
        tabbedPane.addTab("Nuevo Diagnóstico", diagnosticoPanel);

        // Panel de historial
        historialPanel = new HistorialPanel(dbConnection);
        tabbedPane.addTab("Historial", historialPanel);

        // Panel de información
        JPanel infoPanel = crearPanelInfo();
        tabbedPane.addTab("Información", infoPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        JLabel statusLabel = new JLabel("Iniciando conexion...");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (dbConnection.conectar()) {
            java.util.List<com.sistemexperto.models.Enfermedad> enfermedades = dbConnection.obtenerEnfermedades();
            java.util.List<String> sintomas = dbConnection.obtenerSintomas();

            prologEngine.cargarEnfermedades(enfermedades);
            prologEngine.cargarSintomas(sintomas);

            diagnosticoPanel.actualizarDatos();

            JLabel statusLabel = (JLabel) getContentPane().getComponent(1);
            statusLabel.setText("Conectado - " + enfermedades.size() + " enfermedades, " + 
                              sintomas.size() + " sintomas disponibles");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos MySQL.\n" +
                    "Asegurate de que MySQL esta ejecutandose y la BD existe.",
                    "Error de Conexion",
                    JOptionPane.ERROR_MESSAGE);

            JLabel statusLabel = (JLabel) getContentPane().getComponent(1);
            statusLabel.setText("Error de conexion");
        }
    }

    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setText(
                "SISTEMA EXPERTO MEDICO\n" +
                "=======================\n\n" +
                "Sistema de diagnostico medico usando Prolog y MySQL.\n\n" +
                "CARACTERISTICAS:\n" +
                "- Seleccionar sintomas\n" +
                "- Diagnosticar enfermedades\n" +
                "- Ver recomendaciones\n" +
                "- Historial de diagnosticos\n\n" +
                "TECNOLOGIAS:\n" +
                "- MySQL\n" +
                "- Prolog (SWI-Prolog)\n" +
                "- Java Swing\n\n" +
                "ADVERTENCIA: Sistema educativo. Consulta siempre con un medico profesional."
        );

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}
