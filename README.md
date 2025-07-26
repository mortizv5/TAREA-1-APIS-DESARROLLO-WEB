# TAREA-1-APIS-DESARROLLO-WEB

Este proyecto en Java tiene como objetivo consumir una API que genera reportes jerárquicos de tareas (`taskReport`), procesar recursivamente los datos, calcular métricas como eficiencia y cantidad de procesos, y enviar una evaluación a otra API (`taskReportVerification`).

Nombre: Marvin Ortiz  
Carnet: 5190-22-6064  
Sección: 5  

---

## Tecnologías usadas

- Java 17+
- Maven
- Jackson (JSON processing)
- HTTPURLConnection (para consumir APIs REST)
- Git

---

## Estructura del Proyecto
taskreport/
├── src/
│ └── main/
│ └── java/
│ └── com/
│ └── techsolutions/
│ ├── App.java
│ ├── Proceso.java
│ ├── Recurso.java
│ └── Resultados.java
├── pom.xml
└── README.md


---

## Como compilar y ejecutar

1. Abre una terminal en la carpeta del proyecto.
2. Ejecuta Maven:

cmd:
mvn clean compile 
exec:java -Dexec.mainClass="com.techsolutions.App"
