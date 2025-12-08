% ============================================
% ARCHIVO DE PRUEBA - Sistema Experto
% Usa este archivo para probar las reglas antes de integrar con Java
% ============================================

% Cargar el motor de inferencia
:- consult('diagnostico.pl').

% --------------------------------------------
% DATOS DE PRUEBA (simulan lo que vendrá de MySQL)
% --------------------------------------------

% Cargar datos de prueba
cargar_datos_prueba :-
    % Limpiar datos anteriores
    limpiar_base_conocimiento,
    
    % Agregar síntomas
    agregar_sintoma(fiebre),
    agregar_sintoma(tos),
    agregar_sintoma(dolor_cabeza),
    agregar_sintoma(dolor_muscular),
    agregar_sintoma(cansancio),
    agregar_sintoma(estornudos),
    agregar_sintoma(dolor_garganta),
    agregar_sintoma(sed),
    agregar_sintoma(perdida_peso),
    agregar_sintoma(perdida_gusto_olfato),
    agregar_sintoma(erupcion),
    agregar_sintoma(picazon),
    agregar_sintoma(nausea),
    agregar_sintoma(sensibilidad_luz),
    agregar_sintoma(ojos_lagrimosos),
    agregar_sintoma(aumento_peso),
    agregar_sintoma(piel_seca),
    agregar_sintoma(vomito),
    agregar_sintoma(diarrea),
    agregar_sintoma(dolor_abdominal),
    
    % Agregar enfermedades
    agregar_enfermedad(gripe, viral, 
        [fiebre, tos, dolor_cabeza, dolor_muscular], 
        'Descansar, mantenerse hidratado y consultar al medico'),
    
    agregar_enfermedad(resfriado, viral, 
        [tos, estornudos, dolor_garganta], 
        'Descansar y mantenerse hidratado'),
    
    agregar_enfermedad(diabetes, cronica, 
        [sed, cansancio, perdida_peso], 
        'Controlar la dieta y consultar a un especialista'),
    
    agregar_enfermedad(covid19, viral, 
        [fiebre, tos, cansancio, perdida_gusto_olfato], 
        'Aislamiento inmediato y consultar al medico'),
    
    agregar_enfermedad(varicela, viral, 
        [fiebre, erupcion, picazon], 
        'Descansar y evitar rascar las lesiones'),
    
    agregar_enfermedad(migrana, cronica, 
        [dolor_cabeza, nausea, sensibilidad_luz], 
        'Descansar en lugar oscuro y evitar la luz intensa'),
    
    agregar_enfermedad(alergia, alergia, 
        [estornudos, picazon, ojos_lagrimosos], 
        'Evitar alergenos y tomar antihistaminicos'),
    
    agregar_enfermedad(hipotiroidismo, cronica, 
        [cansancio, aumento_peso, piel_seca], 
        'Control medico regular y medicacion'),
    
    agregar_enfermedad(gastroenteritis, viral, 
        [vomito, diarrea, dolor_abdominal, fiebre], 
        'Mantener hidratacion y dieta ligera'),
    
    agregar_enfermedad(faringitis, bacteriana, 
        [dolor_garganta, fiebre, tos], 
        'Consultar al medico para tratamiento antibiotico'),
    
    write('Datos de prueba cargados exitosamente!'), nl.

% --------------------------------------------
% PRUEBAS DE LAS REGLAS
% --------------------------------------------

% Ejecutar todas las pruebas
ejecutar_pruebas :-
    write('======================================'), nl,
    write('PRUEBAS DEL SISTEMA EXPERTO'), nl,
    write('======================================'), nl, nl,
    
    prueba_diagnostico,
    prueba_diagnostico_categoria,
    prueba_recomendacion,
    prueba_enfermedades_cronicas,
    prueba_enfermedades_por_sintoma,
    prueba_diagnostico_ordenado,
    
    write('======================================'), nl,
    write('TODAS LAS PRUEBAS COMPLETADAS'), nl,
    write('======================================'), nl.

% Prueba 1: Diagnóstico básico
prueba_diagnostico :-
    write('--- PRUEBA: diagnostico ---'), nl,
    write('Sintomas del paciente: [fiebre, tos]'), nl,
    write('Enfermedades posibles: '), nl,
    findall(E, diagnostico([fiebre, tos], E), Enfermedades),
    list_to_set(Enfermedades, EnfermedadesUnicas),
    write(EnfermedadesUnicas), nl, nl.

% Prueba 2: Diagnóstico por categoría
prueba_diagnostico_categoria :-
    write('--- PRUEBA: diagnostico_categoria ---'), nl,
    write('Sintomas: [cansancio, sed], Categoria: cronica'), nl,
    write('Enfermedades cronicas posibles: '), nl,
    findall(E, diagnostico_categoria([cansancio, sed], cronica, E), Enfermedades),
    list_to_set(Enfermedades, EnfermedadesUnicas),
    write(EnfermedadesUnicas), nl, nl.

% Prueba 3: Recomendación
prueba_recomendacion :-
    write('--- PRUEBA: recomendacion ---'), nl,
    write('Recomendacion para gripe: '), nl,
    recomendacion(gripe, R),
    write(R), nl, nl.

% Prueba 4: Enfermedades crónicas
prueba_enfermedades_cronicas :-
    write('--- PRUEBA: enfermedades_cronicas ---'), nl,
    write('Lista de enfermedades cronicas: '), nl,
    enfermedades_cronicas(Lista),
    write(Lista), nl, nl.

% Prueba 5: Enfermedades por síntoma
prueba_enfermedades_por_sintoma :-
    write('--- PRUEBA: enfermedades_por_sintoma ---'), nl,
    write('Enfermedades con sintoma "fiebre": '), nl,
    enfermedades_por_sintoma(fiebre, Lista),
    write(Lista), nl, nl.

% Prueba 6: Diagnóstico ordenado por coincidencias
prueba_diagnostico_ordenado :-
    write('--- PRUEBA: diagnostico_ordenado ---'), nl,
    write('Sintomas: [fiebre, tos, cansancio]'), nl,
    write('Diagnosticos ordenados por coincidencias: '), nl,
    diagnostico_ordenado([fiebre, tos, cansancio], Lista),
    imprimir_diagnosticos(Lista), nl.

% Auxiliar para imprimir diagnósticos
imprimir_diagnosticos([]).
imprimir_diagnosticos([(Cantidad, Enfermedad, Categoria, _)|Resto]) :-
    format('  ~w coincidencias: ~w (~w)~n', [Cantidad, Enfermedad, Categoria]),
    imprimir_diagnosticos(Resto).

% --------------------------------------------
% INSTRUCCIONES DE USO
% --------------------------------------------
% 
% 1. Abrir SWI-Prolog
% 2. Cargar este archivo: consult('test_diagnostico.pl').
% 3. Cargar datos de prueba: cargar_datos_prueba.
% 4. Ejecutar pruebas: ejecutar_pruebas.
%
% Consultas manuales de ejemplo:
%   - diagnostico([fiebre, tos], Enfermedad).
%   - recomendacion(gripe, R).
%   - enfermedades_cronicas(Lista).
%   - diagnostico_ordenado([fiebre, tos, dolor_cabeza], Resultado).
% --------------------------------------------

