#!/bin/bash

echo "🚀 Preparando despliegue..."

# Crear directorio .vercel si no existe
mkdir -p .vercel

# Instalar dependencias del frontend
echo "📦 Instalando dependencias del frontend..."
cd frontend && npm install

# Construir el frontend
echo "🔨 Construyendo frontend..."
npm run build

# Volver al directorio raíz
cd ..

# Construir el backend
echo "🔨 Construyendo backend..."
./mvnw clean package -DskipTests

echo "✅ Proyecto listo para despliegue!"
echo ""
echo "Siguiente paso:"
echo "1. Sube tu código a GitHub"
echo "2. Conecta el repositorio con Vercel"
echo "3. Configura las variables de entorno en Vercel"
echo "4. ¡Despliega!"
