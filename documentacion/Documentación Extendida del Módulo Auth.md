# Documentación Extendida del Módulo Auth

## 1. Visión General

El módulo **auth** maneja la **autenticación** y el **registro de usuarios** en la aplicación, usando **Spring Boot**, **Spring Security** y **JWT**. Está organizado en paquetes para separar responsabilidades:

1. **controller:** Controladores REST (endpoints) para registro, login y, opcionalmente, otras operaciones relacionadas con usuarios.
2. **dto:** Clases de transferencia de datos (DTO) que encapsulan la información enviada y recibida por los endpoints.
3. **model:** Entidades que representan la estructura de las tablas en la base de datos (por ejemplo, `User`).
4. **repository:** Interfaces para acceder a la base de datos usando Spring Data JPA.
5. **service:** Lógica de negocio para autenticación, registro y manejo de usuarios.
6. **security:** Configuración de Spring Security, filtros JWT y otras clases para proteger la aplicación.

La clase principal de tu proyecto (`ServiyaPanelApplication.java`) se encarga de iniciar la aplicación Spring Boot, pero no pertenece directamente al módulo `auth`.

---

## 2. Estructura de Archivos

La carpeta `auth` contiene las siguientes subcarpetas y archivos:

pgsql

CopiarEditar

`auth ├── controller │    ├── AuthController.java │    └── UserController.java (opcional) ├── dto │    ├── AuthRequest.java │    ├── AuthResponse.java │    ├── LoginRequestDTO.java │    ├── QuickRegisterRequestDTO.java │    ├── RegisterRequestDTO.java │    └── UserDTO.java ├── model │    ├── User.java │    └── UserRole.java ├── repository │    └── UserRepository.java ├── security │    ├── JwtAuthenticationFilter.java │    └── SecurityConfig.java └── service      ├── AuthService.java      ├── JwtService.java      └── UserService.java`

A continuación, se describe cada archivo en detalle, incluyendo **UserService.java**.

---

## 3. Descripción Detallada de los Componentes

### 3.1. **Paquete `model`**

#### 3.1.1. **User.java**

