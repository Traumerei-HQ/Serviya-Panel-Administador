# Distribución de Carpetas

📦 **proyecto-cotizaciones**  
 ┣ 📂 **backend**  
 ┃ ┣ 📂 `controllers`  → Maneja las solicitudes HTTP  
 ┃ ┣ 📂 `services`  → Lógica de negocio  
 ┃ ┣ 📂 `models`  → Clases que representan datos  
 ┃ ┣ 📂 `database`  → Persistencia de datos (archivos JSON, SQLite o PostgreSQL)  
 ┃ ┗ 📂 `utils`  → Funciones de utilidad  
 ┣ 📂 **frontend**  
 ┃ ┣ 📂 `css`  → Estilos  
 ┃ ┣ 📂 `js`  → Código JavaScript  
 ┃ ┣ 📂 `img`  → Imágenes  
 ┃ ┗ 📂 `pages`  → Páginas HTML  
 ┣ 📂 **public**  → Recursos accesibles (imágenes, archivos estáticos)  
 ┣ 📜 `index.html`  → Página principal  
 ┣ 📜 `server.java`  → Punto de entrada del backend  
 ┣ 📜 `app.js`  → Lógica principal del frontend  
 ┗ 📜 `README.md`  → Documentación  

---

## Explicación y Uso

### 1️⃣ Backend (Java Puro)

- **`controllers/`**: Maneja las solicitudes del frontend (por ejemplo, recibir datos de cotización).  
- **`services/`**: Contiene la lógica de negocio (por ejemplo, cálculos de cotización).  
- **`models/`**: Define las clases que representan datos (por ejemplo, `Cotizacion.java`).  
- **`database/`**: Archivos de persistencia (por ejemplo, archivos JSON o bases de datos SQLite/PostgreSQL).  
- **`utils/`**: Funciones de utilidad como formateo de fechas, validaciones, etc.  
- **`server.java`**: Archivo principal que ejecuta el servidor HTTP con Java (puede ser un simple servidor con HttpServer).

