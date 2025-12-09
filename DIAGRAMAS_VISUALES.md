# 📊 Diagramas Visuales - Sistema Experto Médico

## 1️⃣ Diagrama de Capas (Layers)

```
╔═══════════════════════════════════════════════════════════════════╗
║                    CAPA 1: PRESENTACIÓN (UI)                     ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  MainWindow.java (JFrame Principal)                      │   ║
║  │  ├─ DiagnosticoPanel (Seleccionar síntomas, ver resultados) │ ║
║  │  ├─ HistorialPanel (Ver diagnósticos previos)            │   ║
║  │  └─ InfoPanel (Información del sistema)                  │   ║
║  └──────────────────────────────────────────────────────────┘   ║
║                           ▲                                        ║
║                           │ Eventos (clicks, inputs)              ║
║                           │                                        ║
╠═══════════════════════════════════════════════════════════════════╣
║                    CAPA 2: LÓGICA (BUSINESS)                     ║
║  ┌──────────────────┐    ┌──────────────────────────────────┐   ║
║  │ PrologEngine.java│    │ DatabaseConnection.java          │   ║
║  │ ├─ diagnostico()  │    │ ├─ conectar()                   │   ║
║  │ ├─ recomendacion()│    │ ├─ obtenerEnfermedades()        │   ║
║  │ └─ (5+ métodos)   │    │ ├─ obtenerSintomas()            │   ║
║  │                   │    │ ├─ registrarDiagnostico()       │   ║
║  │ Simula Prolog     │    │ └─ (queries SQL)                │   ║
║  └──────────────────┘    └──────────────────────────────────┘   ║
║        ▲                            ▲                             ║
║        └────────┬─────────────────┬─┘                            ║
║                 │                 │                              ║
║  Métodos Prolog │    Métodos JDBC │                             ║
║                 │                 │                              ║
╠═══════════════════════════════════════════════════════════════════╣
║              CAPA 3: MODELOS DE DATOS (POJO)                     ║
║  ┌─────────────────┐           ┌───────────────────┐             ║
║  │ Enfermedad.java │           │ Diagnostico.java  │             ║
║  │ ├─ nombre       │           │ ├─ nombrePaciente │             ║
║  │ ├─ categoria    │           │ ├─ edad           │             ║
║  │ ├─ sintomas[]   │           │ ├─ síntomas[]     │             ║
║  │ └─ recomendacion│           │ └─ enfermedades[] │             ║
║  └─────────────────┘           └───────────────────┘             ║
║           ▲                                                        ║
║           │ Mapeo OOP-Relacional                                 ║
║           │                                                        ║
╠═══════════════════════════════════════════════════════════════════╣
║              CAPA 4: BASE DE DATOS (MySQL)                       ║
║  ┌─────────────────────────────────────────────────────────┐    ║
║  │                 sistema_experto_medico                   │    ║
║  │  ┌─────────────┐  ┌──────────┐  ┌─────────────────┐    │    ║
║  │  │ categorias  │  │ sintomas │  │  enfermedades   │    │    ║
║  │  │ ├─ id (PK)  │  │ ├─ id (PK)  │  ├─ id (PK)      │    │    ║
║  │  │ └─ nombre   │  │ └─ nombre   │  ├─ nombre       │    │    ║
║  │  └─────────────┘  └──────────┘  ├─ id_categoria  │    │    ║
║  │         ▲                        ├─ recomendacion│    │    ║
║  │         │ 1:N                    └─────────────────┘    │    ║
║  │         │                               ▲               │    ║
║  │  ┌──────────────────────┐              │ M:N           │    ║
║  │  │ enfermedad_sintoma   │              │               │    ║
║  │  │ ├─ id_enfermedad (FK)├──────────────┘               │    ║
║  │  │ └─ id_sintoma (FK)   │                              │    ║
║  │  └──────────────────────┘                              │    ║
║  │                                                         │    ║
║  │  ┌─────────┐  ┌────────────┐  ┌──────────────────┐   │    ║
║  │  │pacientes│  │diagnosticos│  │diagnostico_sint..│   │    ║
║  │  │├─ id    │  │├─ id       │  │├─ id_diagnostico │   │    ║
║  │  │├─ nombre│  │├─ id_pac...│  │└─ id_sintoma     │   │    ║
║  │  │└─ edad  │  │├─ id_enf...│  └──────────────────┘   │    ║
║  │  └─────────┘  │├─ fecha    │                          │    ║
║  │        ▲      │└─ obs.     │                          │    ║
║  │        │      └─────┬──────┘                          │    ║
║  │        └────────────┘                                │    ║
║  │       1:N Relación                                  │    ║
║  │                                                      │    ║
║  └─────────────────────────────────────────────────────┘    ║
╚═══════════════════════════════════════════════════════════════════╝
```

