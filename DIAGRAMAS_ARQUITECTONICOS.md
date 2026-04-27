# DIAGRAMAS ARQUITECTÓNICOS COMPARATIVOS

## CONTENIDO
1. Diagrama de componentes actual vs. futuro
2. Flujo de datos actual vs. futuro
3. Dependencias y acoplamiento
4. Secuencia de autenticación
5. Estructura de paquetes propuesta

---

## 1. DIAGRAMA DE COMPONENTES - ACTUAL vs. FUTURO

### ACTUAL: Arquitectura Monolítica (Swing + JDBC)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        APLICACIÓN ACTUAL                                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                      CAPA DE PRESENTACIÓN                        │  │
│  │                         (Swing)                                  │  │
│  ├──────────────────────────────────────────────────────────────────┤  │
│  │                                                                  │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │  │
│  │  │ Main.java    │  │ Vistas.java  │  │ VISTAS (Paneles)     │  │  │
│  │  │              │  │ (JFrame)     │  ├──────────────────────┤  │  │
│  │  │ ┌──────────┐ │  │              │  │ ├─ Login.java       │  │  │
│  │  │ │main()    │ │  │ ┌──────────┐ │  │ ├─ Home.java        │  │  │
│  │  │ └──────────┘ │  │ │Container:│ │  │ ├─ PanelProductos   │  │  │
│  │  │              │  │ │CardLayout │ │  │ ├─ AlmacenesPanel   │  │  │
│  │  │ ┌──────────┐ │  │ └──────────┘ │  │ └─ FormXXX.java     │  │  │
│  │  │ │crear     │ │  │              │  │                      │  │  │
│  │  │ │Vistas()  │ │  │ ┌──────────┐ │  │ PROBLEMAS:           │  │  │
│  │  │ └──────────┘ │  │ │DB instancia│ │  │ • new Database()    │  │  │
│  │  └──────────────┘  │ └──────────┘ │  │ • ActionListeners    │  │  │
│  │                    └──────────────┘  │ • Lógica mezclada    │  │  │
│  │                                      │ • Sin validaciones    │  │  │
│  │                                      │   centralizadas       │  │  │
│  │                                      └─────────────────────┘  │  │
│  │                                                                  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                 │                                       │
│                                 ▼ (eventos UI)                         │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                 CAPA DE PERSISTENCIA                             │  │
│  │                 (JDBC + Database.java)                           │  │
│  ├──────────────────────────────────────────────────────────────────┤  │
│  │                                                                  │  │
│  │  ┌──────────────────────────────────────────────────────────┐  │  │
│  │  │ Database.java (MONOLÍTICO - 338 LÍNEAS)                 │  │  │
│  │  │                                                          │  │  │
│  │  │ ┌─────────────────┐  ┌──────────────┐  ┌──────────────┐ │  │  │
│  │  │ │ connect()       │  │ CRUD Usuarios│  │ CRUD Almacén │ │  │  │
│  │  │ │                 │  │              │  │              │ │  │  │
│  │  │ │ init()          │  │ ┌──────────┐ │  │ ┌──────────┐ │ │  │  │
│  │  │ │                 │  │ │authenticate  │  │ │insert    │ │ │  │  │
│  │  │ │ setDefaultFecha │  │ │          │ │  │ │update    │ │ │  │  │
│  │  │ │                 │  │ └──────────┘ │  │ │delete    │ │ │  │  │
│  │  │ │ insertDefUser   │  │              │  │ │list      │ │ │  │  │
│  │  │ │                 │  │ insertDefault│  │ │          │ │ │  │  │
│  │  │ │                 │  │ User()       │  │ └──────────┘ │ │  │  │
│  │  │ └─────────────────┘  └──────────────┘  └──────────────┘ │  │  │
│  │  │                                                          │  │  │
│  │  │ PROBLEMAS:                                              │  │  │
│  │  │ × SQL queries embebidas (no reutilizables)             │  │  │
│  │  │ × Mapeo manual ResultSet → POJO                        │  │  │
│  │  │ × Sin connection pooling                               │  │  │
│  │  │ × Sin lazy loading                                     │  │  │
│  │  │ × Múltiples responsabilidades (SRP violation)          │  │  │
│  │  │ × Difícil de testear y mockiar                         │  │  │
│  │  └──────────────────────────────────────────────────────────┘  │  │
│  │                              │                                   │  │
│  │                              ▼ (JDBC Directo)                  │  │
│  │  ┌──────────────────────────────────────────────────────────┐  │  │
│  │  │ SQLITE (Inventario.db)                                 │  │  │
│  │  │                                                          │  │  │
│  │  │ ┌─────────────────────────────────────────────────────┐ │  │  │
│  │  │ │ CREATE TABLE usuarios (id, nombre, password, ...)   │ │  │  │
│  │  │ │ CREATE TABLE almacenes (id, nombre, ubicacion, ...) │ │  │  │
│  │  │ │ CREATE TABLE productos (id, nombre, cantidad, ...)  │ │  │  │
│  │  │ └─────────────────────────────────────────────────────┘ │  │  │
│  │  │                                                          │  │  │
│  │  │ Problemas en BD:                                        │  │  │
│  │  │ × Fechas como STRING (difícil comparar)                │  │  │
│  │  │ × Sin constraints (FK sin FOREIGN KEY)                │  │  │
│  │  │ × Sin índices óptimos                                 │  │  │
│  │  │ × Sin migraciones versionadas                         │  │  │
│  │  └──────────────────────────────────────────────────────────┘  │  │
│  │                                                                  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

