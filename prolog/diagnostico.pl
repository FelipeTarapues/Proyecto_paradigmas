

:- dynamic enfermedad/4.   % enfermedad(Nombre, Categoria, ListaSintomas, Recomendacion)
:- dynamic sintoma/1.      % sintoma(Nombre)
:- dynamic paciente/2.     % paciente(Nombre, Edad)

% coincide_sintomas: verifica si todos los sintomas del usuario estan en la enfermedad
coincide_sintomas([], _).
coincide_sintomas([S|Resto], SintomasEnf) :-
    member(S, SintomasEnf),
    coincide_sintomas(Resto, SintomasEnf).

% devuelve enfermedad que coincide con al menos un sintoma
diagnostico(SintomasUsr, Enf) :-
    enfermedad(Enf, _, SintomasEnf, _),
    member(S, SintomasUsr),
    member(S, SintomasEnf).

% filtra por categoria
diagnostico_categoria(SintomasUsr, Cat, Enf) :-
    enfermedad(Enf, Cat, SintomasEnf, _),
    member(S, SintomasUsr),
    member(S, SintomasEnf).

% obtiene la recomendacion de una enfermedad
recomendacion(Enf, Rec) :-
    enfermedad(Enf, _, _, Rec).

% lista todas las enfermedades cronicas
enfermedades_cronicas(Lista) :-
    findall(E, enfermedad(E, cronica, _, _), Lista).

% enfermedades_por_sintoma: enfermedades que tienen cierto sintoma
enfermedades_por_sintoma(Sint, Lista) :-
    findall(E, (enfermedad(E, _, Sints, _), member(Sint, Sints)), Lista).

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
