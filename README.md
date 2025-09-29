# Lab1As - Sistema Bancario Full Stack

Sistema bancario completo con Spring Boot (backend) y Next.js (frontend).

## 🚀 Despliegue en Producción

### Paso 1: Configurar Base de Datos en Neon

1. Ve a [neon.tech](https://neon.tech) y crea una cuenta
2. Crea una nueva base de datos PostgreSQL
3. Copia la cadena de conexión que te proporciona Neon
4. La cadena se verá así: `postgresql://username:password@ep-xxx.neon.tech/dbname?sslmode=require`

### Paso 2: Desplegar en Vercel

1. **Sube tu código a GitHub:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/TU_USUARIO/lab1as.git
   git push -u origin main
   ```

2. **Conecta con Vercel:**
   - Ve a [vercel.com](https://vercel.com)
   - Conecta tu repositorio de GitHub
   - Selecciona tu proyecto `lab1as`

3. **Configurar Variables de Entorno en Vercel:**
   En la configuración del proyecto en Vercel, añade estas variables:
   ```
   DATABASE_URL=postgresql://username:password@ep-xxx.neon.tech/dbname?sslmode=require
   DATABASE_USERNAME=tu_username_neon
   DATABASE_PASSWORD=tu_password_neon
   SPRING_PROFILES_ACTIVE=production
   ```

4. **Configurar Build Settings:**
   - Framework Preset: Next.js
   - Build Command: `npm run build`
   - Output Directory: `frontend/.next`
   - Install Command: `npm install`

### Paso 3: Configuración Adicional

**Para el Backend (Spring Boot):**
- El backend se ejecutará automáticamente en Vercel usando `@vercel/java`
- Las tablas se crearán automáticamente en Neon la primera vez

**Para el Frontend (Next.js):**
- Se construirá automáticamente y se servirá desde Vercel
- La API apuntará automáticamente al backend en el mismo dominio

## 🛠️ Desarrollo Local

### Prerrequisitos
- Java 17+
- Node.js 18+
- PostgreSQL (o usar Neon también para desarrollo)

### Configuración
1. Clona el repositorio
2. Copia `.env.example` a `.env` y configura tus variables
3. Copia `frontend/.env.local.example` a `frontend/.env.local`

### Ejecutar en desarrollo
```bash
# Instalar dependencias del frontend
cd frontend && npm install

# Ejecutar todo (backend + frontend)
npm run dev
```

O ejecutar por separado:
```bash
# Backend (terminal 1)
./mvnw spring-boot:run

# Frontend (terminal 2)
cd frontend && npm run dev
```

## 📁 Estructura del Proyecto

```
lab1As/
├── src/                          # Backend Spring Boot
│   ├── main/java/com/udea/lab1As/
│   └── main/resources/
├── frontend/                     # Frontend Next.js
│   ├── src/
│   └── package.json
├── vercel.json                   # Configuración de Vercel
├── package.json                  # Scripts del proyecto
└── README.md
```

## 🌐 URLs después del despliegue

- **Aplicación completa:** `https://tu-proyecto.vercel.app`
- **API Backend:** `https://tu-proyecto.vercel.app/api`
- **Frontend:** `https://tu-proyecto.vercel.app`

## 🔧 Solución de Problemas

### Error de conexión a la base de datos
- Verifica que la URL de Neon sea correcta
- Asegúrate de que incluya `?sslmode=require` al final

### Error de CORS
- El archivo `CorsConfig.java` ya está configurado para producción
- Verifica que las URLs estén correctas

### Error de build
- Asegúrate de que todas las dependencias estén instaladas
- Verifica que las variables de entorno estén configuradas correctamente
