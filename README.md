# 🏦 Sistema Bancario - Lab1As

Sistema bancario que permite la gestión de clientes y transacciones financieras con una interfaz web moderna y responsiva.

## 📋 Funcionalidades

- **Gestión de Clientes**: Crear, leer, actualizar y eliminar clientes bancarios
- **Transacciones**: Realizar transferencias entre cuentas con validación de saldos
- **Historial**: Consultar el historial completo de transacciones por cuenta
- **Interfaz Responsiva**: Optimizada para dispositivos móviles y escritorio
- **Validación de Fechas**: Manejo correcto de fechas sin problemas de zona horaria

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.6** - Framework de aplicación
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos relacional
- **Maven** - Gestión de dependencias
- **Hibernate** - ORM (Object-Relational Mapping)
- **Lombok** - Reducción de código boilerplate

### Frontend
- **Next.js 15.5.4** - Framework de React
- **React 19.1.0** - Biblioteca de interfaz de usuario
- **TypeScript 5** - Tipado estático
- **Tailwind CSS 4** - Framework de estilos
- **Axios** - Cliente HTTP
- **React Hot Toast** - Notificaciones
- **Lucide React** - Iconos

### Base de Datos
- **Neon PostgreSQL** - Base de datos en la nube
- **Conexión SSL** - Seguridad en la conexión

## 🚀 Plataformas de Despliegue

### 🌐 Producción
- **Frontend**: [Vercel](https://vercel.com) - Plataforma de despliegue para aplicaciones frontend
- **Backend**: [Render](https://render.com) - Plataforma de despliegue para aplicaciones backend
- **Base de Datos**: [Neon](https://neon.tech) - PostgreSQL serverless en la nube

### 🔗 URLs de Producción
- **Aplicación Web**: `https://lab1as-frontend.vercel.app`
- **API Backend**: `https://lab1as.onrender.com/api`
- **Base de Datos**: Neon PostgreSQL (conexión privada)

## 🏗️ Arquitectura

```
┌─────────────────┐    HTTPS    ┌─────────────────┐    HTTPS    ┌─────────────────┐
│                 │ -----------> │                 │ -----------> │                 │
│  Frontend       │              │  Backend        │              │  Database       │
│  (Vercel)       │              │  (Render)       │              │  (Neon)         │
│  Next.js        │              │  Spring Boot    │              │  PostgreSQL     │
└─────────────────┘              └─────────────────┘              └─────────────────┘
```

## 📊 Características Técnicas

- **Arquitectura RESTful** - API bien estructurada con endpoints claros
- **CORS Configurado** - Permite comunicación entre dominios diferentes
- **Responsive Design** - Adaptado para móviles, tablets y escritorio
- **Manejo de Errores** - Validaciones tanto en frontend como backend
- **Logging Avanzado** - Sistema de logs con colores y formato mejorado
- **Dockerización** - Backend containerizado para fácil despliegue
