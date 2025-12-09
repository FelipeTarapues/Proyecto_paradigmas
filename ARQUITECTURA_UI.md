# Arquitectura del Sistema - Diagrama Visual

## Estructura General del Proyecto

```
Proyecto_paradigmas/
├── database/
│   ├── schema.sql          ← Tablas y vistas SQL
│   └── data.sql            ← Datos iniciales (10 enfermedades, 20 síntomas)
│
├── prolog/
│   ├── diagnostico.pl      ← Motor de inferencia Prolog
│   └── test_diagnostico.pl ← Tests y carga de datos
│
├── java/                   ← NUEVA: Interfaz Visual
│   ├── pom.xml             ← Configuración Maven
│   ├── run.bat / run.sh    ← Scripts de ejecución
│   ├── README.md           ← Documentación detallada
│   └── src/main/java/com/sistemexperto/
│       ├── ui/             ← Interfaz gráfica (Swing)
│       ├── db/             ← Conexión a MySQL
│       ├── prolog/         ← Motor de inferencia (Java)
│       └── models/         ← Clases de datos
│
└── ARCHITECTURE.md         ← Documentación de arquitectura
```

## Flujo de Integración (Capas)

```
┌─────────────────────────────────────────────────────────────┐
│                   CAPA DE PRESENTACIÓN                      │
│                    (Java Swing - UI)                        │
│  MainWindow → DiagnosticoPanel + HistorialPanel             │
│  • Formularios, botones, listas                             │
│  • Resultados visuales                                      │
└────────────────┬────────────────────────────────┬───────────┘
                 │                                │
    ┌────────────▼────────────┐      ┌───────────▼──────────┐
    │ CAPA DE LÓGICA          │      │ CAPA DE DATOS        │
    │                         │      │                      │
    │ PrologEngine.java       │      │ DatabaseConnection   │
    │ • diagnostico()         │      │ • conectar()         │
    │ • recomendacion()       │      │ • obtenerEnfermedades│
    │ • enfermedadesCronicas()│      │ • registrarDiagno... │
    │ • enfermedadesPorSint...│      │ • obtenerSintomas()  │
    │                         │      │                      │
    │ (Simula Prolog)         │      │ (Conecta MySQL)      │
    └────────────┬────────────┘      └───────────┬──────────┘
                 │                                │
            [Java Models]                    [JDBC]
          Enfermedad.java                 MySQL Driver
          Diagnostico.java
                 │                                │
                 └────────────┬─────────────────┬┘
                              │
                    ┌─────────▼──────────┐
                    │   BASE DE DATOS    │
                    │     MySQL 8.0      │
                    │                    │
                    │ sistema_experto_   │
                    │ medico             │
                    │                    │
                    │ Tablas:            │
                    │ - categorias       │
                    │ - sintomas         │
                    │ - enfermedades     │
                    │ - pacientes        │
                    │ - diagnosticos     │
                    │ - vistas SQL       │
                    └────────────────────┘
```

## Interface de Usuario - Componentes

### MainWindow (JFrame Principal)

```
┌────────────────────────────────────────────────────────────┐
│ Sistema Experto Médico - Diagnóstico por Síntomas      [_][□][X]
├────────────────────────────────────────────────────────────┤
│ [Nuevo Diagnóstico] [Historial] [Información]              │
├────────────────────────────────────────────────────────────┤
│                                                             │
│  DiagnosticoPanel / HistorialPanel / InfoPanel             │
│                                                             │
│                                                             │
├────────────────────────────────────────────────────────────┤
│ ✓ Conectado - 10 enfermedades, 20 síntomas disponibles    │
└────────────────────────────────────────────────────────────┘
```

### DiagnosticoPanel (Tab 1)