---

## 2️⃣ Flujo de Diagnóstico (Paso a Paso)

```
┌─────────────────────────────────────────────────────────────────┐
│ USUARIO ABRE LA APLICACIÓN                                      │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     ▼
        ┌────────────────────────┐
        │ MainWindow.main()      │
        │ • Crea JFrame          │
        │ • Conecta BD           │
        │ • Carga datos          │
        └────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
        ▼                         ▼
    MySQL                  PrologEngine
    SELECT *               .cargarEnfermedades()
    FROM v_enfermedades    .cargarSintomas()
        │
        └────────────────────────┐
                                 │
                                 ▼
                     ┌──────────────────────────┐
                     │ DiagnosticoPanel         │
                     │ • Muestra lista síntomas │
                     │ • Listo para diagnóstico │
                     └──────────────────────────┘
                                 │
                                 ▼ Usuario selecciona síntomas
                     ┌──────────────────────────────────────┐
                     │ Usuario pone:                        │
                     │ - Nombre: Juan Pérez                │
                     │ - Edad: 35                          │
                     │ - Síntomas: [fiebre, tos]           │
                     │                                      │
                     │ Presiona: OBTENER DIAGNÓSTICO       │
                     └──────────────────────────────────────┘
                                 │
                                 ▼
                     ┌──────────────────────────────────────┐
                     │ DiagnosticoPanel                     │
                     │ .realizarDiagnostico()               │
                     │                                      │
                     │ 1. Obtiene síntomas seleccionados   │
                     │ 2. Valida datos                     │
                     │ 3. Llama: PrologEngine              │
                     │    .diagnostico([síntomas])         │
                     └────────────┬─────────────────────────┘
                                  │
                                  ▼
                     ┌──────────────────────────────────────┐
                     │ PrologEngine.diagnostico()           │
                     │                                      │
                     │ Para cada enfermedad en BD:          │
                     │   • Obtiene sintomas de enfermedad   │
                     │   • Verifica member(sintoma,lista)   │
                     │   • Si coincide → agrega resultado   │
                     │                                      │
                     │ Retorna: List<Enfermedad>           │
                     │ Ejemplo:                            │
                     │ - Gripe (viral)                     │
                     │ - COVID-19 (viral)                  │
                     │ - Faringitis (bacteriana)           │
                     └────────────┬─────────────────────────┘
                                  │
                                  ▼
                     ┌──────────────────────────────────────┐
                     │ DiagnosticoPanel                     │
                     │ Construye texto de resultados:       │
                     │                                      │
                     │ === DIAGNÓSTICO ===                 │
                     │ Paciente: Juan Pérez                │
                     │ Edad: 35                            │
                     │ Síntomas: [fiebre, tos]             │
                     │                                      │
                     │ POSIBLES DIAGNÓSTICOS:              │
                     │ • Gripe (viral)                     │
                     │   Sínt: [fiebre, tos, ...]         │
                     │   Rec: Descansar, mantenerse...     │
                     │ • COVID-19 (viral)                  │
                     │   Sínt: [fiebre, tos, ...]         │
                     │   Rec: Aislamiento inmediato...     │
                     │                                      │
                     │ (Mostrar en resultadoTextArea)      │
                     └────────────┬─────────────────────────┘
                                  │
                                  ▼ Usuario ve resultados
                     ┌──────────────────────────────────────┐
                     │ Usuario presiona:                    │
                     │ "Guardar Diagnóstico"               │
                     └────────────┬─────────────────────────┘
                                  │
                                  ▼
                     ┌──────────────────────────────────────┐
                     │ DiagnosticoPanel                     │
                     │ .guardarDiagnostico()                │
                     │                                      │
                     │ Llama: DatabaseConnection            │
                     │ .registrarDiagnostico(...)           │
                     └────────────┬─────────────────────────┘
                                  │
                                  ▼
                     ┌──────────────────────────────────────┐
                     │ DatabaseConnection                   │
                     │ .registrarDiagnostico()              │
                     │                                      │
                     │ Transacción SQL:                     │
                     │ 1. BEGIN TRANSACTION                │
                     │ 2. INSERT INTO pacientes             │
                     │    (nombre, edad)                    │
                     │ 3. INSERT INTO diagnosticos          │
                     │    (id_paciente, id_enfermedad)     │
                     │ 4. INSERT INTO diagnostico_sintomas  │
                     │    FOR EACH síntoma                 │
                     │ 5. COMMIT                            │
                     └────────────┬─────────────────────────┘
                                  │
                                  ▼
                           MySQL Base de Datos
                           Almacena todo en:
                           - pacientes
                           - diagnosticos
                           - diagnostico_sintomas
                                  │
                                  ▼
                     ┌──────────────────────────────────────┐
                     │ Confirmación:                        │
                     │ "Diagnóstico guardado exitosamente"  │
                     └──────────────────────────────────────┘
```