### FUTURA: Arquitectura Moderna (JavaFX + DAO + ORMLite)

```
┌───────────────────────────────────────────────────────────────────────────────┐
│                       APLICACIÓN REFACTORIZADA                                │
├───────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌─────────────────────────────────────────────────────────────────────────┐ │
│  │                      CAPA DE PRESENTACIÓN                              │ │
│  │                        (JavaFX + FXML)                                 │ │
│  ├─────────────────────────────────────────────────────────────────────────┤ │
│  │                                                                         │ │
│  │  Main.java                                                             │ │
│  │    │                                                                   │ │
│  │    ▼                                                                   │ │
│  │  ┌────────────────────────────────────────────────────────────────┐  │ │
│  │  │ ApplicationInitializer                                         │  │ │
│  │  │ └─ setupDependencyInjection()                                 │  │ │
│  │  │ └─ loadApplicationContext()                                   │  │ │
│  │  └────────────────────────────────────────────────────────────────┘  │ │
│  │    │                                                                   │ │
│  │    ▼                                                                   │ │
│  │  ┌────────────────────────────────────────────────────────────────┐  │ │
│  │  │ Controllers (MVC Pattern)                                      │  │ │
│  │  ├────────────────────────────────────────────────────────────────┤  │ │
│  │  │                                                                │  │ │
│  │  │ ┌──────────────┐   ┌──────────────┐  ┌──────────────────────┐│  │ │
│  │  │ │LoginController│   │HomeController│  │ProductosController ││  │ │
│  │  │ │              │   │              │  │                    ││  │ │
│  │  │ │@FXML void    │   │@FXML void    │  │@FXML void          ││  │ │
│  │  │ │handleLogin() │   │showProductos()│  │crearProducto()     ││  │ │
│  │  │ │              │   │              │  │buscarProducto()    ││  │ │
│  │  │ └──────────────┘   └──────────────┘  └──────────────────────┘│  │ │
│  │  │                                                                │  │ │
│  │  │ ┌──────────────┐   ┌──────────────┐                          │  │ │
│  │  │ │AlmacenController│  │  [Diálogos]  │                          │  │ │
│  │  │ │              │   │              │                          │  │ │
│  │  │ │@FXML void    │   │FormProducto  │                          │  │ │
│  │  │ │crearAlmacen()│   │FormAlmacen   │                          │  │ │
│  │  │ └──────────────┘   └──────────────┘                          │  │ │
│  │  │                                                                │  │ │
│  │  │ ✓ Inyección de dependencias (Services inyectados)            │  │ │
│  │  │ ✓ Data binding automático (@FXML)                           │  │ │
│  │  │ ✓ FXML para UI (separación clara)                           │  │ │
│  │  │ ✓ CSS para estilos                                          │  │ │
│  │  │ ✓ Threading con Platform.runLater()                         │  │ │
│  │  │                                                                │  │ │
│  │  └────────────────────────────────────────────────────────────────┘  │ │
│  │    │ (llama a)                                                        │ │
│  │    ▼                                                                   │ │
│  │  ┌────────────────────────────────────────────────────────────────┐  │ │
│  │  │ FXML & CSS Resources                                          │  │ │
│  │  │ ├─ login.fxml          ├─ home.fxml                          │  │ │
│  │  │ ├─ productos.fxml      ├─ almacenes.fxml                    │  │ │
│  │  │ └─ styles/              └─ theme.css                         │  │ │
│  │  └────────────────────────────────────────────────────────────────┘  │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                 │                                            │
│                      ▼ (inyección de Services)                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐ │
│  │               CAPA DE SERVICIOS (Business Logic)                       │ │
│  ├─────────────────────────────────────────────────────────────────────────┤ │
│  │                                                                         │ │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │ │
│  │  │ UsuarioService                                                 │  │ │
│  │  │ ├─ autenticar(usuario, password): Usuario                     │  │ │
│  │  │ ├─ cambiarContraseña(usuario, nueva): void                   │  │ │
│  │  │ └─ registrarAuditLog(usuario, accion): void                  │  │ │
│  │  │                                                                │  │ │
│  │  │ ✓ Usa UsuarioDAO para persistencia                          │  │ │
│  │  │ ✓ Validaciones centralizadas                                │  │ │
│  │  │ ✓ Hashing BCrypt                                            │  │ │
│  │  │ ✓ Editable para testeos (DAO injectable)                    │  │ │
│  │  └─────────────────────────────────────────────────────────────────┘  │ │
│  │                                                                         │ │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │ │
│  │  │ ProductoService                                                │  │ │
│  │  │ ├─ crearProducto(producto): Producto                          │  │ │
│  │  │ ├─ actualizarProducto(producto): void                         │  │ │
│  │  │ ├─ eliminarProducto(id): void                                 │  │ │
│  │  │ ├─ buscarProducto(termino): List<Producto>                   │  │ │
│  │  │ ├─ listarPorAlmacen(almacenId): List<Producto>              │  │ │
│  │  │ └─ validarStockMinimo(producto): Boolean                     │  │ │
│  │  │                                                                │  │ │
│  │  │ ✓ Validación de negocio                                      │  │ │
│  │  │ ✓ Transformación a DTO                                       │  │ │
│  │  │ ✓ Manejo de excepciones específicas                          │  │ │
│  │  └─────────────────────────────────────────────────────────────────┘  │ │
│  │                                                                         │ │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │ │
│  │  │ AlmacenService                                                 │  │ │
│  │  │ ├─ crearAlmacen(almacen): Almacen                             │  │ │
│  │  │ ├─ actualizarAlmacen(almacen): void                           │  │ │
│  │  │ ├─ eliminarAlmacen(id, forzar): void                          │  │ │
│  │  │ └─ listarAlmacenes(): List<Almacen>                           │  │ │
│  │  │                                                                │  │ │
│  │  │ ✓ Previene eliminación con productos                         │  │ │
│  │  │ ✓ Validaciones de integridad                                 │  │ │
│  │  └─────────────────────────────────────────────────────────────────┘  │ │
│  │                                                                         │ │
│  │ Excepciones Personalizadas:                                             │ │
│  │ ├─ ValidationException                                                 │ │
│  │ ├─ CredencialesInvalidasException                                      │ │
│  │ ├─ EntidadNoEncontradaException                                        │ │
│  │ ├─ IntegridadDeDatosException                                          │ │
│  │ └─ NegocioException (genérica)                                         │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                 │                                            │
│                    ▼ (llama a DAO methods)                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐ │
│  │            CAPA DE ACCESO A DATOS (DAO Pattern)                        │ │
│  │                        ORMLite BaseDaoImpl                              │ │
│  ├─────────────────────────────────────────────────────────────────────────┤ │
│  │                                                                         │ │
│  │  ┌──────────────────────────────────────────────────────────────────┐ │ │
│  │  │ DatabaseManager                                                 │ │ │
│  │  │                                                                  │ │ │
│  │  │ - connectionSource: ConnectionSource                            │ │ │
│  │  │ - usuarioDAO: UsuarioDAO                                        │ │ │
│  │  │ - productoDAO: ProductoDAO                                      │ │ │
│  │  │ - almacenDAO: AlmacenDAO                                        │ │ │
│  │  │                                                                  │ │ │
│  │  │ + getInstance(): DatabaseManager (Singleton)                   │ │ │
│  │  │ + initialize()                                                  │ │ │
│  │  │ + getConnectionSource(): ConnectionSource                       │ │ │
│  │  │ + getUsuarioDAO(): UsuarioDAO                                   │ │ │
│  │  │ + close()                                                       │ │ │
│  │  │                                                                  │ │ │
│  │  │ ✓ Connection pooling automático                                │ │ │
│  │  │ ✓ Lifecycle management                                         │ │ │
│  │  └──────────────────────────────────────────────────────────────────┘ │ │
│  │                                                                         │ │
│  │  ┌──────────────────────┐  ┌──────────────────────────────────────┐  │ │
│  │  │ UsuarioDAO           │  │ ProductoDAO                          │  │ │
│  │  │ extends BaseDaoImpl   │  │ extends BaseDaoImpl                   │  │ │
│  │  │                      │  │                                      │  │ │
│  │  │ + findByNombre()     │  │ + findByAlmacen()                   │  │ │
│  │  │ + findAll(): List    │  │ + findByNombre(): List              │  │ │
│  │  │ + create(Usuario)    │  │ + findBajoPrecio(double): List      │  │ │
│  │  │ + update(Usuario)    │  │ + create(Producto)                 │  │ │
│  │  │ + deleteById(id)     │  │ + update(Producto)                 │  │ │
│  │  │ + queryBuilder()     │  │ + queryByExample(Producto): List    │  │ │
│  │  │                      │  │                                      │  │ │
│  │  │ ✓ Type-safe queries  │  │ ✓ Lazy loading                     │  │ │
│  │  │ ✓ No SQL raw strings │  │ ✓ Automatic mapping                │  │ │
│  │  │                      │  │                                      │  │ │
│  │  └──────────────────────┘  └──────────────────────────────────────┘  │ │
│  │                                                                         │ │
│  │  ┌──────────────────────┐                                             │ │
│  │  │ AlmacenDAO           │                                             │ │
│  │  │ extends BaseDaoImpl   │                                             │ │
│  │  │                      │                                             │ │
│  │  │ + findByNombre()     │                                             │ │
│  │  │ + findByUbicacion()  │                                             │ │
│  │  │ + create(Almacen)    │                                             │ │
│  │  │ + update(Almacen)    │                                             │ │
│  │  │ + deleteById(id)     │                                             │ │
│  │  │ + queryBuilder()     │                                             │ │
│  │  └──────────────────────┘                                             │ │
│  │                                                                         │ │
│  │ Ventajas DAO Layer:                                                    │ │
│  │ ✓ Abstracción completa de persistencia                                │ │
│  │ ✓ Queries type-safe (no strings)                                      │ │
│  │ ✓ Fácil cambiar BD (ej: MySQL)                                        │ │
│  │ ✓ Transacciones automáticas                                           │ │
│  │ ✓ Mockeable para testing unitario                                     │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                 │                                            │
│                    ▼ (ORMLite mapping)                                     │
│  ┌─────────────────────────────────────────────────────────────────────────┐ │
│  │                  CAPA DE MODELOS (Entidades)                           │ │
│  │                  (con @DatabaseTable anotaciones)                      │ │
│  ├─────────────────────────────────────────────────────────────────────────┤ │
│  │                                                                         │ │
│  │  @DatabaseTable(tableName="usuarios")                                  │ │
│  │  public class Usuario {                                                │ │
│  │      @DatabaseField(id=true, generatedId=true) private Integer id;   │ │
│  │      @DatabaseField(canBeNull=false) private String nombre;          │ │
│  │      @DatabaseField(canBeNull=false) private String passwordHash;    │ │
│  │      @DatabaseField private LocalDateTime fechaCreacion;             │ │
│  │      // getters/setters con validaciones                             │ │
│  │  }                                                                     │ │
│  │                                                                         │ │
│  │  @DatabaseTable(tableName="productos")                                │ │
│  │  public class Producto {                                              │ │
│  │      @DatabaseField(id=true) private Integer id;                     │ │
│  │      @DatabaseField(canBeNull=false) private String nombre;          │ │
│  │      @DatabaseField private Almacen almacen; // FK                   │ │
│  │      @DatabaseField private LocalDateTime fechaModificacion;         │ │
│  │      // getters/setters con validaciones                             │ │
│  │  }                                                                     │ │
│  │                                                                         │ │
│  │  @DatabaseTable(tableName="almacenes")                                │ │
│  │  public class Almacen {                                               │ │
│  │      @DatabaseField(id=true) private Integer id;                     │ │
│  │      @DatabaseField(canBeNull=false) private String nombre;          │ │
│  │      @DatabaseField private LocalDateTime fechaCreacion;             │ │
│  │      // getters/setters con validaciones                             │ │
│  │  }                                                                     │ │
│  │                                                                         │ │
│  │ Mejoras a Modelos:                                                     │ │
│  │ ✓ Encapsulación completa (private fields)                             │ │
│  │ ✓ Fechas como LocalDateTime (no Strings)                              │ │
│  │ ✓ Validaciones en setters                                             │ │
│  │ ✓ Anotaciones ORM (no SQL en código)                                  │ │
│  │ ✓ equals() y hashCode() correctos                                     │ │
│  │ ✓ toString() para debugging                                           │ │
│  │ ✓ Serializable para caching                                           │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                 │                                            │
│                        ▼ (ORMLite JDBC)                                     │
│  ┌─────────────────────────────────────────────────────────────────────────┐ │
│  │                  PERSISTENCIA (ORMLite + SQLite)                       │ │
│  ├─────────────────────────────────────────────────────────────────────────┤ │
│  │                                                                         │ │
│  │  ConnectionSource (con pooling automático)                             │ │
│  │         │                                                              │ │
│  │         ▼                                                              │ │
│  │  ┌──────────────────────────────────────────────────────────────────┐ │ │
│  │  │ SQLite Driver (sqlite-jdbc)                                      │ │ │
│  │  │ URL: jdbc:sqlite:Inventario.db                                  │ │ │
│  │  │ Pool Size: 10 conexiones (automático)                           │ │ │
│  │  └──────────────────────────────────────────────────────────────────┘ │ │
│  │         │                                                              │ │
│  │         ▼                                                              │ │
│  │  ┌──────────────────────────────────────────────────────────────────┐ │ │
│  │  │ Inventario.db (SQLite Database)                                 │ │ │
│  │  │                                                                  │ │ │
│  │  │ CREATE TABLE usuarios (                                         │ │ │
│  │  │     id INTEGER PRIMARY KEY,                                     │ │ │
│  │  │     nombre TEXT UNIQUE NOT NULL,                                │ │ │
│  │  │     password_hash TEXT NOT NULL,                                │ │ │
│  │  │     fecha_creacion DATETIME,                                    │ │ │
│  │  │     fecha_ultimo_inicio DATETIME,                               │ │ │
│  │  │     rol TEXT NOT NULL,                                          │ │ │
│  │  │     FOREIGN KEY (rol) REFERENCES roles(nombre)                  │ │ │
│  │  │ );                                                              │ │ │
│  │  │                                                                  │ │ │
│  │  │ CREATE TABLE almacenes (                                        │ │ │
│  │  │     id INTEGER PRIMARY KEY,                                     │ │ │
│  │  │     nombre TEXT UNIQUE NOT NULL,                                │ │ │
│  │  │     ubicacion TEXT,                                             │ │ │
│  │  │     fecha_creacion DATETIME NOT NULL,                           │ │ │
│  │  │     fecha_modificacion DATETIME,                                │ │ │
│  │  │     ultimo_usuario TEXT,                                        │ │ │
│  │  │     FOREIGN KEY (ultimo_usuario) REFERENCES usuarios(nombre)    │ │ │
│  │  │ );                                                              │ │ │
│  │  │                                                                  │ │ │
│  │  │ CREATE TABLE productos (                                        │ │ │
│  │  │     id INTEGER PRIMARY KEY,                                     │ │ │
│  │  │     nombre TEXT NOT NULL,                                       │ │ │
│  │  │     descripcion TEXT,                                           │ │ │
│  │  │     cantidad INTEGER NOT NULL,                                  │ │ │
│  │  │     precio REAL NOT NULL,                                       │ │ │
│  │  │     almacen_id INTEGER,                                         │ │ │
│  │  │     fecha_creacion DATETIME NOT NULL,                           │ │ │
│  │  │     fecha_modificacion DATETIME,                                │ │ │
│  │  │     ultimo_usuario TEXT NOT NULL,                               │ │ │
│  │  │     FOREIGN KEY (almacen_id) REFERENCES almacenes(id),          │ │ │
│  │  │     FOREIGN KEY (ultimo_usuario) REFERENCES usuarios(nombre)    │ │ │
│  │  │ );                                                              │ │ │
│  │  │                                                                  │ │ │
│  │  │ CREATE INDEX idx_productos_almacen ON productos(almacen_id);   │ │ │
│  │  │ CREATE INDEX idx_usuarios_nombre ON usuarios(nombre);          │ │ │
│  │  │                                                                  │ │ │
│  │  │ ✓ Tablas con constraints (FK, UNIQUE)                          │ │ │
│  │  │ ✓ Tipos DATETIME (no TEXT)                                     │ │ │
│  │  │ ✓ Índices optimizados                                          │ │ │
│  │  │ ✓ Integridad referencial                                       │ │ │
│  │  └──────────────────────────────────────────────────────────────────┘ │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                               │
└───────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. FLUJO DE DATOS - ACTUAL vs. FUTURO

### FLUJO ACTUAL - Crear Producto

```
USUARIO FINAL
    │
    ▼ (Hace click en "Nuevo Producto")
