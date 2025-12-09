# Guía Rápida - Sistema Experto Médico

### Paso 1️⃣: Preparar Base de Datos

```bash
# Abrir terminal en la carpeta del proyecto
cd database

# Ejecutar scripts SQL en MySQL
mysql -u root < schema.sql
mysql -u root < data.sql

# Verificar que se creó
mysql -u root -e "USE sistema_experto_medico; SELECT COUNT(*) as enfermedades FROM enfermedades;"
```

**Resultado esperado:** `10` enfermedades

---

### Paso 2️⃣: Compilar y Ejecutar Java
#### Opción A: Windows (PowerShell)
```powershell
cd java
.\run.bat
```

#### Opción B: Linux/Mac (Terminal)
```bash
cd java
chmod +x run.sh
./run.sh
```

#### Opción C: Maven directo
```bash
cd java
mvn clean compile
mvn exec:java -Dexec.mainClass="com.sistemexperto.ui.MainWindow"
```

---

## 🎮 Usando la Aplicación

### Pantalla Principal
1. **Tab "Nuevo Diagnóstico"** ← Aquí trabajamos
2. **Tab "Historial"** - Ver diagnósticos previos
3. **Tab "Información"** - Detalles del proyecto

### Realizar Diagnóstico

```
1. Ingresa nombre del paciente:
   Nombre: Juan Pérez

2. Ingresa edad:
   Edad: 35

3. Selecciona síntomas de la LISTA IZQUIERDA:
   - fiebre
   - tos
   - (Haz clic "→ Agregar")

4. Presiona "OBTENER DIAGNÓSTICO"

5. ¡Ver resultados en el panel inferior!
   - Enfermedades encontradas
   - Síntomas coincidentes
   - Recomendaciones

6. (Opcional) Presiona "Guardar Diagnóstico"
   → Se guarda en MySQL automáticamente
```

---

## 📁 Estructura del Proyecto (Todo Lo Que Necesitas)

```
Proyecto_paradigmas/
├── database/
│   ├── schema.sql       ✓ Crea tablas MySQL
│   └── data.sql         ✓ Carga 10 enfermedades, 20 síntomas
│
├── prolog/
│   ├── diagnostico.pl   ← Motor lógico (no tocar)
│   └── test_diagnostico.pl ← Tests (no tocar)
│
└── java/                LA INTERFAZ VISUAL
    ├── pom.xml         ← Dependencias Maven
    ├── run.bat/run.sh  ← Ejecutar fácilmente
    ├── README.md       ← Documentación completa
    └── src/main/java/com/sistemexperto/
        ├── ui/         ← Interfaz gráfica
        ├── db/         ← Conexión MySQL
        ├── prolog/     ← Lógica diagnóstica
        └── models/     ← Clases de datos
```

---

## Checklist de Requisitos

- [ ] **Java 11+** instalado
  ```bash
  java -version
  # Resultado: java 11, 17, 21 o superior ✓
  ```

- [ ] **Maven 3.6+** instalado
  ```bash
  mvn -version
  # Resultado: Apache Maven 3.6 o superior ✓
  ```

- [ ] **MySQL 5.7+** ejecutándose
  ```bash
  mysql --version
  # Resultado: mysql Ver 5.7 o 8.0 ✓
  
  # Y ejecutándose:
  mysql -u root -e "SELECT 1"
  # Sin error = ✓
  ```

- [ ] **Base de datos creada**
  ```bash
  mysql -u root -e "USE sistema_experto_medico; SELECT * FROM v_enfermedades_completas LIMIT 1"
  # Sin error = ✓
  ```

---

## Casos de Prueba Recomendados

### Test 1: Diagnóstico Viral (Gripe)
```
Nombre: Juan Pérez
Edad: 35
Síntomas: fiebre, tos, dolor_cabeza, dolor_muscular

Resultados esperados:
✓ Gripe (viral)
✓ COVID-19 (viral)
✓ Faringitis (bacteriana)
```

### Test 2: Enfermedad Crónica
```
Nombre: María García
Edad: 45
Síntomas: cansancio, aumento_peso, piel_seca

Resultados esperados:
✓ Hipotiroidismo (cronica)
```

### Test 3: Alergia
```
Nombre: Carlos López
Edad: 28
Síntomas: estornudos, picazon, ojos_lagrimosos

Resultados esperados:
✓ Alergia (alergia)
✓ Resfriado (viral)
```

---

## Solucionar Problemas

### "Connection refused" a MySQL
```
Error: CommunicationsException
Solución:
1. Abre terminal nueva como administrador
2. Inicia MySQL: mysql -u root
3. Si falla, reinstala MySQL
4. Verifica puerto 3306 no esté bloqueado
```

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
```
Solución:
1. cd java
2. mvn clean dependency:resolve
3. mvn compile
```

### Síntomas no aparecen en la lista
```
Solución:
1. Verifica data.sql se ejecutó:
   mysql -u root sistema_experto_medico
   mysql> SELECT COUNT(*) FROM sintomas;
   # Debe retornar 20

2. Si no, ejecuta de nuevo:
   mysql -u root < data.sql
```

### "Port 3306 already in use"
```
Solución (Windows):
netstat -ano | findstr :3306
taskkill /PID <PID> /F

Solución (Linux/Mac):
lsof -i :3306
kill -9 <PID>
```

---

## Archivos Importantes

| Archivo | Líneas | Propósito |
|---------|--------|----------|
| `java/pom.xml` | 130 | Configuración Maven + dependencias |
| `java/src/main/java/.../MainWindow.java` | 130 | Ventana principal |
| `java/src/main/java/.../DiagnosticoPanel.java` | 260 | Panel de diagnósticos (IMPORTANTE) |
| `java/src/main/java/.../DatabaseConnection.java` | 170 | Conexión MySQL |
| `java/src/main/java/.../PrologEngine.java` | 130 | Lógica de diagnóstico |
| `database/schema.sql` | 90 | Esquema BD (tablas + vistas) |
| `database/data.sql` | 70 | Datos iniciales |
| `prolog/diagnostico.pl` | 53 | Motor Prolog |

---

## 🔗 Flujo Visual Simplificado

```
┌─────────────┐
│  Usuario    │
│ (interfaz)  │
└──────┬──────┘
       │
       ▼
┌──────────────────────┐
│  Java Swing UI       │
│ (Main + Panels)      │
└──────┬───────────────┘
       │
       ├──→ PrologEngine (Lógica)
       │
       ├──→ DatabaseConnection (MySQL)
       │
       ▼
┌──────────────────────┐
│   Base de Datos      │
│   MySQL              │
│ (Enfermedades,       │
│  Síntomas,           │
│  Diagnósticos)       │
└──────────────────────┘
```

---

## Tips y Trucos

### Compilar una sola vez
Si compilaste con `mvn clean compile`, luego puedes ejecutar más rápido:
```bash
mvn exec:java -Dexec.mainClass="com.sistemexperto.ui.MainWindow"
```

### Crear JAR ejecutable standalone
```bash
cd java
mvn clean package
java -jar target/sistema-experto-medico-1.0.0-jar-with-dependencies.jar
```

### Ver qué enfermedades hay en BD
```bash
mysql -u root sistema_experto_medico
mysql> SELECT nombre, categoria FROM enfermedades;
```

### Limpiar diagnósticos anteriores (opcional)
```bash
mysql -u root sistema_experto_medico
mysql> DELETE FROM diagnostico_sintomas;
mysql> DELETE FROM diagnosticos;
mysql> DELETE FROM pacientes;
```

---