---

## 3️⃣ Estructura de Carpetas (Árbol Completo)

```
Proyecto_paradigmas/                          ← Raíz del proyecto
│
├── 📄 INICIO_RAPIDO.md                       ← ⭐ START HERE
├── 📄 ARQUITECTURA_UI.md                     ← Diagramas detallados
├── 📄 ARCHITECTURE.md                        ← Arquitectura completa
│
├── 📁 database/                              ← Base de datos MySQL
│   ├── schema.sql                            ├─ Tablas y vistas
│   └── data.sql                              └─ Datos iniciales (10 enfermedades)
│
├── 📁 prolog/                                ← Motor de Prolog (referencias)
│   ├── diagnostico.pl                        ├─ Predicados core
│   └── test_diagnostico.pl                   └─ Suite de pruebas
│
└── 📁 java/                                  ← ✨ INTERFAZ VISUAL (NUEVA)
    │
    ├── 📄 pom.xml                            ← Configuración Maven
    ├── 📄 run.bat                            ← Ejecutar en Windows
    ├── 📄 run.sh                             ← Ejecutar en Linux/Mac
    ├── 📄 README.md                          ← Documentación completa
    │
    └── 📁 src/main/java/com/sistemexperto/
        │
        ├── 📁 models/                        ← Clases de datos (POJOs)
        │   ├── Enfermedad.java               ├─ Mapea tabla enfermedades
        │   └── Diagnostico.java              └─ Mapea tabla diagnosticos
        │
        ├── 📁 db/                            ← Acceso a base de datos
        │   └── DatabaseConnection.java       └─ Conecta MySQL + queries
        │
        ├── 📁 prolog/                        ← Lógica de diagnóstico
        │   └── PrologEngine.java             └─ Predicados Prolog en Java
        │
        └── 📁 ui/                            ← Interfaz gráfica (Swing)
            ├── MainWindow.java               ├─ Ventana principal (JFrame)
            ├── DiagnosticoPanel.java         ├─ Panel diagnóstico (260 líneas)
            └── HistorialPanel.java           └─ Panel historial (en desarrollo)

📊 ESTADÍSTICAS:
─────────────────────────────────────────────
Total de líneas Java:     ~1300 líneas
Total de líneas SQL:      ~160 líneas
Total de líneas Prolog:   ~120 líneas
Archivos documentación:   ~600 líneas
─────────────────────────────────────────────
TOTAL PROYECTO:           ~2180 líneas

🎯 CLASES PRINCIPALES:
─────────────────────────────────────────────
MainWindow          → JFrame, carga datos, muestra UI
DiagnosticoPanel    → Interacción usuario (260 líneas)
DatabaseConnection  → Conexión MySQL, queries (170 líneas)
PrologEngine        → Lógica diagnóstica (130 líneas)
Enfermedad          → POJO enfermedad (70 líneas)
Diagnostico         → POJO diagnóstico (90 líneas)
```

---

## 4️⃣ Mapa de Métodos Principales

