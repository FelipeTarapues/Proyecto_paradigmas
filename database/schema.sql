-- Base de datos para el sistema experto medico

CREATE DATABASE IF NOT EXISTS sistema_experto_medico
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sistema_experto_medico;

-- Categorias de enfermedades
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- Catalogo de sintomas
CREATE TABLE IF NOT EXISTS sintomas (
    id_sintoma INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- Enfermedades
CREATE TABLE IF NOT EXISTS enfermedades (
    id_enfermedad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    id_categoria INT NOT NULL,
    recomendacion TEXT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

-- Relacion enfermedades-sintomas (N:M)
CREATE TABLE IF NOT EXISTS enfermedad_sintoma (
    id_enfermedad INT NOT NULL,
    id_sintoma INT NOT NULL,
    PRIMARY KEY (id_enfermedad, id_sintoma),
    FOREIGN KEY (id_enfermedad) REFERENCES enfermedades(id_enfermedad),
    FOREIGN KEY (id_sintoma) REFERENCES sintomas(id_sintoma)
);

-- Pacientes registrados
CREATE TABLE IF NOT EXISTS pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    edad INT NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Historial de diagnosticos
CREATE TABLE IF NOT EXISTS diagnosticos (
    id_diagnostico INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_enfermedad INT NOT NULL,
    fecha_diagnostico DATETIME DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (id_enfermedad) REFERENCES enfermedades(id_enfermedad)
);

-- Sintomas de cada diagnostico
CREATE TABLE IF NOT EXISTS diagnostico_sintomas (
    id_diagnostico INT NOT NULL,
    id_sintoma INT NOT NULL,
    PRIMARY KEY (id_diagnostico, id_sintoma),
    FOREIGN KEY (id_diagnostico) REFERENCES diagnosticos(id_diagnostico),
    FOREIGN KEY (id_sintoma) REFERENCES sintomas(id_sintoma)
);

-- Vista para obtener enfermedades con sintomas (para cargar en Prolog)
CREATE OR REPLACE VIEW v_enfermedades_completas AS
SELECT 
    e.nombre AS enfermedad,
    c.nombre AS categoria,
    e.recomendacion,
    GROUP_CONCAT(s.nombre SEPARATOR ',') AS sintomas
FROM enfermedades e
JOIN categorias c ON e.id_categoria = c.id_categoria
JOIN enfermedad_sintoma es ON e.id_enfermedad = es.id_enfermedad
JOIN sintomas s ON es.id_sintoma = s.id_sintoma
GROUP BY e.id_enfermedad;

-- Vista para estadisticas
CREATE OR REPLACE VIEW v_estadisticas AS
SELECT 
    e.nombre AS enfermedad,
    COUNT(d.id_diagnostico) AS total_diagnosticos
FROM enfermedades e
LEFT JOIN diagnosticos d ON e.id_enfermedad = d.id_enfermedad
GROUP BY e.id_enfermedad
ORDER BY total_diagnosticos DESC;