┌─────────────────────────────────────────┐
│ PanelProductos (JPanel)                 │
│ ├─ Abre FormProducto                    │
│ ├─ Usuario completa campos              │
│ └─ Click en "Guardar"                   │
└─────────────────────────────────────────┘
    │
    ▼ (ActionListener)
┌─────────────────────────────────────────┐
│ FormProducto ActionListener             │
│ ├─ Obtiene texto de JTextFields         │
│ ├─ Validación básica (if isEmpty)       │
│ ├─ Crea instancia Producto              │
│ │   p.nombre = txtNombre.getText()      │
│ │   p.precio = Double.parse(...)        │
│ │   p.cantidad = Integer.parse(...)     │
│ └─ Llamada DIRECTA a Database           │
└─────────────────────────────────────────┘
    │
    ▼ (new Database())
┌─────────────────────────────────────────┐
│ Database.insertProducto()               │
│ ├─ connect() → abre conexión nueva      │
│ ├─ crea PreparedStatement                │
│ ├─ seta parámetros:                      │
│ │   ps.setString(1, nombre)              │
│ │   ps.setDouble(2, precio)              │
│ │   ... (7 parámetros)                   │
│ ├─ executeUpdate()                       │
│ ├─ getGeneratedKeys()                    │
│ └─ cierra conexión                       │
└─────────────────────────────────────────┘
    │
    ▼ (JDBC)
