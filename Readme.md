# 🚀 Applicant Management System

Sistema backend reactivo para la **gestión de clientes **, construido con **Java 21** y **Spring WebFlux**, siguiendo principios modernos de arquitectura reactiva, seguridad con JWT y despliegue automatizado en AWS mediante CI/CD.

🌍 **Disponible públicamente en AWS Elastic Beanstalk (DEV):**  
👉 https://Applicant-managment-system-env.eba-8gd6b6c3.us-east-1.elasticbeanstalk.com

---

## 🧠 Descripción del proyecto

**Applicant Management System** es una API REST reactiva diseñada para:
- Gestionar información de clientes
- Exponer endpoints seguros y no bloqueantes
- Servir como base para sistemas de reclutamiento o gestión de talento

El proyecto está orientado a **bajo consumo de recursos**, **alta escalabilidad** y **buenas prácticas de seguridad y despliegue**.

---

## 🛠️ Stack Tecnológico

### Backend
- ☕ **Java 21**
- 🌱 **Spring Boot**
- ⚡ **Spring WebFlux** (100% reactivo)
- 🧵 **R2DBC** (acceso reactivo a base de datos)
- 🔐 **JWT** para autenticación y autorización
- 📄 **SpringDoc / OpenAPI (Swagger)**

### Infraestructura & DevOps
- ☁️ **AWS Elastic Beanstalk** (Single Instance – bajo costo)
- 🐙 **GitHub Actions** (CI/CD)
- 🔐 **OIDC GitHub → AWS** (sin llaves hardcodeadas)
- 📦 **Maven**

---

## 🔐 Seguridad

- Autenticación basada en **JWT**
- Secretos y credenciales **NO están en el repositorio**
- Configuración sensible gestionada vía **variables de entorno**
- Separación clara de perfiles (`dev`, `prod`)

---

## 📚 Endpoints disponibles (resumen)

> Base URL (AWS – DEV):  
> `https://Applicant-managment-system-env.eba-8gd6b6c3.us-east-1.elasticbeanstalk.com`

### 🔎 Salud y monitoreo
| Método | Endpoint | Descripción |
|------|---------|-------------|
| GET | `/actuator/health` | Estado de salud de la aplicación |
| GET | `/actuator/info` | Información básica del servicio |

---

### 🧾 Documentación API
| Método | Endpoint | Descripción |
|------|---------|-------------|
| GET | `/swagger` | UI Swagger (OpenAPI) |
| GET | `/v3/api-docs` | Definición OpenAPI en JSON |

---

### 👤 clients (ejemplo funcional)
> *(Los nombres pueden variar según tu implementación concreta)*

| Método | Endpoint | Descripción |
|------|---------|-------------|
| GET | `/api/clients` | Obtener todos los clientes |
| GET | `/api/clients/{id}` | Obtener cliente por ID |
| POST | `/api/clients` | Crear nuevo cliente |
| PUT | `/api/clients/{id}` | Actualizar cliente |
| DELETE | `/api/clients/{id}` | Eliminar cliente |

---

## 🧪 Cómo probar el proyecto en local

### 📋 Requisitos
- ☕ **Java 21**
- 📦 **Maven 3.9+**
- 🐬 Base de datos compatible con **R2DBC** (MySQL o PostgreSQL)
- 🔧 Git

---

### ⚙️ Variables de entorno necesarias

Antes de ejecutar en local, define:

```bash
SPRING_PROFILES_ACTIVE=dev
SPRING_R2DBC_URL=r2dbc:mysql://localhost:3306/tu_db
SPRING_R2DBC_USERNAME=tu_usuario
SPRING_R2DBC_PASSWORD=tu_password
JWT_SECRET=tu_secreto_jwt

▶️ Ejecutar en local
mvn clean spring-boot:run

### 🌐 URLs de prueba en local
http://localhost:8080

Swagger:
http://localhost:8080/swagger


☁️ Despliegue en AWS (DEV)

Desplegado en AWS Elastic Beanstalk

Tipo: Single Instance

Plataforma: Java SE – Corretto 21 – Amazon Linux 2023

Despliegue automático vía GitHub Actions

🌍 URL pública:

👉 https://Applicant-managment-system-env.eba-8gd6b6c3.us-east-1.elasticbeanstalk.com

👨‍💻 Autor

Desarrollado con ❤️ por DevPull

Siéntete libre de clonar, probar o proponer mejoras 🚀