```
MainWindow.java (130 líneas)
├─ main(String[])
│  └─ SwingUtilities.invokeLater()
│
├─ __init__()
│  ├─ setTitle(), setSize(), setResizable()
│  ├─ DatabaseConnection = new ...()
│  ├─ PrologEngine = new ...()
│  └─ cargarDatos()
│
├─ crearInterfaz()
│  ├─ JTabbedPane tabbedPane = new ...()
│  ├─ tabbedPane.addTab("Nuevo Diagnóstico", diagnosticoPanel)
│  ├─ tabbedPane.addTab("Historial", historialPanel)
│  └─ tabbedPane.addTab("Información", infoPanel)
│
└─ crearPanelInfo()
   └─ JTextArea + info sistema


DiagnosticoPanel.java (260 líneas)
├─ __init__(DatabaseConnection, PrologEngine)
│  ├─ crearPanelPaciente()      → JPanel nombre, edad
│  ├─ crearPanelSintomas()      → JPanel selector síntomas
│  └─ crearPanelResultados()    → JPanel resultados
│
├─ crearPanelPaciente()
│  └─ GridLayout 1x4 → Nombre + Edad
│
├─ crearPanelSintomas()
│  ├─ JList sintomasDisponiblesList
│  ├─ JButton btnAgregar
│  │  └─ agregarSintoma()  → Mueve síntoma a seleccionados
│  ├─ JButton btnRemover
│  │  └─ removerSintoma()  → Mueve síntoma a disponibles
│  ├─ JList sintomasSeleccionadosList
│  └─ JButton "OBTENER DIAGNÓSTICO"
│     └─ realizarDiagnostico()
│
├─ realizarDiagnostico()
│  ├─ Valida nombre paciente
│  ├─ Valida que hay síntomas seleccionados
│  ├─ List<String> sintomasSeleccionados = obtener()
│  ├─ List<Enfermedad> enfermedades = prologEngine.diagnostico(...)
│  ├─ Construye texto resultado
│  └─ resultadoTextArea.setText(resultado)
│
├─ crearPanelResultados()
│  ├─ JTextArea resultadoTextArea
│  └─ JButton "Guardar Diagnóstico"
│     └─ guardarDiagnostico()
│
├─ guardarDiagnostico()
│  ├─ Valida que hay diagnóstico previo
│  ├─ obtener primera enfermedad encontrada
│  ├─ dbConnection.registrarDiagnostico(...)
│  └─ mostrar JOptionPane confirmación
│
└─ actualizarDatos()
   └─ Llena modeloDisponibles con síntomas


DatabaseConnection.java (170 líneas)
├─ conectar() → boolean
│  ├─ DriverManager.getConnection(url, user, password)
│  └─ return true/false
│
├─ obtenerEnfermedades() → List<Enfermedad>
│  ├─ SELECT ... FROM v_enfermedades_completas
│  └─ return List (10 enfermedades)
│
├─ obtenerSintomas() → List<String>
│  ├─ SELECT nombre FROM sintomas
│  └─ return List (20 síntomas)
│
├─ obtenerCategorias() → List<String>
│  ├─ SELECT nombre FROM categorias
│  └─ return List (4 categorías)
│
├─ registrarDiagnostico(String, int, List, String, String) → boolean
│  ├─ BEGIN TRANSACTION
│  ├─ INSERT INTO pacientes
│  ├─ INSERT INTO diagnosticos
│  ├─ INSERT INTO diagnostico_sintomas (loop síntomas)
│  ├─ COMMIT
│  └─ return true/false
│
├─ obtenerIdEnfermedad(String) → int [PRIVATE]
│  └─ SELECT id_enfermedad FROM enfermedades WHERE nombre = ?
│
├─ obtenerIdSintoma(String) → int [PRIVATE]
│  └─ SELECT id_sintoma FROM sintomas WHERE nombre = ?
│
├─ desconectar() → void
│  └─ connection.close()
│
└─ isConnected() → boolean
   └─ connection != null && !connection.isClosed()


PrologEngine.java (130 líneas)
├─ __init__()
│  ├─ List<Enfermedad> enfermedades = new ArrayList<>()
│  └─ List<String> sintomas = new ArrayList<>()
│
├─ cargarEnfermedades(List<Enfermedad>) → void
│  ├─ this.enfermedades = new ArrayList<>(enfermedadesDB)
│  └─ print("✓ Motor Prolog: X enfermedades cargadas")
│
├─ cargarSintomas(List<String>) → void
│  ├─ this.sintomas = new ArrayList<>(sintomasDB)
│  └─ print("✓ Motor Prolog: X síntomas cargados")
│
├─ diagnostico(List<String>) → List<Enfermedad>
│  ├─ FOR enfermedad IN enfermedades:
│  │  ├─ FOR sintoma IN sintomasUsuario:
│  │  │  ├─ IF enf.sintomas.contains(sintoma):
│  │  │  │  └─ resultados.add(enf) + break
│  │  └─ (evita duplicados)
│  └─ return resultados
│
├─ diagnosticoCategoria(List<String>, String) → List<Enfermedad>
│  ├─ Similar a diagnostico()
│  ├─ PERO: filtra IF enf.categoria.equals(categoria)
│  └─ return resultados
│
├─ enfermedadesCronicas() → List<Enfermedad>
│  ├─ FOR enfermedad IN enfermedades:
│  │  ├─ IF enf.categoria.equals("cronica"):
│  │  │  └─ resultados.add(enf)
│  └─ return resultados
│
├─ enfermedadesPorSintoma(String) → List<Enfermedad>
│  ├─ FOR enfermedad IN enfermedades:
│  │  ├─ IF enf.sintomas.contains(sintoma):
│  │  │  └─ resultados.add(enf)
│  └─ return resultados
│
├─ recomendacion(String) → String
│  ├─ FOR enfermedad IN enfermedades:
│  │  ├─ IF enf.nombre.equals(nombreEnfermedad):
│  │  │  └─ return enf.recomendacion
│  └─ return "No se encontró recomendación"
│
├─ coincideSintomas(List<String>, List<String>) → boolean
│  ├─ FOR sintoma IN sintomasEnf:
│  │  ├─ IF !sintomasUsr.contains(sintoma):
│  │  │  └─ return false
│  └─ return true
│
├─ obtenerTodasEnfermedades() → List<Enfermedad>
│  └─ return new ArrayList<>(enfermedades)
│
└─ obtenerTodosSintomas() → List<String>
   └─ return new ArrayList<>(sintomas)
```

