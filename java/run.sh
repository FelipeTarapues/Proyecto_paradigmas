#!/bin/bash
# Script para compilar y ejecutar el Sistema Experto Medico en Linux/Mac

echo "========================================"
echo "Sistema Experto Medico - Compilacion"
echo "========================================"
echo

# Verificar que estamos en la carpeta java
if [ ! -f "pom.xml" ]; then
    echo "Error: pom.xml no encontrado"
    echo "Asegurate de ejecutar este script desde la carpeta java/"
    exit 1
fi

# Limpiar y compilar
echo "Compilando proyecto..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Error en la compilacion"
    exit 1
fi

# Ejecutar
echo
echo "Iniciando aplicacion..."
mvn exec:java -Dexec.mainClass="com.sistemexperto.ui.MainWindow"
