
:- dynamic enfermedad/4.
% enfermedad(Nombre, Categoria, Sintomas, Recomendacion).
% Ejemplo: enfermedad(gripe, viral, [fiebre, tos, dolor_cabeza, dolor_muscular], 'Descansar e hidratarse').

:- dynamic sintoma/1.
% sintoma(Nombre).
% Ejemplo: sintoma(fiebre).

:- dynamic paciente/2.
% paciente(Nombre, Edad).
% Ejemplo: paciente('Juan Perez', 25).

% --------------------------------------------
% REGLAS ESTÁTICAS
% --------------------------------------------

% 1. coincide_sintomas(SintomasUsuario, SintomasEnfermedad)
%    Verdadero si TODOS los síntomas del usuario están en la lista de síntomas de la enfermedad
coincide_sintomas([], _).
coincide_sintomas([Sintoma|Resto], SintomasEnfermedad) :-
    member(Sintoma, SintomasEnfermedad),
    coincide_sintomas(Resto, SintomasEnfermedad).

% 2. diagnostico(SintomasUsuario, Enfermedad)
%    Devuelve una enfermedad que coincide con AL MENOS UN síntoma
diagnostico(SintomasUsuario, Enfermedad) :-
    enfermedad(Enfermedad, _, SintomasEnfermedad, _),
    member(Sintoma, SintomasUsuario),
    member(Sintoma, SintomasEnfermedad).

% 3. diagnostico_completo(SintomasUsuario, Enfermedad, Categoria, Recomendacion)
%    Devuelve enfermedad con toda su información si coincide al menos un síntoma
diagnostico_completo(SintomasUsuario, Enfermedad, Categoria, Recomendacion) :-
    enfermedad(Enfermedad, Categoria, SintomasEnfermedad, Recomendacion),
    member(Sintoma, SintomasUsuario),
    member(Sintoma, SintomasEnfermedad).

% 4. diagnostico_categoria(SintomasUsuario, Categoria, Enfermedad)
%    Devuelve enfermedades de una categoría específica que coinciden con los síntomas
diagnostico_categoria(SintomasUsuario, Categoria, Enfermedad) :-
    enfermedad(Enfermedad, Categoria, SintomasEnfermedad, _),
    member(Sintoma, SintomasUsuario),
    member(Sintoma, SintomasEnfermedad).

% 5. recomendacion(Enfermedad, Recomendacion)
%    Devuelve la recomendación asociada a una enfermedad
recomendacion(Enfermedad, Recomendacion) :-
    enfermedad(Enfermedad, _, _, Recomendacion).

% 6. enfermedades_cronicas(ListaEnfermedades)
%    Devuelve la lista de todas las enfermedades crónicas
enfermedades_cronicas(ListaEnfermedades) :-
    findall(Enfermedad, enfermedad(Enfermedad, cronica, _, _), ListaEnfermedades).

% 7. enfermedades_por_sintoma(Sintoma, ListaEnfermedades)
%    Devuelve todas las enfermedades que presentan un síntoma específico
enfermedades_por_sintoma(Sintoma, ListaEnfermedades) :-
    findall(Enfermedad, 
            (enfermedad(Enfermedad, _, Sintomas, _), member(Sintoma, Sintomas)), 
            ListaEnfermedades).

% 8. enfermedades_por_categoria(Categoria, ListaEnfermedades)
%    Devuelve todas las enfermedades de una categoría específica
enfermedades_por_categoria(Categoria, ListaEnfermedades) :-
    findall(Enfermedad, enfermedad(Enfermedad, Categoria, _, _), ListaEnfermedades).

% 9. obtener_sintomas_enfermedad(Enfermedad, Sintomas)
%    Obtiene la lista de síntomas de una enfermedad
obtener_sintomas_enfermedad(Enfermedad, Sintomas) :-
    enfermedad(Enfermedad, _, Sintomas, _).

% 10. obtener_categoria(Enfermedad, Categoria)
%     Obtiene la categoría de una enfermedad
obtener_categoria(Enfermedad, Categoria) :-
    enfermedad(Enfermedad, Categoria, _, _).

% 11. contar_coincidencias(SintomasUsuario, Enfermedad, Cantidad)
%     Cuenta cuántos síntomas del usuario coinciden con una enfermedad
contar_coincidencias(SintomasUsuario, Enfermedad, Cantidad) :-
    enfermedad(Enfermedad, _, SintomasEnfermedad, _),
    findall(S, (member(S, SintomasUsuario), member(S, SintomasEnfermedad)), Coincidencias),
    length(Coincidencias, Cantidad).

% 12. diagnostico_ordenado(SintomasUsuario, ListaDiagnosticos)
%     Devuelve diagnósticos ordenados por cantidad de síntomas coincidentes
diagnostico_ordenado(SintomasUsuario, ListaDiagnosticos) :-
    findall(
        (Cantidad, Enfermedad, Categoria, Recomendacion),
        (
            enfermedad(Enfermedad, Categoria, SintomasEnfermedad, Recomendacion),
            findall(S, (member(S, SintomasUsuario), member(S, SintomasEnfermedad)), Coincidencias),
            length(Coincidencias, Cantidad),
            Cantidad > 0
        ),
        ListaSinOrdenar
    ),
    sort(0, @>=, ListaSinOrdenar, ListaDiagnosticos).

% 13. todas_las_enfermedades(Lista)
%     Obtiene lista de todas las enfermedades registradas
todas_las_enfermedades(Lista) :-
    findall(Enfermedad, enfermedad(Enfermedad, _, _, _), Lista).

% 14. todos_los_sintomas(Lista)
%     Obtiene lista de todos los síntomas registrados
todos_los_sintomas(Lista) :-
    findall(Sintoma, sintoma(Sintoma), Lista).

% 15. todas_las_categorias(Lista)
%     Obtiene lista de categorías únicas
todas_las_categorias(Lista) :-
    findall(Categoria, enfermedad(_, Categoria, _, _), TodasCategorias),
    list_to_set(TodasCategorias, Lista).

% --------------------------------------------
% CARGA DINÁMICA
% --------------------------------------------

% Limpiar todos los hechos dinámicos
limpiar_base_conocimiento :-
    retractall(enfermedad(_, _, _, _)),
    retractall(sintoma(_)),
    retractall(paciente(_, _)).

% Agregar una enfermedad (Java)
agregar_enfermedad(Nombre, Categoria, Sintomas, Recomendacion) :-
    assertz(enfermedad(Nombre, Categoria, Sintomas, Recomendacion)).

% Agregar un síntoma (Java)
agregar_sintoma(Nombre) :-
    assertz(sintoma(Nombre)).

% Agregar un paciente
agregar_paciente(Nombre, Edad) :-
    assertz(paciente(Nombre, Edad)).

