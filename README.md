[![CI/CD Pipeline](https://github.com/josealbornozvu/lab1v2026/actions/workflows/build.yml/badge.svg)](https://github.com/josealbornozvu/lab1v2026/actions/workflows/build.yml)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=coverage)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Maintainability issues](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=software_quality_maintainability_issues)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=josealbornozvu_lab1v2026&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=josealbornozvu_lab1v2026)

# lab2v2026

Implementation of a Simple App with the next operations:

* Get random nations
* Get random currencies
* Get random Aircraft
* Get application version
* health check

Including integration with GitHub Actions, Sonarqube (SonarCloud), Coveralls and Snyk

### Folders Structure

In the folder `src` is located the main code of the app

In the folder `test` is located the unit tests

### How to install it

Execute:

  ```shell
  $ mvnw spring-boot:run
  ```
to download the node dependencies

### How to test it

Execute:

  ```shell
  $ mvnw clean install
  ```

### How to get coverage test

Execute:

  ```shell
  $ mvwn -B package -DskipTests --file pom.xml
  ```

Banco UA — Laboratorio de CI/CD con Spring Boot

Aplicación bancaria sencilla con un backend REST en Spring Boot y un frontend en React. Permite administrar clientes, transferir dinero entre cuentas y consultar el historial de transacciones de una cuenta.

El proyecto incluye un pipeline de integración y despliegue continuo con GitHub Actions, análisis de calidad con SonarCloud, medición de cobertura con JaCoCo, publicación de la imagen en Docker Hub y despliegue del backend en Railway.

Contenido
Funcionalidades
Tecnologías
Estructura del repositorio
Ejecutar en local
API del backend
Pruebas y cobertura
Pipeline CI/CD
Flujo de ramas
Docker
Despliegue en Railway
Funcionalidades
Crear, listar, consultar, actualizar y eliminar clientes.
Transferir dinero entre dos cuentas, validando que existan y que haya saldo suficiente.
Consultar el historial de transacciones de una cuenta (enviadas y recibidas).
Documentación interactiva de la API con Swagger UI.
Tecnologías
Capa	Tecnologías
Backend	Java 21, Spring Boot 3.5, Spring Data JPA, MapStruct, Lombok, springdoc-openapi
Base de datos	MySQL 8
Frontend	React (Create React App), Axios, lucide-react
Pruebas	JUnit 5, Mockito, JaCoCo
CI/CD	GitHub Actions, SonarCloud, Docker, Docker Hub, Railway
Estructura del repositorio

Es un monorepo: backend y frontend viven en carpetas hermanas dentro del mismo repositorio.

text
.
├── .github/workflows/build.yml   # Pipeline CI/CD
├── lab1v2026/                    # Backend (Spring Boot)
│   ├── src/main/java             # Código fuente (controller, service, repository, entity, DTO, mapper)
│   ├── src/test/java             # Pruebas unitarias
│   ├── Dockerfile
│   └── pom.xml
└── frontend/                     # Frontend (React)
├── src/api                   # Cliente HTTP (Axios)
└── src/components            # Clientes, transferencias e historial
Ejecutar en local
Requisitos
JDK 21
MySQL 8 en ejecución
Node.js (para el frontend)
Git
1. Base de datos

Crea el esquema una sola vez (las tablas las crea Hibernate al arrancar):

sql
CREATE DATABASE lab1v2026;
2. Backend

La contraseña de MySQL no se guarda en el repositorio: se entrega por variable de entorno.

En PowerShell (Windows):

powershell
cd lab1v2026
$env:SPRING_DATASOURCE_PASSWORD='tu_contraseña'
.\mvnw spring-boot:run

En Linux o macOS:

shell
cd lab1v2026
export SPRING_DATASOURCE_PASSWORD='tu_contraseña'
./mvnw spring-boot:run

El backend queda en http://localhost:8080.

3. Frontend
   shell
   cd frontend
   npm install
   npm start

El frontend queda en http://localhost:3000 y consume http://localhost:8080/api.

API del backend

Swagger UI: http://localhost:8080/swagger-ui/index.html

Clientes
Método	Ruta	Descripción
GET	/api/customers	Lista todos los clientes
GET	/api/customers/{id}	Consulta un cliente
POST	/api/customers	Crea un cliente
PUT	/api/customers/{id}	Actualiza un cliente
DELETE	/api/customers/{id}	Elimina un cliente

Ejemplo de cuerpo para crear un cliente:

json
{
"firstName": "Ana",
"lastName": "Perez",
"accountNumber": "1001",
"balance": 500.0
}
Transacciones
Método	Ruta	Descripción
POST	/api/transactions	Transfiere dinero entre dos cuentas
GET	/api/transactions/{accountNumber}	Historial de una cuenta

Ejemplo de cuerpo para transferir:

json
{
"senderAccountNumber": "1001",
"receiverAccountNumber": "1002",
"amount": 50.0
}

Si la cuenta no existe o el saldo es insuficiente, la API responde 400 con el motivo.

Pruebas y cobertura

Ejecutar las pruebas y generar el reporte de cobertura (desde la carpeta lab1v2026):

shell
./mvnw clean verify

El reporte de JaCoCo queda en lab1v2026/target/site/jacoco/index.html. La cobertura también se publica en SonarCloud en cada ejecución del pipeline.

Pipeline CI/CD

El workflow .github/workflows/build.yml tiene cuatro trabajos encadenados:

Trabajo	Qué hace	Cuándo corre
tests	Ejecuta las pruebas unitarias con un MySQL temporal	Push a main o feature/**, y pull requests
sonar	Compila, mide cobertura y analiza la calidad en SonarCloud	Igual que tests, si tests pasa
build	Genera el JAR y lo guarda como artefacto; en main publica la imagen en Docker Hub	Igual que sonar, si sonar pasa
deploy	Despliega el backend en Railway	Solo en push a main, si build pasa
Flujo de ramas

Se trabaja con trunk-based development: una rama principal (main) y ramas cortas feature/* que se integran rápido mediante pull request.

Crear una rama corta desde main: git checkout -b feature/mi-cambio.
Hacer commits pequeños y subir la rama. El pipeline valida pruebas, calidad y build.
Abrir un pull request hacia main y esperar que todos los checks pasen.
Hacer merge. El pipeline corre sobre main, publica la imagen y despliega.
Borrar la rama.
Docker

La imagen del backend se publica en Docker Hub en cada cambio de main:

shell
docker pull jalbornozv/lab1v2026:latest

Para ejecutarla necesita una base MySQL accesible. Ejemplo usando el MySQL de tu máquina con Docker Desktop:

shell
docker run -p 8080:8080 \
-e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/lab1v2026?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
-e SPRING_DATASOURCE_USERNAME=root \
-e SPRING_DATASOURCE_PASSWORD=tu_contraseña \
jalbornozv/lab1v2026:latest
Despliegue en Railway

El backend se despliega automáticamente en Railway desde el trabajo deploy del pipeline, junto con una base MySQL del mismo proyecto.

URL pública: https://lab1v2026-production.up.railway.app
Swagger en la nube: https://lab1v2026-production.up.railway.app/swagger-ui/index.html

Configuración del servicio en Railway (variables de entorno, sin valores secretos aquí):

Variable	Origen
SPRING_DATASOURCE_URL	Construida con MYSQLHOST, MYSQLPORT y MYSQLDATABASE del servicio MySQL
SPRING_DATASOURCE_USERNAME	MYSQLUSER del servicio MySQL
SPRING_DATASOURCE_PASSWORD	MYSQLPASSWORD del servicio MySQL

El puerto lo inyecta Railway mediante la variable PORT, que la aplicación lee con server.port=${PORT:8080}.

Secretos necesarios en GitHub (Settings → Secrets and variables → Actions): SONAR_TOKEN, DOCKER_USERNAME, DOCKER_PASSWORD y RAILWAY_TOKEN.

Autor

Jose Fernando Albornoz — Arquitectura de Software, Universidad de Antioquia.