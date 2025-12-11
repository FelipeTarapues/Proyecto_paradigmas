package com.sistemexperto.db;

import java.sql.*;
import java.util.*;
import com.sistemexperto.models.Enfermedad;

public class DatabaseConnection {
    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DATABASE = System.getenv().getOrDefault("DB_DATABASE", "sistema_experto_medico");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "Metallicaseekn04");

    private Connection connection;

    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver MySQL no encontrado. Verifica que mysql-connector-java este en el classpath.");
            return false;
        }
        
        try {
            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
                    "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            connection = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("Conectado a MySQL correctamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL: " + e.getMessage());
            return false;
        }
    }

    public List<Enfermedad> obtenerEnfermedades() {
        List<Enfermedad> enfermedades = new ArrayList<>();
        String query = "SELECT enfermedad, categoria, recomendacion, sintomas FROM v_enfermedades_completas";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nombre = rs.getString("enfermedad");
                String categoria = rs.getString("categoria");
                String recomendacion = rs.getString("recomendacion");
                String sintomasStr = rs.getString("sintomas");

                // Convertir síntomas CSV a lista
                List<String> sintomas = Arrays.asList(sintomasStr.split(","));

                enfermedades.add(new Enfermedad(nombre, categoria, sintomas, recomendacion));
            }
            System.out.println("Se cargaron " + enfermedades.size() + " enfermedades");
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener enfermedades: " + e.getMessage());
        }
        return enfermedades;
    }

    public List<String> obtenerSintomas() {
        List<String> sintomas = new ArrayList<>();
        String query = "SELECT nombre FROM sintomas ORDER BY nombre";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sintomas.add(rs.getString("nombre"));
            }
            System.out.println("Se cargaron " + sintomas.size() + " sintomas");
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener síntomas: " + e.getMessage());
        }
        return sintomas;
    }

    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        String query = "SELECT nombre FROM categorias ORDER BY nombre";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener categorías: " + e.getMessage());
        }
        return categorias;
    }

    public boolean registrarDiagnostico(String nombrePaciente, int edad, List<String> sintomas,
            String enfermedad, String observaciones) {
        String queryPaciente = "INSERT INTO pacientes (nombre, edad) VALUES (?, ?)";
        String queryDiagnostico = "INSERT INTO diagnosticos (id_paciente, id_enfermedad, observaciones) VALUES (?, ?, ?)";
        String querySintomas = "INSERT INTO diagnostico_sintomas (id_diagnostico, id_sintoma) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            // Insertar paciente
            PreparedStatement stmtPaciente = connection.prepareStatement(queryPaciente,
                    Statement.RETURN_GENERATED_KEYS);
            stmtPaciente.setString(1, nombrePaciente);
            stmtPaciente.setInt(2, edad);
            stmtPaciente.executeUpdate();

            ResultSet rsPaciente = stmtPaciente.getGeneratedKeys();
            int idPaciente = 0;
            if (rsPaciente.next()) {
                idPaciente = rsPaciente.getInt(1);
            }

            // Obtener ID de enfermedad
            int idEnfermedad = obtenerIdEnfermedad(enfermedad);

            // Insertar diagnóstico
            PreparedStatement stmtDiagnostico = connection.prepareStatement(queryDiagnostico,
                    Statement.RETURN_GENERATED_KEYS);
            stmtDiagnostico.setInt(1, idPaciente);
            stmtDiagnostico.setInt(2, idEnfermedad);
            stmtDiagnostico.setString(3, observaciones);
            stmtDiagnostico.executeUpdate();

            ResultSet rsDiagnostico = stmtDiagnostico.getGeneratedKeys();
            int idDiagnostico = 0;
            if (rsDiagnostico.next()) {
                idDiagnostico = rsDiagnostico.getInt(1);
            }

            // Insertar síntomas del diagnóstico
            for (String sintoma : sintomas) {
                int idSintoma = obtenerIdSintoma(sintoma);
                PreparedStatement stmtSintomas = connection.prepareStatement(querySintomas);
                stmtSintomas.setInt(1, idDiagnostico);
                stmtSintomas.setInt(2, idSintoma);
                stmtSintomas.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Diagnostico registrado correctamente");
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.err.println("✗ Error al registrar diagnóstico: " + e.getMessage());
            return false;
        }
    }

    private int obtenerIdEnfermedad(String nombre) {
        String query = "SELECT id_enfermedad FROM enfermedades WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_enfermedad");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID enfermedad: " + e.getMessage());
        }
        return 0;
    }

    private int obtenerIdSintoma(String nombre) {
        String query = "SELECT id_sintoma FROM sintomas WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_sintoma");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID síntoma: " + e.getMessage());
        }
        return 0;
    }

    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Desconectado de MySQL");
            }
        } catch (SQLException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public List<String[]> obtenerHistorial() {
        List<String[]> historial = new ArrayList<>();
        String query = "SELECT p.nombre as paciente, p.edad, e.nombre as enfermedad, " +
                "c.nombre as categoria, d.fecha_diagnostico, d.observaciones " +
                "FROM diagnosticos d " +
                "JOIN pacientes p ON d.id_paciente = p.id_paciente " +
                "JOIN enfermedades e ON d.id_enfermedad = e.id_enfermedad " +
                "JOIN categorias c ON e.id_categoria = c.id_categoria " +
                "ORDER BY d.fecha_diagnostico DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] fila = new String[6];
                fila[0] = rs.getString("paciente");
                fila[1] = String.valueOf(rs.getInt("edad"));
                fila[2] = rs.getString("enfermedad");
                fila[3] = rs.getString("categoria");
                fila[4] = rs.getString("fecha_diagnostico");
                fila[5] = rs.getString("observaciones") != null ? rs.getString("observaciones") : "";
                historial.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }
        return historial;
    }
}
