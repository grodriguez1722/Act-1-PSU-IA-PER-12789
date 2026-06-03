# Tablero Kanban

Aplicación web de gestión de tareas basada en la metodología Kanban. Permite crear tareas, organizarlas en columnas (**Pendiente**, **En Progreso** y **Completado**) mediante drag-and-drop y almacenar la información de forma persistente en una base de datos local.

## Tecnologías

* Backend: Java 17, Spring Boot, Spring Web, Spring Data JPA
* Base de datos: H2 Database
* Frontend: HTML5, CSS3 y JavaScript Vanilla
* Testing: JUnit 5, Spring Boot Test, MockMvc y H2 en memoria

## Requisitos previos

* Java JDK 17 o superior
* Apache Maven 3.9 o superior

## Ejecución de la aplicación

Desde la raíz del proyecto ejecutar:

```bash
./mvnw spring-boot:run
```

o alternativamente:

```bash
mvn spring-boot:run
```

Una vez iniciada la aplicación, abrir en el navegador:

```
http://localhost:8080/index.html
```

## Base de datos

La aplicación utiliza H2 Database para el almacenamiento de tareas.

La consola H2 puede habilitarse mediante la configuración correspondiente en `application.properties`.

## Entorno de pruebas

El proyecto incluye pruebas automatizadas para verificar las funcionalidades principales de la aplicación:

* Carga del contexto de Spring Boot.
* Creación de tareas.
* Consulta de tareas.
* Actualización de tareas.
* Eliminación de tareas.
* Asignación automática del estado `TODO`.
* Actualización parcial de atributos de una tarea.
* Gestión de errores para identificadores inexistentes.

### Ejecución de todas las pruebas

Ejecutar:

```bash
mvn test
```

### Limpieza y ejecución completa

```bash
mvn clean test
```

### Generación del informe de cobertura (si JaCoCo está configurado)

```bash
mvn clean verify
```

El informe de cobertura estará disponible en:

```
target/site/jacoco/index.html
```

## Estructura principal del proyecto

```text
src
├── main
│   ├── java/com/example/demo
│   │   ├── DemoApplication.java
│   │   ├── Task.java
│   │   ├── TaskController.java
│   │   └── TaskRepository.java
│   └── resources
│       ├── static
│       │   ├── index.html
│       │   ├── style.css
│       │   └── app.js
│       └── application.properties
└── test
    └── java/com/example/demo
        ├── DemoApplicationTests.java
        └── TaskControllerTest.java
```

## Funcionalidades

* Crear tareas.
* Editar tareas.
* Eliminar tareas.
* Mover tareas entre columnas mediante drag-and-drop.
* Persistencia de datos mediante H2.
* API REST para operaciones CRUD.
* Pruebas automatizadas de integración y funcionalidad.