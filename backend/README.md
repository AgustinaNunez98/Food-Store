# Food Store — Backend

Aplicación de consola desarrollada en Java para el Trabajo Práctico Integrador de Programación III.
Usa JPA con Hibernate para guardar los datos en una base H2 local y Gradle para gestionar las dependencias 
y compilar el proyecto.

## Funcionalidades

- Alta, modificación, baja lógica y listado de categorías y productos.
- Validación de precio y stock de los productos.
- Consulta JPQL de productos activos por categoría.
- Persistencia de datos en H2 entre ejecuciones.

## Requisitos

- JDK 21.
- Conexión a internet para la primera descarga de Gradle y sus dependencias.

## Compilación desde la terminal

Desde la carpeta `backend/`, ejecutá:

```bash
./gradlew compileJava
```

En Windows, usá `.\gradlew.bat compileJava`. La tarea `compileJava` verifica que el código Java compile.

## Ejecución del menú en IntelliJ IDEA

1. Abrí la carpeta principal del TPI.
2. Vinculá `backend/build.gradle` como proyecto Gradle y seleccioná un **JDK 21** en **Gradle JVM**.
3. Abrí `backend/src/main/java/com/tp/jpa/Main.java` y ejecutá `com.tp.jpa.Main`.
4. En la configuración de ejecución de `Main`, seleccioná **`backend/`** como **Working directory**.
5. Usá la consola para gestionar categorías y productos, o consultar el reporte de productos por categoría.

## Base de datos

La configuración de JPA está en `src/main/resources/META-INF/persistence.xml`. Con `backend/` como directorio de 
trabajo, H2 guarda la base en `backend/data/jpa_db.mv.db` y la reutiliza en las siguientes ejecuciones.
H2 funciona dentro de la aplicación, sin un servidor de base de datos aparte.