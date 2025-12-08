% Pruebas del motor de inferencia

:- consult('diagnostico.pl').

% Carga los datos de prueba (simula lo que haria Java desde MySQL)
cargar_datos_prueba :-
    limpiar_hechos,
    
    % sintomas
    agregar_sintoma(fiebre), agregar_sintoma(tos),
    agregar_sintoma(dolor_cabeza), agregar_sintoma(dolor_muscular),
    agregar_sintoma(cansancio), agregar_sintoma(estornudos),
    agregar_sintoma(dolor_garganta), agregar_sintoma(sed),
    agregar_sintoma(perdida_peso), agregar_sintoma(perdida_gusto_olfato),
    agregar_sintoma(erupcion), agregar_sintoma(picazon),
    agregar_sintoma(nausea), agregar_sintoma(sensibilidad_luz),
    agregar_sintoma(ojos_lagrimosos), agregar_sintoma(aumento_peso),
    agregar_sintoma(piel_seca), agregar_sintoma(vomito),
    agregar_sintoma(diarrea), agregar_sintoma(dolor_abdominal),
    
    % enfermedades con sus sintomas
    agregar_enfermedad(gripe, viral, [fiebre, tos, dolor_cabeza, dolor_muscular], 
        'Descansar, mantenerse hidratado y consultar al medico'),
    agregar_enfermedad(resfriado, viral, [tos, estornudos, dolor_garganta], 
        'Descansar y mantenerse hidratado'),
    agregar_enfermedad(diabetes, cronica, [sed, cansancio, perdida_peso], 
        'Controlar la dieta y consultar a un especialista'),
    agregar_enfermedad(covid19, viral, [fiebre, tos, cansancio, perdida_gusto_olfato], 
        'Aislamiento inmediato y consultar al medico'),
    agregar_enfermedad(varicela, viral, [fiebre, erupcion, picazon], 
        'Descansar y evitar rascar las lesiones'),
    agregar_enfermedad(migrana, cronica, [dolor_cabeza, nausea, sensibilidad_luz], 
        'Descansar en lugar oscuro y evitar la luz intensa'),
    agregar_enfermedad(alergia, alergia, [estornudos, picazon, ojos_lagrimosos], 
        'Evitar alergenos y tomar antihistaminicos'),
    agregar_enfermedad(hipotiroidismo, cronica, [cansancio, aumento_peso, piel_seca], 
        'Control medico regular y medicacion'),
    agregar_enfermedad(gastroenteritis, viral, [vomito, diarrea, dolor_abdominal, fiebre], 
        'Mantener hidratacion y dieta ligera'),
    agregar_enfermedad(faringitis, bacteriana, [dolor_garganta, fiebre, tos], 
        'Consultar al medico para tratamiento antibiotico'),
    
    write('Datos cargados!'), nl.

% Correr todas las pruebas
ejecutar_pruebas :-
    nl, write('=== PRUEBAS ==='), nl, nl,
    
    % prueba diagnostico
    write('1. diagnostico([fiebre, tos], E):'), nl,
    findall(E, diagnostico([fiebre, tos], E), R1),
    list_to_set(R1, R1u), write('   '), write(R1u), nl, nl,
    
    % prueba diagnostico_categoria
    write('2. diagnostico_categoria([cansancio], cronica, E):'), nl,
    findall(E, diagnostico_categoria([cansancio], cronica, E), R2),
    list_to_set(R2, R2u), write('   '), write(R2u), nl, nl,
    
    % prueba recomendacion
    write('3. recomendacion(gripe, R):'), nl,
    recomendacion(gripe, R3), write('   '), write(R3), nl, nl,
    
    % prueba enfermedades_cronicas
    write('4. enfermedades_cronicas(L):'), nl,
    enfermedades_cronicas(R4), write('   '), write(R4), nl, nl,
    
    % prueba enfermedades_por_sintoma
    write('5. enfermedades_por_sintoma(fiebre, L):'), nl,
    enfermedades_por_sintoma(fiebre, R5), write('   '), write(R5), nl, nl,
    
    % prueba coincide_sintomas
    write('6. coincide_sintomas([fiebre, tos], [fiebre, tos, dolor_cabeza]):'), nl,
    (coincide_sintomas([fiebre, tos], [fiebre, tos, dolor_cabeza]) 
        -> write('   true') ; write('   false')), nl, nl,
    
    write('=== OK ==='), nl.

% Para ejecutar: cargar_datos_prueba, ejecutar_pruebas.
