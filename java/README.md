# Sistema Experto Médico - Interfaz Java Swing

## 📋 Descripción

Interfaz visual completa en **Java Swing** que conecta con:
- **Base de datos MySQL** (`database/schema.sql`, `database/data.sql`)
- **Motor de inferencia Prolog** (`prolog/diagnostico.pl`)

## 🏗️ Estructura del Proyecto

```
java/
├── pom.xml                          # Configuración Maven
├── src/main/java/com/sistemexperto/
│   ├── ui/
│   │   ├── MainWindow.java          # Ventana principal con tabs
│   │   ├── DiagnosticoPanel.java    # Panel para realizar diagnósticos
│   │   └── HistorialPanel.java      # Panel de historial de diagnósticos
│   ├── db/
│   │   └── DatabaseConnection.java  # Gestión de conexión MySQL
│   ├── prolog/
│   │   └── PrologEngine.java        # Motor de inferencia simulado
│   └── models/
│       ├── Enfermedad.java          # Modelo de enfermedad
│       └── Diagnostico.java         # Modelo de diagnóstico
└── README.md                        # Este archivo
```

## 📦 Requisitos Previos

1. **Java 11+**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 5.7+** (ejecutándose)
   ```bash
   mysql --version
   ```

4. **Base de datos creada y poblada**
   ```bash
   mysql -u root < database/schema.sql
   mysql -u root < database/data.sql
   ```

## 🔧 Configuración

### 1. Verificar credenciales de BD

En `DatabaseConnection.java`, ajusta si es necesario:
```java
private static final String HOST = "localhost";
private static final String PORT = "3306";
private static final String DATABASE = "sistema_experto_medico";
private static final String USER = "root";
private static final String PASSWORD = "";  // Sin contraseña por defecto
```

### 2. Crear la base de datos

```bash
# En la raíz del proyecto:
cd database
mysql -u root -p < schema.sql
mysql -u root -p < data.sql
```

O ejecutar manualmente en MySQL:
```sql
CREATE DATABASE sistema_experto_medico;
USE sistema_experto_medico;
-- Copiar contenido de schema.sql
-- Copiar contenido de data.sql
```

## 🚀 Compilación y Ejecución

### Opción 1: Usar Maven desde línea de comandos

```bash
# Navegar a la carpeta java/
cd java

# Compilar
mvn clean compile

# Ejecutar
mvn exec:java -Dexec.mainClass="com.sistemexperto.ui.MainWindow"

# O crear JAR ejecutable
mvn clean package
java -jar target/sistema-experto-medico-1.0.0-jar-with-dependencies.jar
```

### Opción 2: Usar IDE (IntelliJ IDEA, Eclipse, VS Code)

1. Abrir la carpeta `java/` como proyecto Maven
2. Maven debería descargar automáticamente las dependencias
3. Ejecutar `MainWindow.java` como clase principal

### Opción 3: Compilar manualmente

```bash
cd java
javac -cp "src/main/java:lib/*" src/main/java/com/sistemexperto/ui/MainWindow.java
java -cp "src/main/java:lib/*" com.sistemexperto.ui.MainWindow
```

## 💻 Interfaz de Usuario

### Pestaña 1: Nuevo Diagnóstico
- **Datos del Paciente**: Nombre y edad
- **Selector de Síntomas**: 
  - Lista de síntomas disponibles
  - Botones para agregar/remover
  - Lista de síntomas seleccionados
- **Botón "OBTENER DIAGNÓSTICO"**: Consulta el motor Prolog
- **Resultados**: Muestra enfermedades probables con recomendaciones
- **Botón "Guardar Diagnóstico"**: Registra en MySQL

### Pestaña 2: Historial
- Vista previa de diagnósticos (por expandir en futuras versiones)

### Pestaña 3: Información
- Descripción general del sistema
- Advertencias legales
- Tecnologías utilizadas

## 🔌 Integración con Componentes

### Conexión MySQL
```java
DatabaseConnection db = new DatabaseConnection();
db.conectar();
List<Enfermedad> enfermedades = db.obtenerEnfermedades();
db.registrarDiagnostico(...);
db.desconectar();
```

### Motor Prolog (Simulado en Java)
```java
PrologEngine prolog = new PrologEngine();
prolog.cargarEnfermedades(enfermedades);
prolog.cargarSintomas(sintomas);

// Predicados disponibles:
List<Enfermedad> resultados = prolog.diagnostico([síntomas]);
List<Enfermedad> cronicas = prolog.enfermedadesCronicas();
String rec = prolog.recomendacion("gripe");
```

## 📊 Flujo de Datos

```
[Usuario: Nombre, Edad, Síntomas]
           ↓
    [MySQL: Carga datos]
           ↓
   [PrologEngine: Procesa síntomas]
           ↓
    [Resultados: Enfermedades + Recomendaciones]
           ↓
  [Opcional: Guardar en MySQL diagnosticos]
```

## 🧪 Pruebas Básicas

### Test 1: Diagnóstico de Gripe
1. Nombre: Juan Pérez
2. Edad: 35
3. Síntomas: fiebre, tos
4. Resultado esperado: Gripe, COVID-19, Faringitis

### Test 2: Diagnóstico de Alergia
1. Síntomas: estornudos, picazon, ojos_lagrimosos
2. Resultado esperado: Alergia

### Test 3: Diagnóstico Crónico
1. Síntomas: cansancio, aumento_peso, piel_seca
2. Resultado esperado: Hipotiroidismo

## 📋 Dependencias Maven

- **mysql-connector-java 8.0.33**: Conexión a MySQL
- **jpl 8.2.0**: Interfaz Java para SWI-Prolog (futuro: integración real)
- **slf4j**: Logging
- **junit 4.13.2**: Testing

## 🔮 Mejoras Futuras

- [ ] Integración real con SWI-Prolog (JPL)
- [ ] Panel de historial con tabla filtrable
- [ ] Estadísticas de diagnósticos
- [ ] Exportación a PDF
- [ ] Búsqueda avanzada de síntomas
- [ ] Análisis de confianza de diagnóstico
- [ ] Ponderación de síntomas críticos
- [ ] Análisis por edad/género
- [ ] Sistema de usuarios y roles
- [ ] REST API

## 🐛 Solución de Problemas

### "Connection refused" a MySQL
```
Error: com.mysql.cj.jdbc.exceptions.CommunicationsException
```
- Verificar que MySQL está ejecutándose
- Verificar credenciales en `DatabaseConnection.java`
- Ejecutar `mysql -u root` para verificar acceso

### "ClassNotFoundException" para MySQL driver
```
mvn clean compile
mvn dependency:resolve
```

### Síntomas no aparecen en la lista
- Verificar que `data.sql` se ejecutó correctamente
- Ejecutar en MySQL: `SELECT COUNT(*) FROM sintomas;`

## 📚 Referencias

- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [MySQL JDBC Driver](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [SWI-Prolog JPL](https://www.swi-prolog.org/pldoc/doc_for?object=section(%27packages/jpl%27))

## 📝 Notas

- El motor Prolog está simulado en Java por simplicidad
- Para una integración real con SWI-Prolog, usar la librería JPL
- Todos los diagnósticos se registran en `diagnosticos` tabla

## 👨‍💼 Autor

Desarrollado como proyecto de Paradigmas de Programación - Diciembre 2025

## ⚖️ Disclaimer

**ADVERTENCIA LEGAL**: Este sistema es educativo y no debe utilizarse para 
diagnósticos médicos reales. Siempre consulta con un profesional de la salud 
certificado.

---

**¿Necesita ayuda?** Revisa los comentarios en el código fuente.