┌─────────────────────────────────────────┐
│ SQLite: INSERT INTO productos VALUES... │
└─────────────────────────────────────────┘
    │
    ▼ (Devuelve ID)
┌─────────────────────────────────────────┐
│ FormProducto ActionListener             │
│ ├─ Si id > 0:                           │
│ │   ├─ JOptionPane SUCCESS              │
│ │   └─ cierra FormProducto              │
│ ├─ Si id < 0:                           │
│ │   ├─ JOptionPane ERROR                │
│ │   └─ No cierra                        │
│ └─ PanelProductos llama cargarProductos │
└─────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────┐
│ PanelProductos.cargarProductos()        │
│ ├─ Limpia tabla actual                  │
│ ├─ Llama db.listProductos() (TODA lista)│
│ ├─ Itera y agrega a DefaultTableModel   │
│ └─ Pinta tabla                          │
└─────────────────────────────────────────┘
    │
    ▼
USUARIO VE TABLA ACTUALIZADA


PROBLEMAS EN ESTE FLUJO:
❌ Creación de Database en cada operación (sin pooling)
❌ Validación dispersa (FormProducto + ActionListener)
❌ No hay capa de servicio
❌ Error handling es "try-catch silent" (println)
❌ Recargar tabla entera es ineficiente
❌ No hay confirmación de persistencia antes de UI update
```

---

### FLUJO FUTURO - Crear Producto

```
USUARIO FINAL
    │
    ▼ (Hace click en "Nuevo Producto")