```
┌─────────────────────────────────────────────────────────┐
│ Datos del Paciente                                      │
├─────────────────────────────────────────────────────────┤
│ Nombre: [____________]    Edad: [35]                   │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ Selecciona Síntomas                                     │
├──────────────────┬────────┬──────────────────┐
│ Síntomas Disp.   │ Botones│ Síntomas Select. │
├──────────────────┼────────┼──────────────────┤
│ □ cansancio      │        │ ✓ fiebre        │
│ □ dolor_cabeza   │ → Agregar                 │
│ □ diarrea        │ ← Remover │ ✓ tos        │
│ □ erupcion       │        │                  │
│ □ fiebre         │        │                  │
│ □ nausea         │        │                  │
│ □ tos            │        │                  │
│ (scroll...)      │        │                  │
└──────────────────┴────────┴──────────────────┘

┌─────────────────────────────────────────────────────────┐
│ [         OBTENER DIAGNÓSTICO         ]                 │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ Resultados del Diagnóstico                              │
├─────────────────────────────────────────────────────────┤
│ === DIAGNÓSTICO ===                                     │
│ Paciente: Juan Pérez                                    │
│ Edad: 35 años                                           │
│ Síntomas: [fiebre, tos]                                │
│                                                         │
│ POSIBLES DIAGNÓSTICOS:                                  │
│ =====================                                   │
│                                                         │
│ • Gripe (viral)                                         │
│   Síntomas: [fiebre, tos, ...]                         │
│   Recomendación: Descansar, mantenerse hidratado...   │
│                                                         │
│ • COVID-19 (viral)                                      │
│   Síntomas: [fiebre, tos, ...]                         │
│   Recomendación: Aislamiento inmediato...             │
│                                                         │
│ • Faringitis (bacteriana)                               │
│   Síntomas: [dolor_garganta, fiebre, tos]              │
│   Recomendación: Consultar al médico...                │
│                                                         │
│ ADVERTENCIA:                                            │
│ Este es un diagnóstico asistido. Consulta siempre      │
│ con un médico profesional.                              │
│                                                         │
├─────────────────────────────────────────────────────────┤
│ [        Guardar Diagnóstico        ]                   │
└─────────────────────────────────────────────────────────┘
```

## Secuencia de Operación

```
Usuario abre aplicación
       │
       ▼
MainWindow.java
       │
       ├─→ DatabaseConnection.conectar()
       │   └─→ MySQL: "sistema_experto_medico"
       │
       ├─→ DatabaseConnection.obtenerEnfermedades()
       │   └─→ SELECT * FROM v_enfermedades_completas
       │
       ├─→ PrologEngine.cargarEnfermedades(list)
       │
       ▼
Interfaz lista mostrando síntomas
       │
Usuario selecciona síntomas
       │
       ▼
DiagnosticoPanel.realizarDiagnostico()
       │
       ├─→ PrologEngine.diagnostico([síntomas])
       │   ├─→ Itera enfermedades
       │   ├─→ Busca coincidencias member()
       │   └─→ Retorna List<Enfermedad>
       │
       ▼
Mostrar resultados en TextArea
       │
       ├─→ Mostrar enfermedad
       ├─→ Mostrar síntomas coincidentes
       └─→ Mostrar recomendación
       │
Usuario presiona "Guardar"
       │
       ▼
DatabaseConnection.registrarDiagnostico()
       │
       ├─→ INSERT INTO pacientes
       ├─→ INSERT INTO diagnosticos
       └─→ INSERT INTO diagnostico_sintomas
       │
       ▼
Confirmación guardada en BD
```

## Mapeo de Clases Java ↔ Base de Datos

```
Enfermedad.java
├─ idEnfermedad      ←→ enfermedades.id_enfermedad
├─ nombre            ←→ enfermedades.nombre
├─ categoria         ←→ categorias.nombre
├─ sintomas (List)   ←→ GROUP_CONCAT(sintomas.nombre)
└─ recomendacion     ←→ enfermedades.recomendacion

Diagnostico.java
├─ idDiagnostico     ←→ diagnosticos.id_diagnostico
├─ nombrePaciente    ←→ pacientes.nombre
├─ edadPaciente      ←→ pacientes.edad
├─ sintomasIngresados (List)
├─ enfermedadesDetectadas (List)
├─ recomendacion
└─ fecha             ←→ diagnosticos.fecha_diagnostico
```

## Predicados PrologEngine.java

| Método Java | Equivalente Prolog | Descripción |
|---|---|---|
| `diagnostico(List<String>)` | `diagnostico(Síntomas, Enfermedad)` | Encuentra enfermedades con síntomas comunes |
| `diagnosticoCategoria(List<String>, String)` | `diagnostico_categoria(Síntomas, Cat, Enf)` | Filtra por categoría |
| `recomendacion(String)` | `recomendacion(Enfermedad, Recomendación)` | Obtiene recomendación |
| `enfermedadesCronicas()` | `enfermedades_cronicas(Lista)` | Lista enfermedades crónicas |
| `enfermedadesPorSintoma(String)` | `enfermedades_por_sintoma(Sint, Lista)` | Enfermedades con síntoma específico |
| `coincideSintomas(List, List)` | `coincide_sintomas(SintUsr, SintEnf)` | Verifica si tiene TODOS los síntomas |

## Flujo de Ejecutación Paso a Paso

