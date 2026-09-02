# 📦 Logitrack - API REST Backend & Sistema de Inventario

> Sistema de gestión logística y control de inventarios desarrollado con **Java Spring Boot**, **Spring Security (JWT)**, **MySQL** y documentado con **OpenAPI/Swagger**. Este repositorio incluye tanto la arquitectura del backend como una interfaz frontend de pruebas (*Vanilla JS*).

---

## 📑 Tabla de Contenidos
1. [Descripción del Proyecto](#-descripción-del-proyecto)
2. [Arquitectura y Tecnologías](#-arquitectura-y-tecnologías)
3. [Estructura del Proyecto](#-estructura-del-proyecto)
4. [Instrucciones de Instalación y Ejecución](#-instrucciones-de-instalación-y-ejecución)
5. [Ejemplos de Endpoints](#-ejemplos-de-endpoints)
6. [Documentación y Capturas (Swagger)](#-documentación-y-capturas-swagger)
7. [Frontend de Consumo](#-frontend-de-consumo)

---

## 🚀 1. Descripción del Proyecto
**Logitrack** es un backend robusto diseñado para entornos académicos y empresariales que permite administrar de forma segura:
* **Usuarios y Roles:** Control de acceso basado en roles con autenticación stateless mediante **JSON Web Tokens (JWT)**.
* **Bodegas y Catálogos:** Administración de ubicaciones físicas y un catálogo maestro de productos clasificados por categorías.
* **Control de Stock (Clave Compuesta):** Gestión de existencias asociando catálogos con bodegas específicas.
* **Auditoría Avanzada:** Registro y consulta de transacciones manuales y automáticas (gestionadas mediante triggers/base de datos o servicios dedicados) para `audit_transacciones`, `audit_usuarios` y `audit_general`.

---

## 🛠️ 2. Arquitectura y Tecnologías
* **Java 17+**
* **Spring Boot 3.x**
* **Spring Data JPA / Hibernate**
* **Spring Security** (Filtros JWT personalizados y configuración CORS)
* **MySQL Database**
* **Lombok** (Reducción de boilerplate)
* **SpringDoc OpenAPI 3 / Swagger** (Documentación interactiva)
* **Maven** (Gestor de dependencias)

---

## 📂 3. Estructura del Proyecto

```text
projectD1Campuslands/
│
├── src/main/java/com/miempresa/proyecto/
│   ├── controller/      # Controladores REST (@RestController)
│   ├── service/         # Lógica de negocio (Interfaces y ServiceImpl)
│   ├── repository/      # Interfaces Spring Data JPA
│   ├── entity/          # Modelos de persistencia mapeados a tablas MySQL
│   ├── dto/             # Objetos de transferencia (Request / Response Records)
│   ├── mapper/          # Mapeadores de Entidad <-> DTO
│   ├── exception/       # Manejador global de excepciones (@RestControllerAdvice)
│   └── security/        # Configuración de seguridad y filtros JWT
│
├── src/main/resources/
│   └── application.properties # Configuración de conexión y perfiles
│
├── frontend/            # Interfaz Web estática (HTML / CSS / JS)
│   ├── index.html
│   ├── style.css
│   └── app.js
│
└── README.md
```

---

## ⚙️ 4. Instrucciones de Instalación y Ejecución
Prerrequisitos
- Tener instalado Java JDK 17 o superior.

- Tener instalado Maven.

- Servidor MySQL corriendo localmente o en contenedor.

Pasos para levantar el Backend:
 - Clonar el repositorio:

```Bash
git clone [https://github.com/Shiroses/ProyectoSpringBootSantiagoDominguez.git](https://github.com/Shiroses/ProyectoSpringBootSantiagoDominguez.git)
```

Configurar la Base de Datos:

Carga el script de MySQL LogiTrack

Modifica las credenciales en src/main/resources/application.properties:

Properties
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack
spring.datasource.username=usr_api_service
spring.datasource.password=BackendPass123!
Compilar y Ejecutar la aplicación:

```Bash
mvn clean spring-boot:run
```

El servidor se levantará por defecto en el puerto 8080.

🔌 5. Ejemplos de Endpoints
🔐 Autenticación
POST /auth/login

Descripción: Autentica las credenciales de un usuario y retorna un token JWT válido.

Body (JSON):

JSON
{
  "email": "admin@logitrack.com",
  "password": "password123"
}
📦 Productos / Stock en Bodegas
GET /api/v1/productos

Descripción: Retorna la lista completa del stock en todas las bodegas.

POST /api/v1/productos

Descripción: Asigna stock inicial de un producto de catálogo a una bodega.

Body (JSON):

JSON
{
  "idCatalogo": 1,
  "idBodega": 2,
  "stock": 50
}
🔍 Auditorías (Solo Lectura)
GET /api/v1/auditoria-general

Descripción: Consulta los eventos generales registrados por la base de datos (categorías, bodegas, catálogos).

📸 6. Documentación y Capturas (Swagger)
Una vez iniciado el proyecto, puedes acceder a la interfaz interactiva de Swagger UI para probar directamente las peticiones HTTP desde el navegador:

👉 http://localhost:8080/swagger-ui/index.html

💻 7. Frontend de Consumo
El repositorio incluye una carpeta frontend/ construida con HTML5, CSS3 y JavaScript puro (Vanilla JS) configurada para consumir los endpoints protegidos con JWT de esta API respetando políticas de CORS.

Cómo ejecutar el Frontend:
Abre la carpeta frontend/ en tu editor (ej. VS Code).

Utiliza la extensión Live Server para levantar el archivo index.html, o abre el archivo directamente en tu navegador (configurado para apuntar a http://localhost:8080).

Asegúrate de iniciar sesión mediante el formulario para almacenar el token JWT en las cabeceras de autorización de las peticiones subsiguientes.
