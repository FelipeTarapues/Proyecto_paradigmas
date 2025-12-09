package com.sistemexperto.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para visualizar el historial de diagnósticos
 */
public class HistorialPanel extends JPanel {

    public HistorialPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear tabla de historial (por implementar con datos reales de BD)
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 11));
        textArea.setText(
                "HISTORIAL DE DIAGNÓSTICOS\n" +
                "==========================\n\n" +
                "Esta sección mostrará todos los diagnósticos guardados.\n\n" +
                "Funcionalidades:\n" +
                "- Ver todos los diagnósticos previos\n" +
                "- Filtrar por paciente\n" +
                "- Filtrar por fecha\n" +
                "- Filtrar por enfermedad\n" +
                "- Exportar datos a CSV\n\n" +
                "La funcionalidad completa será implementada en la siguiente versión."
        );

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}
