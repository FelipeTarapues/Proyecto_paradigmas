-- Datos iniciales del sistema experto

USE sistema_experto_medico;

-- Categorias
INSERT INTO categorias (nombre, descripcion) VALUES
('viral', 'Enfermedades causadas por virus'),
('cronica', 'Enfermedades de larga duracion'),
('alergia', 'Reacciones alergicas'),
('bacteriana', 'Enfermedades por bacterias');

-- Sintomas
INSERT INTO sintomas (nombre) VALUES
('fiebre'), ('tos'), ('dolor_cabeza'), ('dolor_muscular'),
('cansancio'), ('estornudos'), ('dolor_garganta'), ('sed'),
('perdida_peso'), ('perdida_gusto_olfato'), ('erupcion'), ('picazon'),
('nausea'), ('sensibilidad_luz'), ('ojos_lagrimosos'), ('aumento_peso'),
('piel_seca'), ('vomito'), ('diarrea'), ('dolor_abdominal');

-- Enfermedades
INSERT INTO enfermedades (nombre, id_categoria, recomendacion) VALUES
('Gripe', 1, 'Descansar, mantenerse hidratado y consultar al medico si persiste'),
('Resfriado', 1, 'Descansar y mantenerse hidratado'),
('Diabetes', 2, 'Controlar la dieta y consultar a un especialista'),
('COVID-19', 1, 'Aislamiento inmediato y consultar al medico'),
('Varicela', 1, 'Descansar y evitar rascar las lesiones'),
('Migrana', 2, 'Descansar en lugar oscuro y evitar la luz intensa'),
('Alergia', 3, 'Evitar alergenos y tomar antihistaminicos'),
('Hipotiroidismo', 2, 'Control medico regular y medicacion'),
('Gastroenteritis', 1, 'Mantener hidratacion y dieta ligera'),
('Faringitis', 4, 'Consultar al medico para tratamiento antibiotico');

-- Relacion enfermedad-sintomas
-- Gripe: fiebre, tos, dolor_cabeza, dolor_muscular
INSERT INTO enfermedad_sintoma VALUES (1, 1), (1, 2), (1, 3), (1, 4);

-- Resfriado: tos, estornudos, dolor_garganta
INSERT INTO enfermedad_sintoma VALUES (2, 2), (2, 6), (2, 7);

-- Diabetes: sed, cansancio, perdida_peso
INSERT INTO enfermedad_sintoma VALUES (3, 8), (3, 5), (3, 9);

-- COVID-19: fiebre, tos, cansancio, perdida_gusto_olfato
INSERT INTO enfermedad_sintoma VALUES (4, 1), (4, 2), (4, 5), (4, 10);

-- Varicela: fiebre, erupcion, picazon
INSERT INTO enfermedad_sintoma VALUES (5, 1), (5, 11), (5, 12);

-- Migrana: dolor_cabeza, nausea, sensibilidad_luz
INSERT INTO enfermedad_sintoma VALUES (6, 3), (6, 13), (6, 14);

-- Alergia: estornudos, picazon, ojos_lagrimosos
INSERT INTO enfermedad_sintoma VALUES (7, 6), (7, 12), (7, 15);

-- Hipotiroidismo: cansancio, aumento_peso, piel_seca
INSERT INTO enfermedad_sintoma VALUES (8, 5), (8, 16), (8, 17);

-- Gastroenteritis: vomito, diarrea, dolor_abdominal, fiebre
INSERT INTO enfermedad_sintoma VALUES (9, 18), (9, 19), (9, 20), (9, 1);

-- Faringitis: dolor_garganta, fiebre, tos
INSERT INTO enfermedad_sintoma VALUES (10, 7), (10, 1), (10, 2);

-- Verificar datos
SELECT * FROM v_enfermedades_completas;