┌─────────────────────────────────────────────────────────┐
│ ProductosController (JavaFX)                            │
│ ├─ @FXML abre FormProducto Dialog                       │
│ ├─ Usuario completa campos                              │
│ ├─ Data binding automático a Form model                 │
│ └─ Click en "Guardar"                                   │
└─────────────────────────────────────────────────────────┘
    │
    ▼ (@FXML void guardar())
┌─────────────────────────────────────────────────────────┐
│ FormProductoController                                  │
│ ├─ getProductoDelFormulario(): Producto               │
│ ├─ Llama ProductoService.crearProducto()              │
│ │   └─ (inyección de dependencias)                      │
│ └─ Maneja resultado o exceptions                        │
└─────────────────────────────────────────────────────────┘
    │
    ▼ (Service layer)
┌─────────────────────────────────────────────────────────┐
│ ProductoService.crearProducto()                         │
│                                                         │
│ ┌─ Validación Profunda                                 │
│ │  ├─ validador.validarNombre(producto.getNombre())   │
│ │  ├─ validador.validarPrecio(producto.getPrecio())   │
│ │  ├─ validador.validarCantidad(producto.getCant())   │
│ │  └─ validador.lanzarErrors() si falla                │
│ │                                                      │
│ ├─ Validación de Integridad                            │
│ │  ├─ Si almacen != null:                              │
│ │  │   └─ almacenDAO.queryForId(almacenId)            │
│ │  │       └─ Si null → lanza EntityNotFoundException  │
│ │  └─ Si precio es negativo → lanza BusinessException  │
│ │                                                      │
│ ├─ Enriquecimiento                                      │
│ │  ├─ producto.setFechaCreacion(LocalDateTime.now())  │
│ │  ├─ producto.setUltimoUsuario(usuarioActual.getNomb)│
│ │  └─ producto.generarCodigo() si es necesario         │
│ │                                                      │
│ ├─ Persistencia                                         │
│ │  ├─ try {                                            │
│ │  │   productoDAO.create(producto)                   │
│ │  │   // ORMLite maneja: conexión, mapping, FK       │
│ │  │   auditLogger.log(usuario, "CREATE_PRODUCTO", fn) │
│ │  │   return producto                                 │
│ │  ├─ catch (SQLException e) {                         │
│ │  │   logger.error("Error BD", e)                    │
│ │  │   throw new PersistenciaException(...)           │
│ │  └─ }                                                │
│ │                                                      │
│ └─ Post-Persistencia (si es necesario)                 │
│    ├─ actualizarCache()                                │
│    ├─ notificarObservadores()                          │
│    └─ enviarNotificación()                             │
│                                                         │
│ Lanzar Excepciones Específicas:                         │
│ ├─ ValidationException (errores de validación)         │
│ ├─ EntityNotFoundException (almacén no existe)         │
│ ├─ PersistenciaException (error BD)                   │
│ └─ BusinessException (regla de negocio)               │
└─────────────────────────────────────────────────────────┘
    │
    ▼ (DAO layer)
┌─────────────────────────────────────────────────────────┐
│ ProductoDAO.create(producto)                            │
│                                                         │
│ ├─ queryBuilder()                                       │
│ │  .insertInto()                                        │
│ │  .columns()                                           │
│ │  .values()                                            │
│ │  .executeUpdate()                                     │
│ │                                                       │
│ └─ Retorna ID generado (automático ORMLite)            │
└─────────────────────────────────────────────────────────┘
    │
    ▼ (ORMLite + Connection Pool)
┌─────────────────────────────────────────────────────────┐
│ ConnectionSource (pooling)                              │
│ ├─ Obtiene conexión del pool (reutiliza si existe)     │
│ ├─ Ejecuta prepared statement                           │
│ └─ Devuelve conexión al pool                            │
└─────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────┐
│ SQLite: INSERT INTO productos (...) VALUES (...)       │
│        RETURNING id;                                    │
└─────────────────────────────────────────────────────────┘
    │
    ▼ (Suceso)
┌─────────────────────────────────────────────────────────┐
│ ProductosController (Resultado Exitoso)                │
│ ├─ Recibe Producto creado                              │
│ ├─ Alert.showInformation("Producto creado")            │
│ ├─ Platform.runLater(() → {                            │
│ │   - Actualiza tabla (observable)                     │
│ │   - Data binding automático                          │
│ │   - Cierra diálogo                                   │
│ │ })                                                    │
│ └─ Refresh UI threading-safe                           │
└─────────────────────────────────────────────────────────┘
    │
    ▼ (O si falla)
┌─────────────────────────────────────────────────────────┐
│ ProductosController (Manejo de Exceptions)             │
│ ├─ catch ValidationException e {                        │
│ │   Alert.showError(e.getMessage())                    │
│ │   NO cierra diálogo (permite corrección)             │
│ ├─ catch EntityNotFoundException e {                    │
│ │   Alert.showError("Almacén no existe")              │
│ ├─ catch PersistenciaException e {                     │
│ │   Alert.showError("Error al guardar")               │
│ │   logger.error("Trazas detalladas")                 │
│ └─ }                                                    │
└─────────────────────────────────────────────────────────┘
    │
    ▼
USUARIO VE UI ACTUALIZADA (si OK) O MENSAJE DE ERROR


VENTAJAS DE ESTE FLUJO:
✓ Separación de responsabilidades clara
✓ Validaciones centralizadas
✓ Manejo de errores específicos
✓ Connection pooling automático
✓ Data binding automático (no refresh manual)
✓ Thread-safe UI updates
✓ Auditoría integrada
✓ Excepciones específicas (mejor debugging)
✓ Service layer reutilizable
✓ DAO mockeable para testing
```

---

## 3. MATRIZ DE ACOPLAMIENTO

### ACTUAL: Alto Acoplamiento

```
┌────────────────────┐
│   Login.java       │ ──(new Database)──┐
├────────────────────┤                    │
│ • Autenticación    │                    ▼
│ • Validación UI    │          ┌──────────────────┐
│ • Acceso a BD      │          │ Database.java    │
└────────────────────┘          │                  │
                                 │ • CRUD Usuarios  │
┌────────────────────┐           │ • CRUD Almacén   │
│ PanelProductos     │ ──────┐   │ • CRUD Productos │
├────────────────────┤       │   │ • Queries        │
│ • Listado          │       │   │ • Mapeo manual   │
│ • CRUD             │       │   │ • Conexiones     │
│ • Filtrado         │       └──→│                  │
└────────────────────┘           └──────────────────┘
                                          │
┌────────────────────┐                    │
│ AlmacenesPanel     │ ──(new Database)───┤
├────────────────────┤                    │
│ • Listado          │                    │
│ • CRUD             │                    ▼
│ • Acceso a BD      │          ┌──────────────────┐
└────────────────────┘          │    SQLite        │
                                 │  Inventario.db   │
┌────────────────────┐           └──────────────────┘
│ FormProducto       │ ──────┐
├────────────────────┤       │
│ • UI               │       └──(new Database)──┐
│ • Validación       │                         │
└────────────────────┘                         ▼
                          Multiple Database instances
                          (NO connection pooling)

PROBLEMAS:
❌ Múltiples instancias Database
❌ Vistas crean Database (tight coupling)
❌ Imposible mockiar Database para tests
❌ Cambios en BD afectan muchas vistas
❌ Sin separación de responsabilidades
❌ Difícil agregar nuevos paneles

ÍNDICE DE ACOPLAMIENTO ≈ 0.75
```

---

### FUTURA: Bajo Acoplamiento

```
┌──────────────────────────────────────────────────────────┐
│                   APPLICATION CONTEXT                    │
│                 (Dependency Injection)                    │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  DatabaseManager → ConnectionSource (Singleton)         │
│       │                                                  │
│       ├─→ UsuarioDAO ──┐                                │
│       ├─→ ProductoDAO ─┼─→ *Service ─┬─→ Controllers  │
│       └─→ AlmacenDAO ──┘             └─→ (inyectados)  │
│                                                          │
└──────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│ LoginController (.fxml)                 │
├─────────────────────────────────────────┤
│ - usuarioService: UsuarioService (iny) │
│                                         │
│ + handleLogin()                         │
│   └─ usuarioService.autenticar()      │
└─────────────────────────────────────────┘
         ▲
         │ (inyección)
         │
┌─────────────────────────────────────────┐
│ UsuarioService                          │
├─────────────────────────────────────────┤
│ - usuarioDAO: UsuarioDAO (iny)         │
│                                         │
│ + autenticar(nom, pass): Usuario       │
│   └─ usuarioDAO.findByNombre()        │
└─────────────────────────────────────────┘
         ▲
         │ (inyección)
         │
┌─────────────────────────────────────────┐
│ UsuarioDAO (extends BaseDaoImpl)        │
├─────────────────────────────────────────┤
│ + findByNombre(nombre)                 │
│ + create(usuario)                      │
│ + update(usuario)                      │
│ + deleteById(id)                       │
└─────────────────────────────────────────┘
         ▲
         │ (ConnectionSource inyectado)
         │
┌─────────────────────────────────────────┐
│ ORMLite ConnectionSource (Pooled)      │
├─────────────────────────────────────────┤
│ • Pool de 10 conexiones                │
│ • Reutilización automática             │
│ • Cierre automático                    │
└─────────────────────────────────────────┘
         ▼
┌─────────────────────────────────────────┐
│        SQLite Inventario.db            │
└─────────────────────────────────────────┘


VENTAJAS:
✓ Una sola instancia DatabaseManager
✓ Inyección de dependencias en todas partes
✓ Controllers NO crean servicios
✓ Servicios NO crean DAOs
✓ DAOs NO crean conexiones
✓ Fácil mockiar cada capa para tests
✓ Cambios en BD solo afectan DAO
✓ Reutilizar servicios en múltiples controllers
✓ Agregar nuevos controllers fácilmente

ÍNDICE DE ACOPLAMIENTO ≈ 0.25
```

---

## 4. SECUENCIA DE AUTENTICACIÓN

### ACTUAL

```
Usuario              Login (JPanel)        Database            SQLite
   │                     │                    │                 │
   ├─Ingresa credenciales│                    │                 │
   │                     │                    │                 │
   ├─Click "Ingresar"────┤                    │                 │
   │                     │                    │                 │
   │                     ├─new Database()────→│                 │
   │                     │                    │                 │
   │                     │    authenticate()──┤                 │
   │                     │                    │                 │
   │                     │              SELECT FROM usuarios
   │                     │                    │←─Query SQL────→ │
   │                     │                    │←─Resultado ────→│
   │                     │                    │                 │
   │                     │  ← Usuario object ─┤                 │
   │                     │                    │                 │
   │  ← onLogin callback  │                    │                 │
   │                     │                    │                 │
   ├─Navega a Home ──────┤                    │                 │
   │                     │                    │                 │

FLUJO SIMPLE PERO PROBLEMÁTICO:
• Sin validación centralizada
• MD5 es débil (vulnerable a rainbow tables)
• Database se crea localmente
• Sin auditoria
• Sin transacciones explícitas
```

---

### FUTURO

```
Usuario         LoginController      UsuarioService        UsuarioDAO    SQLite
   │                  │                   │                    │           │
   ├─Ingresa creds    │                   │                    │           │
   │                  │                   │                    │           │
   ├─Click "Ingresar" │                   │                    │           │
   │                  │                   │                    │           │
   │                  ├─{validate input}  │                    │           │
   │                  │                   │                    │           │
   │                  ├─autenticar()─────→│                    │           │
   │                  │                   │                    │           │
   │                  │          {validar entrada}             │           │
   │                  │                   │                    │           │
   │                  │          findByNombre()───────────────→│           │
   │                  │                   │                    │           │
   │                  │                   │    queryBuilder()  │           │
   │                  │                   │    .where().eq()   │           │
   │                  │                   │    .queryForFirst()│─(SELECT)─→│
   │                  │                   │                    │←─Usuario─┤
   │                  │                   │←─Usuario obj──────┤           │
   │                  │                   │                    │           │
   │                  │          {validar password}            │           │
   │                  │          BCrypt.checkpw()             │           │
   │                  │                   │                    │           │
   │                  │          {si OK: set fecha acceso}     │           │
   │                  │                   │                    │           │
   │                  │          update(usuario)──────────────→│           │
   │                  │                   │                    │           │
   │                  │                   │←─Confirmación────┤           │
   │                  │                   │                    │           │
   │                  │←─Usuario + roles─┤                    │           │
   │                  │                   │                    │           │
   │  ← onLogin() ────┤                   │                    │           │
   │                  │                   │                    │           │
   ├─Platform.runLater│                   │                    │           │
   │  (navega Home)   │                   │                    │           │
   │                  │                    │                    │           │

FLUJO ROBUSTO Y SEGURO:
✓ Validación de entrada completa
✓ BCrypt para hashing (seguro)
✓ Manejo de excepciones específicas
✓ Inyección de dependencias
✓ Transacciones automáticas ORMLite
✓ Auditoria integrada
✓ Thread-safe (Platform.runLater)
✓ Connection pooling automático
✓ Fácil testeable (DAO mockeable)
```

---

## 5. ESTRUCTURA DE PAQUETES PROPUESTA

### ACTUAL

```
mx.unison/
├── Main.java
├── controller/
│   └── Vistas.java
├── database/
│   └── Database.java
├── models/
│   ├── Almacen.java
│   ├── Producto.java
│   └── Usuario.java
├── util/
│   └── CryptoUtils.java
└── views/
    ├── form/
    │   ├── FormAlmacen.java
    │   └── FormProducto.java
    └── panel/
        ├── AlmacenesPanel.java
        ├── Home.java
        ├── Login.java
        ├── PanelProductos.java
        └── Vistas.java

PROBLEMAS:
• views/ contiene lógica + UI
• models/ son POJOs sin encapsulación
• No hay capa de servicios
• No hay DAO explícito
• Todo mezclado
```

---

### FUTURA

```
mx.unison/
│
├── Main.java  (bootstrapper)
│
├── config/
│   ├── ApplicationConfig.java
│   ├── ApplicationContext.java
│   └── DependencyInjector.java
│
├── models/
│   ├── Almacen.java          (@DatabaseTable)
│   ├── Producto.java         (@DatabaseTable)
│   ├── Usuario.java          (@DatabaseTable)
│   ├── dto/
│   │   ├── ProductoDTO.java
│   │   ├── AlmacenDTO.java
│   │   └── UsuarioDTO.java
│   └── exceptions/
│       ├── ValidationException.java
│       ├── EntityNotFoundException.java
│       ├── PersistenciaException.java
│       └── NegocioException.java
│
├── persistence/
│   ├── DatabaseManager.java
│   └── dao/
│       ├── GenericDAO.java (interfaz)
│       ├── UsuarioDAO.java
│       ├── ProductoDAO.java
│       └── AlmacenDAO.java
│
├── service/
│   ├── UsuarioService.java
│   ├── ProductoService.java
│   ├── AlmacenService.java
│   └── ValidationService.java
│
├── controller/
│   ├── LoginController.java
│   ├── HomeController.java
│   ├── ProductosController.java
│   ├── AlmacenesController.java
│   └── DialogControllers/
│       ├── FormProductoController.java
│       └── FormAlmacenController.java
│
├── ui/
│   ├── fx/
│   │   ├── UILoader.java
│   │   ├── DialogHelper.java
│   │   └── SceneManager.java
│   └── resources/
│       ├── fxml/
│       │   ├── login.fxml
│       │   ├── home.fxml
│       │   ├── productos.fxml
│       │   ├── almacenes.fxml
│       │   └── dialogs/
│       │       ├── form-producto.fxml
│       │       └── form-almacen.fxml
│       ├── css/
│       │   ├── styles.css
│       │   ├── dark-theme.css
│       │   └── light-theme.css
│       └── images/
│           └── icons/
│               ├── add.png
│               ├── edit.png
│               ├── delete.png
│               └── ... otros icons
│
├── util/
│   ├── security/
│   │   ├── PasswordUtils.java    (BCrypt)
│   │   └── EncryptionUtils.java
│   ├── validation/
│   │   ├── UsuarioValidator.java
│   │   ├── ProductoValidator.java
│   │   └── AlmacenValidator.java
│   ├── logging/
│   │   ├── AuditLogger.java
│   │   └── SystemLogger.java
│   └── converters/
│       ├── DateTimeConverter.java
│       └── CurrencyConverter.java
│
└── resources/
    ├── application.properties
    ├── database/
    │   ├── schema.sql
    │   └── migrations/
    │       ├── V1__initial_schema.sql
    │       └── V2__add_indices.sql
    ├── logging/
    │   └── logback.xml
    └── data/
        └── initial-data.sql

VENTAJAS:
✓ Separación clara de responsabilidades
✓ Cada paquete tiene un propósito específico
✓ Fácil de navegar
✓ Escalable (agregar nuevos features)
✓ Testeable (cada capa independiente)
✓ Reutilizable (servicios para múltiples controllers)
✓ DTOs para transferencia de datos
✓ Validators centralizados
✓ Logging y auditoría
✓ Security utils separadas
```

---

## CONCLUSIÓN

Esta arquitectura propuesta establece:

1. **Separación clara** entre presentación, negocio y persistencia
2. **Inyección de dependencias** para facilitar testing y mantenimiento
3. **Uso de patrones** (MVC, DAO, DTO, Singleton)
4. **Mejora de seguridad** (BCrypt en lugar de MD5)
5. **Mejor performance** (connection pooling, lazy loading)
6. **Escalabilidad** (fácil agregar nuevas features)
7. **Testabilidad** (cada capa es mockeable)

---

*Documentos complementarios: REPORTE_REINGENIERIA.md*

