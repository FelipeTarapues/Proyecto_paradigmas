-- ============================================
-- SISTEMA EXPERTO DE DIAGNÓSTICO MÉDICO
-- Script de inserción de datos iniciales
-- ============================================

USE sistema_experto_medico;

-- ============================================
-- INSERTAR CATEGORÍAS
-- ============================================
INSERT INTO categorias (nombre, descripcion) VALUES
('viral', 'Enfermedades causadas por virus'),
('cronica', 'Enfermedades de larga duración o recurrentes'),
('alergia', 'Reacciones del sistema inmunológico a sustancias'),
('bacteriana', 'Enfermedades causadas por bacterias');

-- ============================================
-- INSERTAR SÍNTOMAS
-- ============================================
INSERT INTO sintomas (nombre, descripcion) VALUES
-- Síntomas generales
('fiebre', 'Temperatura corporal elevada'),
('tos', 'Expulsión brusca de aire de los pulmones'),
('dolor_cabeza', 'Dolor o molestia en la cabeza'),
('dolor_muscular', 'Dolor en los músculos del cuerpo'),
('cansancio', 'Sensación de fatiga o agotamiento'),
('estornudos', 'Expulsión súbita de aire por nariz y boca'),
('dolor_garganta', 'Dolor o irritación en la garganta'),

-- Síntomas específicos
('sed', 'Sensación de necesidad de beber líquidos'),
('perdida_peso', 'Disminución del peso corporal'),
('perdida_gusto_olfato', 'Pérdida del sentido del gusto y/o olfato'),
('erupcion', 'Aparición de manchas o lesiones en la piel'),
('picazon', 'Sensación de comezón en la piel'),
('nausea', 'Sensación de malestar estomacal'),
('sensibilidad_luz', 'Molestia ante la exposición a la luz'),
('ojos_lagrimosos', 'Lagrimeo excesivo de los ojos'),
('aumento_peso', 'Incremento del peso corporal'),
('piel_seca', 'Resequedad en la piel'),
('vomito', 'Expulsión del contenido gástrico'),
('diarrea', 'Evacuaciones líquidas frecuentes'),
('dolor_abdominal', 'Dolor en la zona del abdomen');

-- ============================================
-- INSERTAR ENFERMEDADES
-- ============================================

-- Gripe (viral)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Gripe', id_categoria, 'Descansar, mantenerse hidratado y consultar al médico si los síntomas persisten'
FROM categorias WHERE nombre = 'viral';

-- Resfriado (viral)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Resfriado', id_categoria, 'Descansar y mantenerse hidratado'
FROM categorias WHERE nombre = 'viral';

-- Diabetes (crónica)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Diabetes', id_categoria, 'Controlar la dieta y consultar a un especialista'
FROM categorias WHERE nombre = 'cronica';

-- COVID-19 (viral)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'COVID-19', id_categoria, 'Aislamiento inmediato y consultar al médico'
FROM categorias WHERE nombre = 'viral';

-- Varicela (viral)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Varicela', id_categoria, 'Descansar y evitar rascar las lesiones'
FROM categorias WHERE nombre = 'viral';

-- Migraña (crónica)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Migrana', id_categoria, 'Descansar en lugar oscuro y evitar la luz intensa'
FROM categorias WHERE nombre = 'cronica';

-- Alergia (alergia)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Alergia', id_categoria, 'Evitar alérgenos conocidos y tomar antihistamínicos'
FROM categorias WHERE nombre = 'alergia';

-- Hipotiroidismo (crónica)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Hipotiroidismo', id_categoria, 'Control médico regular y medicación según prescripción'
FROM categorias WHERE nombre = 'cronica';

-- Gastroenteritis (viral)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Gastroenteritis', id_categoria, 'Mantener hidratación y seguir dieta ligera'
FROM categorias WHERE nombre = 'viral';

-- Faringitis (bacteriana)
INSERT INTO enfermedades (nombre, id_categoria, recomendacion)
SELECT 'Faringitis', id_categoria, 'Consultar al médico para posible tratamiento antibiótico'
FROM categorias WHERE nombre = 'bacteriana';

-- ============================================
-- RELACIONAR ENFERMEDADES CON SÍNTOMAS
-- ============================================

-- Gripe: fiebre, tos, dolor_cabeza, dolor_muscular
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Gripe' AND s.nombre IN ('fiebre', 'tos', 'dolor_cabeza', 'dolor_muscular');

-- Resfriado: tos, estornudos, dolor_garganta
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Resfriado' AND s.nombre IN ('tos', 'estornudos', 'dolor_garganta');

-- Diabetes: sed, cansancio, perdida_peso
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Diabetes' AND s.nombre IN ('sed', 'cansancio', 'perdida_peso');

-- COVID-19: fiebre, tos, cansancio, perdida_gusto_olfato
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'COVID-19' AND s.nombre IN ('fiebre', 'tos', 'cansancio', 'perdida_gusto_olfato');

-- Varicela: fiebre, erupcion, picazon
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Varicela' AND s.nombre IN ('fiebre', 'erupcion', 'picazon');

-- Migraña: dolor_cabeza, nausea, sensibilidad_luz
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Migrana' AND s.nombre IN ('dolor_cabeza', 'nausea', 'sensibilidad_luz');

-- Alergia: estornudos, picazon, ojos_lagrimosos
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Alergia' AND s.nombre IN ('estornudos', 'picazon', 'ojos_lagrimosos');

-- Hipotiroidismo: cansancio, aumento_peso, piel_seca
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Hipotiroidismo' AND s.nombre IN ('cansancio', 'aumento_peso', 'piel_seca');

-- Gastroenteritis: vomito, diarrea, dolor_abdominal, fiebre
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Gastroenteritis' AND s.nombre IN ('vomito', 'diarrea', 'dolor_abdominal', 'fiebre');

-- Faringitis: dolor_garganta, fiebre, tos
INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma)
SELECT e.id_enfermedad, s.id_sintoma
FROM enfermedades e, sintomas s
WHERE e.nombre = 'Faringitis' AND s.nombre IN ('dolor_garganta', 'fiebre', 'tos');

-- ============================================
-- VERIFICAR DATOS INSERTADOS
-- ============================================
SELECT '=== CATEGORÍAS ===' AS '';
SELECT * FROM categorias;

SELECT '=== SÍNTOMAS ===' AS '';
SELECT * FROM sintomas;

SELECT '=== ENFERMEDADES ===' AS '';
SELECT e.nombre, c.nombre AS categoria, e.recomendacion 
FROM enfermedades e 
JOIN categorias c ON e.id_categoria = c.id_categoria;

SELECT '=== ENFERMEDADES CON SUS SÍNTOMAS ===' AS '';
SELECT * FROM vista_enfermedades_sintomas;

SELECT 'Datos iniciales insertados exitosamente!' AS mensaje;

