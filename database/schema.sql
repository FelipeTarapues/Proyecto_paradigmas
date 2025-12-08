-- ============================================
-- SISTEMA EXPERTO DE DIAGNÓSTICO MÉDICO
-- Script de creación de base de datos MySQL
-- ============================================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS sistema_experto_medico
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE sistema_experto_medico;

-- ============================================
-- TABLA: categorias
-- Almacena las categorías de enfermedades
-- ============================================
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: sintomas
-- Almacena todos los síntomas disponibles
-- ============================================
CREATE TABLE IF NOT EXISTS sintomas (
    id_sintoma INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: enfermedades
-- Almacena las enfermedades con su categoría
-- ============================================
CREATE TABLE IF NOT EXISTS enfermedades (
    id_enfermedad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    id_categoria INT NOT NULL,
    recomendacion TEXT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- TABLA: enfermedad_sintoma
-- Relación muchos a muchos entre enfermedades y síntomas
-- ============================================
CREATE TABLE IF NOT EXISTS enfermedad_sintoma (
    id_enfermedad INT NOT NULL,
    id_sintoma INT NOT NULL,
    PRIMARY KEY (id_enfermedad, id_sintoma),
    FOREIGN KEY (id_enfermedad) REFERENCES enfermedades(id_enfermedad)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_sintoma) REFERENCES sintomas(id_sintoma)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- TABLA: pacientes
-- Almacena la información de los pacientes
-- ============================================
CREATE TABLE IF NOT EXISTS pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    edad INT NOT NULL CHECK (edad > 0 AND edad < 150),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- TABLA: diagnosticos
-- Historial de diagnósticos realizados
-- ============================================
CREATE TABLE IF NOT EXISTS diagnosticos (
    id_diagnostico INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_enfermedad INT NOT NULL,
    fecha_diagnostico DATETIME DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_enfermedad) REFERENCES enfermedades(id_enfermedad)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- TABLA: diagnostico_sintomas
-- Síntomas reportados en cada diagnóstico
-- ============================================
CREATE TABLE IF NOT EXISTS diagnostico_sintomas (
    id_diagnostico INT NOT NULL,
    id_sintoma INT NOT NULL,
    PRIMARY KEY (id_diagnostico, id_sintoma),
    FOREIGN KEY (id_diagnostico) REFERENCES diagnosticos(id_diagnostico)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_sintoma) REFERENCES sintomas(id_sintoma)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- ÍNDICES para optimizar consultas
-- ============================================
CREATE INDEX idx_enfermedad_categoria ON enfermedades(id_categoria);
CREATE INDEX idx_diagnostico_paciente ON diagnosticos(id_paciente);
CREATE INDEX idx_diagnostico_fecha ON diagnosticos(fecha_diagnostico);

-- ============================================
-- VISTAS útiles para reportes
-- ============================================

-- Vista: Enfermedades con sus síntomas (formato para Prolog)
CREATE OR REPLACE VIEW vista_enfermedades_sintomas AS
SELECT 
    e.id_enfermedad,
    e.nombre AS enfermedad,
    c.nombre AS categoria,
    e.recomendacion,
    GROUP_CONCAT(s.nombre ORDER BY s.nombre SEPARATOR ', ') AS sintomas
FROM enfermedades e
INNER JOIN categorias c ON e.id_categoria = c.id_categoria
INNER JOIN enfermedad_sintoma es ON e.id_enfermedad = es.id_enfermedad
INNER JOIN sintomas s ON es.id_sintoma = s.id_sintoma
GROUP BY e.id_enfermedad, e.nombre, c.nombre, e.recomendacion;

-- Vista: Estadísticas de diagnósticos por enfermedad
CREATE OR REPLACE VIEW vista_estadisticas_enfermedades AS
SELECT 
    e.nombre AS enfermedad,
    c.nombre AS categoria,
    COUNT(d.id_diagnostico) AS total_diagnosticos
FROM enfermedades e
INNER JOIN categorias c ON e.id_categoria = c.id_categoria
LEFT JOIN diagnosticos d ON e.id_enfermedad = d.id_enfermedad
GROUP BY e.id_enfermedad, e.nombre, c.nombre
ORDER BY total_diagnosticos DESC;

-- Vista: Síntomas más frecuentes
CREATE OR REPLACE VIEW vista_sintomas_frecuentes AS
SELECT 
    s.nombre AS sintoma,
    COUNT(ds.id_diagnostico) AS frecuencia
FROM sintomas s
LEFT JOIN diagnostico_sintomas ds ON s.id_sintoma = ds.id_sintoma
GROUP BY s.id_sintoma, s.nombre
ORDER BY frecuencia DESC;

DELIMITER //

-- ============================================
-- PROCEDIMIENTO: Obtener datos para Prolog
-- Retorna enfermedades con síntomas en formato lista
-- ============================================
CREATE PROCEDURE sp_obtener_enfermedades_prolog()
BEGIN
    SELECT 
        e.nombre AS enfermedad,
        c.nombre AS categoria,
        e.recomendacion,
        GROUP_CONCAT(s.nombre ORDER BY s.nombre SEPARATOR ',') AS sintomas_lista
    FROM enfermedades e
    INNER JOIN categorias c ON e.id_categoria = c.id_categoria
    INNER JOIN enfermedad_sintoma es ON e.id_enfermedad = es.id_enfermedad
    INNER JOIN sintomas s ON es.id_sintoma = s.id_sintoma
    GROUP BY e.id_enfermedad, e.nombre, c.nombre, e.recomendacion;
END //

-- ============================================
-- PROCEDIMIENTO: Registrar diagnóstico completo
-- ============================================
CREATE PROCEDURE sp_registrar_diagnostico(
    IN p_id_paciente INT,
    IN p_id_enfermedad INT,
    IN p_sintomas_ids VARCHAR(255),
    IN p_observaciones TEXT
)
BEGIN
    DECLARE v_id_diagnostico INT;
    
    -- Insertar el diagnóstico
    INSERT INTO diagnosticos (id_paciente, id_enfermedad, observaciones)
    VALUES (p_id_paciente, p_id_enfermedad, p_observaciones);
    
    SET v_id_diagnostico = LAST_INSERT_ID();
    
    -- Insertar los síntomas del diagnóstico
    -- p_sintomas_ids debe ser una cadena de IDs separados por coma: "1,2,3"
    SET @sql = CONCAT(
        'INSERT INTO diagnostico_sintomas (id_diagnostico, id_sintoma) ',
        'SELECT ', v_id_diagnostico, ', id_sintoma FROM sintomas WHERE id_sintoma IN (', p_sintomas_ids, ')'
    );
    
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
    SELECT v_id_diagnostico AS id_diagnostico_creado;
END //

DELIMITER ;

SELECT 'Esquema de base de datos creado exitosamente!' AS mensaje;