- **Propósito:** Representa la tabla `users` en la base de datos.
- **Anotaciones importantes:**
  - `@Entity` y `@Table(name = "users")`: Indican que es una entidad JPA y que se mapeará a la tabla `users`.
  - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`: Lombok genera getters, setters, constructores y un patrón Builder.
  - `implements UserDetails`: Permite a Spring Security tratar esta entidad como un usuario válido para la autenticación.
- **Campos relevantes:**
  - `id`: Clave primaria (tipo `Long` o `UUID`, según tu configuración).
  - `email`: Campo único y obligatorio.
  - `password`: Almacena la contraseña cifrada.
  - `acceptedHabeasData`: Boolean que indica si el usuario aceptó la política de datos.
  - `name`: Nombre del usuario (puede estar vacío en el registro rápido).
  - `role`: Indica el rol (`USER`, `ADMIN`), definido en `UserRole.java`.
  - Otros: `docType`, `docNumber`, `phone`, etc.
- **Métodos de `UserDetails`:**
  - `getUsername()`: Retorna `email` como identificador del usuario.
  - `getAuthorities()`: Devuelve una lista de roles/permisos; en este caso, vacío o mapearía `role`.

#### 3.1.2. **UserRole.java**

- **Propósito:** Enum que define roles posibles, como `USER` o `ADMIN`.
- **Uso en `User.java`:** Campo `role` para almacenar el rol del usuario.

---

### 3.2. **Paquete `dto` (Data Transfer Objects)**

Estas clases definen los datos que viajan hacia y desde los endpoints REST, sin exponer la entidad `User` directamente.

1. **AuthRequest.java**
   
   - **Uso:** En el login (usuario y contraseña).
   - **Campos:** `email`, `password`.

2. **AuthResponse.java**
   
   - **Uso:** Respuesta al cliente tras login o registro, contiene el token JWT y datos del usuario.
   - **Campos:** `token`, `name`, `email`, `role`.

3. **LoginRequestDTO.java**
   
   - **Uso:** Similar a `AuthRequest`, pero con un nombre distinto (podrías unificarlos si quieres).
   - **Campos:** `email`, `password`.

4. **QuickRegisterRequestDTO.java**
   
   - **Uso:** Registro rápido, solo requiere `email`, `password` y un boolean para `acceptedHabeasData`.
   - **Campos:** `email`, `password`, `acceptedHabeasData`.

5. **RegisterRequestDTO.java**
   
   - **Uso:** Registro completo, con campos adicionales (nombre, etc.).
   - **Campos:** `name`, `email`, `password`, y otros que quieras.

6. **UserDTO.java**
   
   - **Uso:** Exponer datos del usuario sin incluir la contraseña u otra info sensible.
   - **Campos:** `id`, `name`, `email`, `role`.

---

### 3.3. **Paquete `repository`**

#### 3.3.1. **UserRepository.java**

- **Interfaz** que extiende `JpaRepository<User, Long>` (o `User, UUID` si tu `id` es `UUID`).
- **Métodos comunes:**
  - `findByEmail(String email)`: Busca un usuario por email.
  - `existsByEmail(String email)`: Verifica si un usuario ya está registrado.

---

### 3.4. **Paquete `service`**

#### 3.4.1. **AuthService.java**

- **Propósito:** Lógica de negocio para login, registro rápido, registro completo, etc.
- **Métodos principales:**
  - `quickRegister(QuickRegisterRequestDTO request)`: Crea un usuario con email, password y marca `acceptedHabeasData`.
  - `register(RegisterRequestDTO request)`: Registro completo con más datos (por ejemplo, `name`).
  - `login(AuthRequest request)`: Autentica al usuario (verifica email y password) y genera un token JWT.
- **Detalles de implementación:**
  - Usa `userRepository` para guardar y buscar usuarios.
  - Usa `passwordEncoder` para cifrar la contraseña.
  - Usa `jwtService` para generar el token JWT.
  - Devuelve un `AuthResponse` con el token y datos del usuario.

#### 3.4.2. **JwtService.java**

- **Propósito:** Generar, validar y extraer información de los tokens JWT.
- **Métodos clave:**
  - `generateToken(String email)`: Crea un token firmado con la clave secreta.
  - `validateToken(String token)`: Verifica que el token no esté alterado o expirado.
  - `extractUsername(String token)`: Extrae el campo `sub` (subject), usualmente el email.
- **Uso:** Llamado desde `AuthService` y `JwtAuthenticationFilter`.

#### 3.4.3. **UserService.java**

- **Propósito:** Implementa la interfaz `UserDetailsService` (de Spring Security) o una lógica adicional para manejar los usuarios.
- **Métodos típicos:**
  - `loadUserByUsername(String email)`: Busca al usuario en la base de datos y retorna un objeto `UserDetails` (tu `User`).
  - Puede contener otros métodos, como `createUser(...)`, `updateUser(...)`, etc., si quieres centralizar la lógica de usuarios ahí.
- **Integración con `SecurityConfig`:**
  - En `SecurityConfig`, un `DaoAuthenticationProvider` o `AuthenticationManager` puede usar este `UserService` para autenticar usuarios basados en email y password.
- **Importante:**
  - A veces la lógica de usuarios se pone en `AuthService`, pero `UserService` es un lugar ideal para la lógica de `UserDetailsService` si usas `DaoAuthenticationProvider`.

---

### 3.5. **Paquete `security`**

#### 3.5.1. **SecurityConfig.java**

- **Propósito:** Configurar Spring Security (rutas permitidas, CORS, CSRF, filtros, etc.).

- **Puntos clave:**
  
  - `@Configuration`: Spring Boot lo detecta como clase de configuración.
  
  - `securityFilterChain(HttpSecurity http)`: Define la cadena de filtros y las reglas de acceso.
    
    - **Ejemplo:**
      
      java
      
      CopiarEditar
      
      `.authorizeHttpRequests(auth -> auth     .requestMatchers("/auth/**").permitAll()     .anyRequest().authenticated() )`
      
      Esto permite acceso a las rutas que empiezan con `/auth/` y protege el resto.
  
  - `sessionManagement(...)`: Se configura en modo `STATELESS` cuando se usan JWT.
  
  - `authenticationProvider(...)`: Se registra un `DaoAuthenticationProvider` que utiliza el `UserService` y un `PasswordEncoder` (por ejemplo, `BCryptPasswordEncoder`).
  
  - `corsConfigurationSource()`: Permite configurar CORS para aceptar peticiones de ciertos orígenes, métodos y headers.

#### 3.5.2. **JwtAuthenticationFilter.java**

- **Propósito:** Interceptar cada petición para verificar si contiene un token JWT válido.
- **Flujo:**
  1. Obtiene el header `Authorization`.
  2. Verifica si empieza con `"Bearer "`.
  3. Extrae el token, lo valida con `jwtService`.
  4. Si es válido, obtiene el usuario de la base de datos (`userService.loadUserByUsername(...)`) y setea el contexto de autenticación (`SecurityContextHolder`).
- **Ubicación en la cadena de filtros:** Se añade antes del `UsernamePasswordAuthenticationFilter`.

---

## 4. Flujo de Trabajo

1. **Registro Rápido** (`POST /auth/quick-register`):
   - El controlador recibe `QuickRegisterRequestDTO` con `email`, `password`, `acceptedHabeasData`.
   - `AuthService.quickRegister(...)` valida la entrada, crea el usuario con nombre vacío, asigna rol `USER`, genera el token y retorna un `AuthResponse`.
2. **Registro Completo** (`POST /auth/register`):
   - El controlador recibe `RegisterRequestDTO` con más datos (por ejemplo, `name`).
   - `AuthService.register(...)` guarda el usuario con todos los datos, genera el token, retorna `AuthResponse`.
3. **Login** (`POST /auth/login`):
   - El controlador recibe `AuthRequest` con `email` y `password`.
   - `AuthService.login(...)` autentica usando `authenticationManager`, genera token y retorna `AuthResponse`.
4. **Futuras Operaciones** (por ejemplo, `PUT /auth/complete-profile`):
   - Se pueden crear endpoints adicionales para que el usuario actualice campos como `docType`, `docNumber`, etc.
   - Estos endpoints deben estar protegidos, por lo que el usuario debe enviar su token en cada petición.

---

## 5. ¿Cómo se Conecta Todo?

1. **Usuario** envía solicitud (ej: `/auth/quick-register`) → **AuthController** la recibe y llama a **AuthService**.
2. **AuthService** usa **UserRepository** para guardar/consultar la entidad **User**.
3. **AuthService** también llama a **JwtService** para generar o validar tokens.
4. **UserService** (si implementa `UserDetailsService`) es utilizado por Spring Security (mediante `SecurityConfig` y `DaoAuthenticationProvider`) para cargar el usuario al autenticar.
5. **SecurityConfig** define qué rutas son públicas (`/auth/**`) y cuáles requieren token, además de manejar CORS y CSRF.
6. **JwtAuthenticationFilter** se ejecuta en cada petición, verifica el token y setea la autenticación si es válido.

---

## 6. Modificaciones y Extensiones

- **Agregar más campos al usuario:**  
  Edita `User.java` y, si corresponde, crea un nuevo DTO para recibir o exponer esos campos.
- **Cambiar la lógica de registro:**  
  Edita los métodos en `AuthService.java` o crea nuevos endpoints en `AuthController.java`.
- **Personalizar la seguridad:**  
  Modifica `SecurityConfig.java` para cambiar rutas permitidas, roles, CORS, etc.
- **Roles avanzados:**  
  Si quieres mapear roles a autoridades, en `User.java` podrías devolver una lista con `new SimpleGrantedAuthority("ROLE_" + role.name())` en `getAuthorities()`.

---

## 7. Consejos de Uso y Mantenimiento

1. **Revisar dependencias de Lombok**: Asegúrate de tener configurados `compileOnly 'org.projectlombok:lombok'` y `annotationProcessor 'org.projectlombok:lombok'` en tu `build.gradle`.
2. **Mantener DTOs separados** de las entidades para no exponer campos sensibles (ejemplo: no exponer `password` en un `UserDTO`).
3. **Probar con Postman** tras cada cambio para confirmar que los endpoints se comportan como esperas.
4. **Documentar cada endpoint** con comentarios o usar Swagger/OpenAPI para generar documentación automática.
5. **Gestionar tokens en frontend**: Al recibir el token en `AuthResponse`, el cliente debe guardarlo (por ejemplo, en localStorage o cookies) y enviarlo en el header `Authorization: Bearer <token>` para rutas protegidas.

---

## 8. Conclusión

El **módulo auth** provee la infraestructura necesaria para manejar la autenticación y el registro de usuarios. Consta de:

- **Controladores** que exponen los endpoints.
- **DTOs** para intercambiar datos de forma clara y segura.
- **Entidades y Repositorios** para mapear y persistir datos en la base de datos.
- **Servicios** (`AuthService`, `UserService`, `JwtService`) que contienen la lógica de autenticación, registro y tokenización.
- **Configuración de Seguridad** (`SecurityConfig` y `JwtAuthenticationFilter`) para proteger las rutas y validar tokens JWT.

Si quieres **extender** la funcionalidad, puedes crear nuevos endpoints, agregar campos en `User.java` o cambiar la forma en que generas y validas tokens en `JwtService`. Este diseño modular te permite escalar la aplicación y adaptarla a tus necesidades futuras.

¡Con esta documentación extendida, ya tienes una visión completa de cómo se conectan todas las partes y cómo modificar el módulo auth de tu aplicación!
