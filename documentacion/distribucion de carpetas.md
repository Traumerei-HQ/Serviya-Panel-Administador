# Distribucion de carpetas.

📦 proyecto-cotizaciones
 ┣ 📂 backend  # Lógica del servidor en Java
 ┃ ┣ 📂 controllers  # Maneja las solicitudes HTTP
 ┃ ┣ 📂 services  # Lógica de negocio
 ┃ ┣ 📂 models  # Clases que representan datos
 ┃ ┣ 📂 database  # Persistencia de datos (archivos JSON, SQLite o PostgreSQL)
 ┃ ┗ 📂 utils  # Funciones de utilidad
 ┣ 📂 frontend  # Interfaz de usuario
 ┃ ┣ 📂 css  # Estilos
 ┃ ┣ 📂 js  # Código JavaScript
 ┃ ┣ 📂 img  # Imágenes
 ┃ ┗ 📂 pages  # Páginas HTML
 ┣ 📂 public  # Recursos accesibles (imágenes, archivos estáticos)
 ┣ 📜 index.html  # Página principal
 ┣ 📜 server.java  # Punto de entrada del backend
 ┣ 📜 app.js  # Lógica principal del frontend
 ┗ 📜 README.md  # Documentación

## Explicacion y uso

1️⃣ Backend (Java Puro)

controllers/ → Maneja las solicitudes del frontend (ejemplo: recibir datos de cotización).
services/ → Contiene la lógica de negocio (ejemplo: cálculos de cotización).
models/ → Define las clases que representan datos (ejemplo: Cotizacion.java).
database/ → Archivos de persistencia (ejemplo: archivo JSON o base de datos SQLite/PostgreSQL).
utils/ → Funciones de utilidad como formateo de fechas, validaciones, etc.
server.java → Archivo principal que ejecuta el servidor HTTP con Java (puede ser un simple servidor con HttpServer).
2️⃣ Frontend (HTML, CSS y JavaScript Vanilla)

css/ → Archivos .css para los estilos.
js/ → Código JavaScript para manejar eventos y peticiones al backend.
pages/ → Páginas HTML separadas (login.html, dashboard.html, etc.).

