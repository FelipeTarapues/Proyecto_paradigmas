% Motor de inferencia - Sistema experto medico
% Los hechos se cargan dinamicamente desde Java (MySQL)

:- dynamic enfermedad/4.   % enfermedad(Nombre, Categoria, ListaSintomas, Recomendacion)
:- dynamic sintoma/1.      % sintoma(Nombre)
:- dynamic paciente/2.     % paciente(Nombre, Edad)

% coincide_sintomas: verifica si todos los sintomas del usuario estan en la enfermedad
coincide_sintomas([], _).
coincide_sintomas([S|Resto], SintomasEnf) :-
    member(S, SintomasEnf),
    coincide_sintomas(Resto, SintomasEnf).

% diagnostico: devuelve enfermedad que coincide con al menos un sintoma
diagnostico(SintomasUsr, Enf) :-
    enfermedad(Enf, _, SintomasEnf, _),
    member(S, SintomasUsr),
    member(S, SintomasEnf).

% diagnostico_categoria: filtra por categoria
diagnostico_categoria(SintomasUsr, Cat, Enf) :-
    enfermedad(Enf, Cat, SintomasEnf, _),
    member(S, SintomasUsr),
    member(S, SintomasEnf).

% recomendacion: obtiene la recomendacion de una enfermedad
recomendacion(Enf, Rec) :-
    enfermedad(Enf, _, _, Rec).

% enfermedades_cronicas: lista todas las enfermedades cronicas
enfermedades_cronicas(Lista) :-
    findall(E, enfermedad(E, cronica, _, _), Lista).

% enfermedades_por_sintoma: enfermedades que tienen cierto sintoma
enfermedades_por_sintoma(Sint, Lista) :-
    findall(E, (enfermedad(E, _, Sints, _), member(Sint, Sints)), Lista).

% Reglas auxiliares para la GUI

% obtener info completa de un diagnostico
diagnostico_info(SintomasUsr, Enf, Cat, Rec) :-
    enfermedad(Enf, Cat, SintomasEnf, Rec),
    member(S, SintomasUsr),
    member(S, SintomasEnf).

% contar cuantos sintomas coinciden
contar_coincidencias(SintomasUsr, Enf, Cant) :-
    enfermedad(Enf, _, SintomasEnf, _),
    findall(S, (member(S, SintomasUsr), member(S, SintomasEnf)), Coinciden),
    length(Coinciden, Cant).

% diagnosticos ordenados por relevancia (mas coincidencias primero)
diagnostico_ordenado(SintomasUsr, ListaOrdenada) :-
    findall((Cant, Enf, Cat, Rec),
        (enfermedad(Enf, Cat, SintomasEnf, Rec),
         findall(S, (member(S, SintomasUsr), member(S, SintomasEnf)), Coinc),
         length(Coinc, Cant),
         Cant > 0),
        ListaSinOrden),
    sort(0, @>=, ListaSinOrden, ListaOrdenada).

% listar todas las enfermedades
todas_enfermedades(Lista) :-
    findall(E, enfermedad(E, _, _, _), Lista).

% listar todos los sintomas
todos_sintomas(Lista) :-
    findall(S, sintoma(S), Lista).

% enfermedades de una categoria
enfermedades_categoria(Cat, Lista) :-
    findall(E, enfermedad(E, Cat, _, _), Lista).

% Predicados para carga dinamica (usados por Java)

limpiar_hechos :-
    retractall(enfermedad(_, _, _, _)),
    retractall(sintoma(_)),
    retractall(paciente(_, _)).

agregar_enfermedad(Nom, Cat, Sints, Rec) :-
    assertz(enfermedad(Nom, Cat, Sints, Rec)).

agregar_sintoma(Nom) :-
    assertz(sintoma(Nom)).

agregar_paciente(Nom, Edad) :-
    assertz(paciente(Nom, Edad)).
