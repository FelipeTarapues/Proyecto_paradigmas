package com.sistemexperto.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.sistemexperto.db.DatabaseConnection;
import com.sistemexperto.prolog.PrologEngine;

/**
 * Ventana principal de la aplicación
 */
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

    /**
     * Crea la interfaz principal con tabs
     */
    private void crearInterfaz() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de diagnóstico
        diagnosticoPanel = new DiagnosticoPanel(dbConnection, prologEngine);
        tabbedPane.addTab("Nuevo Diagnóstico", diagnosticoPanel);

        // Panel de historial
        historialPanel = new HistorialPanel();
        tabbedPane.addTab("Historial", historialPanel);

        // Panel de información
        JPanel infoPanel = crearPanelInfo();
        tabbedPane.addTab("Información", infoPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        JLabel statusLabel = new JLabel("Iniciando conexión...");
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Carga los datos desde la base de datos
     */
    private void cargarDatos() {
        if (dbConnection.conectar()) {
            var enfermedades = dbConnection.obtenerEnfermedades();
            var sintomas = dbConnection.obtenerSintomas();

            prologEngine.cargarEnfermedades(enfermedades);
            prologEngine.cargarSintomas(sintomas);

            diagnosticoPanel.actualizarDatos();

            JLabel statusLabel = (JLabel) getContentPane().getComponent(1);
            statusLabel.setText("✓ Conectado - " + enfermedades.size() + " enfermedades, " + 
                              sintomas.size() + " síntomas disponibles");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos MySQL.\n" +
                    "Asegúrate de que:\n" +
                    "1. MySQL está ejecutándose\n" +
                    "2. La base de datos 'sistema_experto_medico' existe\n" +
                    "3. El usuario es 'root' sin contraseña",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);

            JLabel statusLabel = (JLabel) getContentPane().getComponent(1);
            statusLabel.setText("✗ Error de conexión");
        }
    }

    /**
     * Crea el panel de información
     */
    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setText(
                "SISTEMA EXPERTO MÉDICO\n" +
                "=======================\n\n" +
                "Este sistema utiliza un motor de inferencia basado en Prolog para\n" +
                "diagnosticar enfermedades basándose en los síntomas ingresados.\n\n" +
                "CARACTERÍSTICAS:\n" +
                "- Selecciona tus síntomas de una lista predefinida\n" +
                "- El sistema identifica posibles enfermedades\n" +
                "- Proporciona recomendaciones médicas\n" +
                "- Registra todos los diagnósticos en la base de datos\n\n" +
                "TECNOLOGÍAS:\n" +
                "- Base de datos: MySQL\n" +
                "- Motor de inferencia: Prolog (SWI-Prolog)\n" +
                "- Interfaz: Java Swing\n\n" +
                "ADVERTENCIA:\n" +
                "Este sistema es educativo. SIEMPRE consulta con un médico\n" +
                "profesional para diagnósticos y tratamientos reales.\n\n" +
                "Desarrollado para: Proyecto Paradigmas de Programación"
        );

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 11));
            UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 11));
            new MainWindow();
        });
    }
}
