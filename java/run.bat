@echo off
REM Script para compilar y ejecutar el Sistema Experto Medico en Windows

echo ========================================
echo Sistema Experto Medico - Compilacion
echo ========================================
echo.

REM Verificar que estamos en la carpeta java
if not exist "pom.xml" (
    echo Error: pom.xml no encontrado
    echo Asegurate de ejecutar este script desde la carpeta java/
    pause
    exit /b 1
)

REM Limpiar y compilar
echo Compilando proyecto...
call mvn clean compile
if %errorlevel% neq 0 (
    echo Error en la compilacion
    pause
    exit /b 1
)

REM Ejecutar
echo.
echo Iniciando aplicacion...
call mvn exec:java -Dexec.mainClass="com.sistemexperto.ui.MainWindow"

pause