---

## 5️⃣ Ciclo de Vida de la Aplicación

```
FASE 1: INICIO
├─ User: double-click run.bat
├─ java -jar ... / mvn exec:java
├─ MainWindow.__init__()
├─ DatabaseConnection db = new DatabaseConnection()
├─ PrologEngine prolog = new PrologEngine()
├─ db.conectar()  ← Intenta MySQL
└─ cargarDatos()  ← Llena estructuras

FASE 2: CONEXIÓN BD
├─ db.obtenerEnfermedades()
│  ├─ MySQL: SELECT * FROM v_enfermedades_completas
│  └─ Return: [Gripe, Resfriado, ..., Faringitis]
│
├─ db.obtenerSintomas()
│  ├─ MySQL: SELECT nombre FROM sintomas
│  └─ Return: [fiebre, tos, dolor_cabeza, ...]
│
├─ prolog.cargarEnfermedades(list)
│  └─ Almacena en memoria
│
├─ prolog.cargarSintomas(list)
│  └─ Almacena en memoria
│
└─ diagnosticoPanel.actualizarDatos()
   └─ Llena JList con síntomas disponibles

FASE 3: UI LISTA
├─ MainWindow.setVisible(true)
├─ Usuario ve 3 tabs
│  ├─ Nuevo Diagnóstico (DiagnosticoPanel)
│  ├─ Historial (HistorialPanel)
│  └─ Información (InfoPanel)
└─ Status bar: "✓ Conectado - 10 enfermedades..."

FASE 4: USUARIO INTERACTÚA
├─ Usuario escribe nombre paciente
├─ Usuario selecciona edad
├─ Usuario selecciona síntomas (arrastra)
├─ Usuario presiona "OBTENER DIAGNÓSTICO"
│  └─ DiagnosticoPanel.realizarDiagnostico()
│
└─ DiagnosticoPanel.realizarDiagnostico()
   ├─ Obtiene: nombre, edad, síntomas
   ├─ PrologEngine.diagnostico(síntomas)
   │  └─ Retorna: List<Enfermedad>
   │
   ├─ Construye texto resultado
   ├─ resultadoTextArea.setText(resultado)
   └─ Usuario ve: "Gripe, COVID-19, Faringitis"

FASE 5: GUARDAR (OPCIONAL)
├─ Usuario presiona "Guardar Diagnóstico"
├─ DiagnosticoPanel.guardarDiagnostico()
│
└─ DatabaseConnection.registrarDiagnostico(...)
   ├─ BEGIN
   ├─ INSERT INTO pacientes (Juan Pérez, 35)
   │  └─ id_paciente = 1
   │
   ├─ INSERT INTO diagnosticos (1, 1, "obs")
   │  └─ id_diagnostico = 1
   │
   ├─ FOR EACH síntoma:
   │  ├─ obtenerIdSintoma(síntoma)
   │  └─ INSERT INTO diagnostico_sintomas (1, id_sintoma)
   │
   ├─ COMMIT
   └─ JOptionPane: "Guardado exitosamente"

FASE 6: CIERRE
├─ Usuario cierra ventana (click X)
├─ addWindowListener.windowClosing()
├─ db.desconectar()
│  └─ connection.close()
└─ System.exit(0)
```

