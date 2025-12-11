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

    private JLabel statusLabel;

    private void crearInterfaz() {
        JTabbedPane tabbedPane = new JTabbedPane();

        diagnosticoPanel = new DiagnosticoPanel(dbConnection, prologEngine);
        tabbedPane.addTab("Nuevo Diagnóstico", diagnosticoPanel);

        historialPanel = new HistorialPanel(dbConnection);
        tabbedPane.addTab("Historial", historialPanel);

        add(tabbedPane, BorderLayout.CENTER);

        statusLabel = new JLabel("Iniciando conexion...");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (dbConnection.conectar()) {
            java.util.List<com.sistemexperto.models.Enfermedad> enfermedades = dbConnection.obtenerEnfermedades();
            java.util.List<String> sintomas = dbConnection.obtenerSintomas();

            prologEngine.cargarEnfermedades(enfermedades);
            prologEngine.cargarSintomas(sintomas);

            diagnosticoPanel.actualizarDatos();
            historialPanel.cargarHistorial();

            statusLabel.setText("Conectado - " + enfermedades.size() + " enfermedades, " + 
                              sintomas.size() + " sintomas disponibles");
        } else {
            String mensaje = "Error: No se pudo conectar a la base de datos MySQL.\n\n" +
                    "Verifica:\n" +
                    "1. MySQL esta ejecutandose\n" +
                    "2. La base de datos 'sistema_experto_medico' existe\n" +
                    "3. Usuario: root\n" +
                    "4. Contrasena correcta\n\n" +
                    "Revisa la consola para mas detalles del error.";
            
            JOptionPane.showMessageDialog(this, mensaje,
                    "Error de Conexion",
                    JOptionPane.ERROR_MESSAGE);

            statusLabel.setText("Error de conexion - Revisa la consola");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}