```
1. Usuario ejecuta: java -jar sistema-experto-medico-1.0.0.jar
   └─ O: mvn exec:java -Dexec.mainClass="com.sistemexperto.ui.MainWindow"

2. MainWindow.__init__()
   ├─ DatabaseConnection db = new DatabaseConnection()
   ├─ PrologEngine prolog = new PrologEngine()
   ├─ db.conectar()  ← Abre conexión JDBC a MySQL
   └─ cargarDatos()  ← Carga tablas

3. cargarDatos() ejecuta:
   ├─ enfermedades = db.obtenerEnfermedades()  [Query: SELECT * FROM v_enfermedades_completas]
   ├─ sintomas = db.obtenerSintomas()          [Query: SELECT nombre FROM sintomas]
   ├─ prolog.cargarEnfermedades(enfermedades)
   ├─ prolog.cargarSintomas(sintomas)
   └─ diagnosticoPanel.actualizarDatos()

4. Usuario selecciona síntomas y presiona "OBTENER DIAGNÓSTICO"
   └─ DiagnosticoPanel.realizarDiagnostico()

5. realizarDiagnostico() ejecuta:
   ├─ List<String> sintomasSeleccionados = [obtener de UI]
   ├─ List<Enfermedad> resultados = prolog.diagnostico(sintomasSeleccionados)
   │  └─ PrologEngine itera todas las enfermedades
   │  └─ Para cada enfermedad, verifica member(sintoma, lista)
   │  └─ Si hay coincidencia, agrega a resultados
   └─ Mostrar resultados en resultadoTextArea

6. Usuario presiona "Guardar Diagnóstico"
   └─ DiagnosticoPanel.guardarDiagnostico()

7. guardarDiagnostico() ejecuta:
   ├─ db.registrarDiagnostico(nombre, edad, síntomas, enfermedad, obs)
   │  ├─ INSERT INTO pacientes (nombre, edad)
   │  ├─ INSERT INTO diagnosticos (id_paciente, id_enfermedad, obs)
   │  └─ INSERT INTO diagnostico_sintomas (id_diagnostico, id_sintoma)
   └─ Mostrar confirmación
```

## Archivos Generados

```
java/
├── pom.xml                                    # 130 líneas
├── run.bat                                    # 20 líneas
├── run.sh                                     # 21 líneas
├── README.md                                  # 250+ líneas
└── src/main/java/com/sistemexperto/
    ├── models/
    │   ├── Enfermedad.java                    # 70 líneas
    │   └── Diagnostico.java                   # 90 líneas
    ├── db/
    │   └── DatabaseConnection.java            # 170 líneas
    ├── prolog/
    │   └── PrologEngine.java                  # 130 líneas
    └── ui/
        ├── MainWindow.java                    # 130 líneas
        ├── DiagnosticoPanel.java              # 260 líneas
        └── HistorialPanel.java                # 30 líneas

TOTAL: ~1300 líneas de código Java
```

## Capacidades de Cada Componente

### DatabaseConnection (170 líneas)
- ✅ Conectar/desconectar MySQL
- ✅ Cargar enfermedades con síntomas
- ✅ Cargar síntomas y categorías
- ✅ Registrar diagnósticos (transacciones ACID)
- ✅ Manejo de IDs generados automáticamente

### PrologEngine (130 líneas)
- ✅ Simula predicados Prolog en Java
- ✅ diagnostico() - búsqueda con al menos 1 síntoma
- ✅ diagnosticoCategoria() - filtro por categoría
- ✅ recomendacion() - extrae recomendaciones
- ✅ enfermedadesCronicas() - lista crónicas
- ✅ enfermedadesPorSintoma() - búsqueda inversa
- ✅ coincideSintomas() - validación booleana

### DiagnosticoPanel (260 líneas)
- ✅ Formulario paciente (nombre, edad)
- ✅ Selector de síntomas dual (disponibles ↔ seleccionados)
- ✅ Botones mover síntomas
- ✅ Visualización de resultados formateada
- ✅ Guardado en base de datos
- ✅ Validaciones de entrada

### MainWindow (130 líneas)
- ✅ Interfaz con JTabbedPane (3 tabs)
- ✅ Carga automática de datos en background
- ✅ Manejo de excepciones conexión
- ✅ Barra de estado
- ✅ Panel de información

## Casos de Uso Cubiertos

1. **Diagnóstico Simple** ✅
   - Usuario: síntomas → Sistema: enfermedades probables

2. **Diagnóstico con Categoría** ✅
   - Usuario: síntomas + categoría → Sistema: filtra resultados

3. **Historial** ✅ (interfaz lista, datos pendientes)
   - Sistema: almacena diagnósticos en MySQL

4. **Recomendaciones** ✅
   - Sistema: muestra pasos recomendados por enfermedad

5. **Persistencia** ✅
   - Base de datos guarda todos los diagnósticos

---

**Última actualización:** 8 de Diciembre de 2025

