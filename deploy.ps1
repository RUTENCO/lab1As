Write-Host "🚀 Preparando despliegue..." -ForegroundColor Green

# Crear directorio .vercel si no existe
if (!(Test-Path ".vercel")) {
    New-Item -ItemType Directory -Path ".vercel"
}

# Instalar dependencias del frontend
Write-Host "📦 Instalando dependencias del frontend..." -ForegroundColor Yellow
Set-Location frontend
npm install

# Construir el frontend
Write-Host "🔨 Construyendo frontend..." -ForegroundColor Yellow
npm run build

# Volver al directorio raíz
Set-Location ..

# Construir el backend
Write-Host "🔨 Construyendo backend..." -ForegroundColor Yellow
.\mvnw.cmd clean package -DskipTests

Write-Host "✅ Proyecto listo para despliegue!" -ForegroundColor Green
Write-Host ""
Write-Host "Siguiente paso:" -ForegroundColor Cyan
Write-Host "1. Sube tu código a GitHub" -ForegroundColor White
Write-Host "2. Conecta el repositorio con Vercel" -ForegroundColor White
Write-Host "3. Configura las variables de entorno en Vercel" -ForegroundColor White
Write-Host "4. ¡Despliega!" -ForegroundColor White