---

## 6️⃣ Esquema de BD (Tablas Relacionadas)

```
╔════════════════╗
║  categorias    ║          ╔════════════════════╗
║ ──────────────┣┐         ║    enfermedades    ║
║ id_categoria  │ │         ║ ──────────────────┣┐
║ (PK)          │ │  1:N    ║ id_enfermedad (PK) │ │
║ nombre (UK)   │ │         ║ nombre (UK)        │ │  M:N  ╔════════════════╗
║ descripción   │ └────────┨ id_categoria (FK)─┐│ ├─────┨    sintomas     ║
╚════════════════╝         ║ recomendacion      ││ │     ║ ──────────────┣
                           ╚════════════════════╣│ │     ║ id_sintoma (PK
                                                │ │     ║ nombre (UK)   ║
                                                │ │     ╚════════════════╝
                                                │ │            ▲
                                                │ │            │
          ╔═════════════════════════╗           │ │      ╔─────────────────────┐
          ║ enfermedad_sintoma      ║           │ │      ║ (Junction Table)    ║
          ║ ────────────────────────╢N     ────┘ │      ║ ────────────────────┤
          ║ id_enfermedad (FK)──────┼────────────┘      ║ id_enfermedad (FK)  ║
          ║ id_sintoma (FK)─────────┼──────────────────┨ id_sintoma (FK)     ║
          ║ (PK: enfermedad, sintoma│                  ║ (PK: enferm, sint)  ║
          ╚═════════════════════════╝                  └─────────────────────┘


          ╔════════════════════╗
          ║   pacientes        ║
          ║ ──────────────────┣ 1
          ║ id_paciente (PK)   ║ │
          ║ nombre             ║ │  :N
          ║ edad               ║ │
          ║ fecha_registro     ║ │
          ╚════════════════════╝ │
               ▲                 │
               │                 │
               │           ╔════════════════════════════╗
               │           ║   diagnosticos             ║
               └───────────┨ ──────────────────────────┣
                           ║ id_diagnostico (PK)       ║
                           ║ id_paciente (FK)──────────┘
                           ║ id_enfermedad (FK)────┐
                           ║ fecha_diagnostico     │
                           ║ observaciones         │
                           ╚════════════════════════════╝
                                   ▲                     ▲
                                   │ 1                   │
                                   │ │                   │
                                   │ └────M:N────────┐   │
                    ╔══════════════════════════════╗ │   │
                    ║ diagnostico_sintomas         ║◄┘   │
                    ║ ──────────────────────────────║     │
                    ║ id_diagnostico (FK)─────────┬┘     │
                    ║ id_sintoma (FK)───────┐     │      │
                    ║ (PK: diag, sintoma)   │     │      │
                    ╚═════────────────────────┼────┼─────┘
                                             │    │
                                             │    └─ Vincula síntomas
                                             │       del diagnóstico
                                             │
                                             └─ Vincula síntomas
                                                de la enfermedad
```

---

## 7️⃣ Leyenda de Símbolos

```
SIMBOLOGÍA USADA:
─────────────────────────────────────────

✓ = Completado / Éxito
✗ = Error / Fallo
⭐ = Importante / START HERE
📄 = Archivo
📁 = Carpeta
📊 = Datos / Gráfico
📞 = Contacto / Ayuda
💡 = Consejo / Tip
🚀 = Inicio / Lanzamiento
🧪 = Test / Prueba
🔗 = Enlace / Relación
🔌 = Conexión / Interface
⚙️ = Configuración
📝 = Documentación
🎯 = Objetivo
🐛 = Bug / Problema
1️⃣ 2️⃣ 3️⃣ = Pasos numerados
→ = Flecha derecha (proceso)
← = Flecha izquierda (retorno)
↓ ↑ = Flujo vertical
├─ = Rama (árbol)
└─ = Rama final (árbol)
```

---

**Última actualización: 8 de Diciembre de 2025**
