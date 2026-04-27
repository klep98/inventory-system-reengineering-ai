# EJEMPLOS DE CÓDIGO - ARQUITECTURA FUTURA
## Implementación de Componentes Clave

---

## 1. MODELOS CON ANOTACIONES ORMLITE

### Usuario.java (Refactorizado)

```java
package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad Usuario del sistema con anotaciones ORMLite.
 * Encapsulada completamente con validaciones.
 */
@DatabaseTable(tableName = "usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nombre;

    @DatabaseField(canBeNull = false)
    private String passwordHash;

    @DatabaseField(canBeNull = false)
    private String rol;

    @DatabaseField
    private LocalDateTime fechaCreacion;

    @DatabaseField
    private LocalDateTime fechaUltimoInicio;

    // Constructor sin argumentos requerido por ORMLite
    public Usuario() {
    }

    // Constructor con parámetros principales
    public Usuario(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters con validación
    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener mínimo 3 caracteres");
        }
        this.nombre = nombre.trim();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isEmpty()) {
            throw new IllegalArgumentException("El hash de contraseña no puede estar vacío");
        }
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        if (!isRolValido(rol)) {
            throw new IllegalArgumentException("Rol inválido: " + rol);
        }
        this.rol = rol;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaUltimoInicio() {
        return fechaUltimoInicio;
    }

    public void setFechaUltimoInicio(LocalDateTime fechaUltimoInicio) {
        this.fechaUltimoInicio = fechaUltimoInicio;
    }

    // Métodos de utilidad
    private static boolean isRolValido(String rol) {
        return rol != null && 
               (rol.equals("ADMIN") || rol.equals("PRODUCTOS") || rol.equals("ALMACENES"));
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id != null && id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
```

### Producto.java (Refactorizado)

```java
package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad Producto con anotaciones ORMLite.
 * Incluye referencia a Almacén con Foreign Key.
 */
@DatabaseTable(tableName = "productos")
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String nombre;

    @DatabaseField
    private String descripcion;

    @DatabaseField(canBeNull = false)
    private Integer cantidad;

    @DatabaseField(canBeNull = false)
    private Double precio;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true)
    private Almacen almacen;

    @DatabaseField
    private LocalDateTime fechaCreacion;

    @DatabaseField
    private LocalDateTime fechaModificacion;

    @DatabaseField
    private String ultimoUsuario;

    // Constructor sin argumentos
    public Producto() {
    }

    // Constructor con parámetros principales
    public Producto(String nombre, Integer cantidad, Double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser >= 0");
        }
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        if (precio == null || precio < 0) {
            throw new IllegalArgumentException("El precio debe ser >= 0");
        }
        this.precio = precio;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", almacen=" + (almacen != null ? almacen.getNombre() : "N/A") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id != null && id.equals(producto.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
```

---

## 2. CAPA DAO CON ORMLITE

### GenericDAO.java (Interfaz Base)

```java
package mx.unison.persistence.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD.
 * Proporciona contrato base para todos los DAOs.
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Crea una nueva entidad en la base de datos.
     */
    void create(T entity) throws SQLException;
    
    /**
     * Obtiene entidad por ID.
     */
    T findById(ID id) throws SQLException;
    
    /**
     * Obtiene todas las entidades.
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Actualiza una entidad existente.
     */
    void update(T entity) throws SQLException;
    
    /**
     * Elimina una entidad por ID.
     */
    void deleteById(ID id) throws SQLException;
    
    /**
     * Cuenta registros totales.
     */
    long count() throws SQLException;
}
```

### UsuarioDAO.java (Implementación)

```java
package mx.unison.persistence.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import mx.unison.models.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO para operaciones con la entidad Usuario.
 * Extiende BaseDaoImpl de ORMLite.
 */
public class UsuarioDAO extends BaseDaoImpl<Usuario, Integer> {

    public UsuarioDAO(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Usuario.class);
    }

    /**
     * Busca un usuario por nombre.
     */
    public Usuario findByNombre(String nombre) throws SQLException {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        List<Usuario> usuarios = queryBuilder()
                .where()
                .eq("nombre", nombre.trim())
                .query();
        
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    /**
     * Busca todos los usuarios con un rol específico.
     */
    public List<Usuario> findByRol(String rol) throws SQLException {
        if (rol == null || rol.isEmpty()) {
            throw new IllegalArgumentException("El rol no puede estar vacío");
        }
        
        return queryBuilder()
                .where()
                .eq("rol", rol)
                .orderBy("nombre", true)
                .query();
    }

    /**
     * Obtiene todos los usuarios ordenados por nombre.
     */
    @Override
    public List<Usuario> findAll() throws SQLException {
        return queryBuilder()
                .orderBy("nombre", true)
                .query();
    }

    /**
     * Valida que no exista duplicado antes de crear.
     */
    @Override
    public void create(Usuario usuario) throws SQLException {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no puede ser null");
        }
        
        Usuario existente = findByNombre(usuario.getNombre());
        if (existente != null) {
            throw new SQLException("Usuario '" + usuario.getNombre() + "' ya existe");
        }
        
        super.create(usuario);
    }
}
```

### ProductoDAO.java (Implementación)

```java
package mx.unison.persistence.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import mx.unison.models.Producto;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO para operaciones con la entidad Producto.
 */
public class ProductoDAO extends BaseDaoImpl<Producto, Integer> {

    public ProductoDAO(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Producto.class);
    }

    /**
     * Busca productos por almacén.
     */
    public List<Producto> findByAlmacen(Integer almacenId) throws SQLException {
        if (almacenId == null || almacenId <= 0) {
            throw new IllegalArgumentException("Almacén ID inválido");
        }
        
        return queryBuilder()
                .where()
                .eq("almacen_id", almacenId)
                .orderBy("nombre", true)
                .query();
    }

    /**
     * Busca productos por nombre (LIKE).
     */
    public List<Producto> buscarPorNombre(String termino) throws SQLException {
        if (termino == null || termino.isEmpty()) {
            return findAll();
        }
        
        String pattern = "%" + termino.trim() + "%";
        return queryBuilder()
                .where()
                .like("nombre", pattern)
                .orderBy("nombre", true)
                .query();
    }

    /**
     * Busca productos con precio menor al especificado.
     */
    public List<Producto> findBajoPrecio(Double precioMaximo) throws SQLException {
        if (precioMaximo == null || precioMaximo < 0) {
            throw new IllegalArgumentException("Precio máximo inválido");
        }
        
        return queryBuilder()
                .where()
                .le("precio", precioMaximo)
                .orderBy("precio", true)
                .query();
    }

    /**
     * Busca productos con stock bajo (cantidad < umbral).
     */
    public List<Producto> findStockBajo(Integer umbral) throws SQLException {
        if (umbral == null || umbral < 0) {
            throw new IllegalArgumentException("Umbral inválido");
        }
        
        return queryBuilder()
                .where()
                .lt("cantidad", umbral)
                .orderBy("cantidad", true)
                .query();
    }

    /**
     * Obtiene todos ordenados por nombre.
     */
    @Override
    public List<Producto> findAll() throws SQLException {
        return queryBuilder()
                .orderBy("nombre", true)
                .query();
    }
}
```

---

## 3. CAPA DE SERVICIOS

### UsuarioService.java

```java
package mx.unison.service;

import mx.unison.models.Usuario;
import mx.unison.persistence.dao.UsuarioDAO;
import mx.unison.util.security.PasswordUtils;
import mx.unison.util.logging.AuditLogger;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Servicio de negocio para operaciones con usuarios.
 * Encapsula validaciones, autenticación y auditoría.
 */
public class UsuarioService {
    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());
    
    private final UsuarioDAO usuarioDAO;
    private final AuditLogger auditLogger;

    public UsuarioService(UsuarioDAO usuarioDAO, AuditLogger auditLogger) {
        this.usuarioDAO = usuarioDAO;
        this.auditLogger = auditLogger;
    }

    /**
     * Autentica un usuario con sus credenciales.
     * 
     * @param nombre Nombre del usuario
     * @param passwordPlain Contraseña sin hash
     * @return Usuario autenticado
     * @throws CredencialesInvalidasException Si las credenciales son inválidas
     * @throws SQLException Si hay error en BD
     */
    public Usuario autenticar(String nombre, String passwordPlain) 
            throws CredencialesInvalidasException, SQLException {
        
        // Validar entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (passwordPlain == null || passwordPlain.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Buscar usuario en BD
        Usuario usuario = usuarioDAO.findByNombre(nombre);
        if (usuario == null) {
            auditLogger.log("SISTEMA", "LOGIN_FALLIDO", "Usuario no existe: " + nombre);
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        // Verificar contraseña con BCrypt
        if (!BCrypt.checkpw(passwordPlain, usuario.getPasswordHash())) {
            auditLogger.log(nombre, "LOGIN_FALLIDO", "Contraseña incorrecta");
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        // Actualizar fecha de último acceso
        usuario.setFechaUltimoInicio(LocalDateTime.now());
        usuarioDAO.update(usuario);

        // Registrar en auditoría
        auditLogger.log(nombre, "LOGIN_EXITOSO", "Usuario autenticado correctamente");

        logger.info("Usuario " + nombre + " autenticado exitosamente");
        return usuario;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     */
    public Usuario crearUsuario(String nombre, String passwordPlain, String rol) 
            throws SQLException {
        
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener mínimo 3 caracteres");
        }
        
        if (passwordPlain == null || passwordPlain.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo 6 caracteres");
        }

        // Verificar disponibilidad del nombre
        if (usuarioDAO.findByNombre(nombre) != null) {
            throw new SQLException("El usuario '" + nombre + "' ya existe");
        }

        // Crear usuario
        Usuario usuario = new Usuario(nombre, rol);
        String passwordHash = BCrypt.hashpw(passwordPlain, BCrypt.gensalt(10));
        usuario.setPasswordHash(passwordHash);

        // Persistir
        usuarioDAO.create(usuario);

        // Auditoría
        auditLogger.log("SISTEMA", "CREATE_USER", "Nuevo usuario creado: " + nombre);

        logger.info("Usuario " + nombre + " creado exitosamente");
        return usuario;
    }

    /**
     * Cambia la contraseña de un usuario.
     */
    public void cambiarContraseña(String nombreUsuario, String passwordActualPlain, 
                                  String passwordNuevaPlain) 
            throws CredencialesInvalidasException, SQLException {
        
        // Validar usuario existe
        Usuario usuario = usuarioDAO.findByNombre(nombreUsuario);
        if (usuario == null) {
            throw new CredencialesInvalidasException("Usuario no existe");
        }

        // Verificar contraseña actual
        if (!BCrypt.checkpw(passwordActualPlain, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Contraseña actual incorrecta");
        }

        // Validar nueva contraseña
        if (passwordNuevaPlain == null || passwordNuevaPlain.length() < 6) {
            throw new IllegalArgumentException("La nueva contraseña debe tener mínimo 6 caracteres");
        }

        // Hash de nueva contraseña
        String nuevoHash = BCrypt.hashpw(passwordNuevaPlain, BCrypt.gensalt(10));
        usuario.setPasswordHash(nuevoHash);

        // Actualizar
        usuarioDAO.update(usuario);

        // Auditoría
        auditLogger.log(nombreUsuario, "CHANGE_PASSWORD", "Contraseña actualizada");

        logger.info("Contraseña de " + nombreUsuario + " actualizada");
    }
}
```

### ProductoService.java

```java
package mx.unison.service;

import mx.unison.models.Producto;
import mx.unison.models.Almacen;
import mx.unison.models.dto.ProductoDTO;
import mx.unison.persistence.dao.ProductoDAO;
import mx.unison.persistence.dao.AlmacenDAO;
import mx.unison.util.logging.AuditLogger;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Servicio de negocio para operaciones con productos.
 */
public class ProductoService {
    private static final Logger logger = Logger.getLogger(ProductoService.class.getName());
    
    private final ProductoDAO productoDAO;
    private final AlmacenDAO almacenDAO;
    private final AuditLogger auditLogger;
    private final ValidationService validationService;

    public ProductoService(ProductoDAO productoDAO, AlmacenDAO almacenDAO, 
                          AuditLogger auditLogger, ValidationService validationService) {
        this.productoDAO = productoDAO;
        this.almacenDAO = almacenDAO;
        this.auditLogger = auditLogger;
        this.validationService = validationService;
    }

    /**
     * Crea un nuevo producto.
     */
    public Producto crearProducto(Producto producto, String usuarioActual) 
            throws ValidationException, BusinessException, SQLException {
        
        // Validar entrada
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }

        // Validar campos
        validationService.validarProducto(producto);

        // Validar que almacén existe si se especificó
        if (producto.getAlmacen() != null && producto.getAlmacen().getId() != null) {
            Almacen almacen = almacenDAO.findById(producto.getAlmacen().getId());
            if (almacen == null) {
                throw new BusinessException("El almacén especificado no existe");
            }
            producto.setAlmacen(almacen);
        }

        // Establecer metadata
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setUltimoUsuario(usuarioActual);

        // Persistir
        productoDAO.create(producto);

        // Auditoría
        auditLogger.log(usuarioActual, "CREATE_PRODUCT", 
            "Producto creado: " + producto.getNombre() + " (ID: " + producto.getId() + ")");

        logger.info("Producto creado: " + producto.getNombre());
        return producto;
    }

    /**
     * Actualiza un producto existente.
     */
    public void actualizarProducto(Producto producto, String usuarioActual) 
            throws ValidationException, BusinessException, SQLException {
        
        // Validar entrada
        if (producto == null || producto.getId() == null) {
            throw new IllegalArgumentException("El producto y su ID son obligatorios");
        }

        // Validar campos
        validationService.validarProducto(producto);

        // Verificar que existe
        Producto existente = productoDAO.findById(producto.getId());
        if (existente == null) {
            throw new BusinessException("El producto no existe");
        }

        // Actualizar metadata
        producto.setFechaCreacion(existente.getFechaCreacion()); // No cambiar fecha creación
        producto.setFechaModificacion(LocalDateTime.now());
        producto.setUltimoUsuario(usuarioActual);

        // Persistir cambios
        productoDAO.update(producto);

        // Auditoría
        auditLogger.log(usuarioActual, "UPDATE_PRODUCT", 
            "Producto actualizado: " + producto.getNombre() + " (ID: " + producto.getId() + ")");

        logger.info("Producto actualizado: " + producto.getNombre());
    }

    /**
     * Lista todos los productos.
     */
    public List<ProductoDTO> listarProductos() throws SQLException {
        List<Producto> productos = productoDAO.findAll();
        return productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos por almacén.
     */
    public List<ProductoDTO> buscarPorAlmacen(Integer almacenId) throws SQLException {
        if (almacenId == null || almacenId <= 0) {
            throw new IllegalArgumentException("ID de almacén inválido");
        }
        
        List<Producto> productos = productoDAO.findByAlmacen(almacenId);
        return productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos por nombre.
     */
    public List<ProductoDTO> buscar(String termino) throws SQLException {
        List<Producto> productos = productoDAO.buscarPorNombre(termino);
        return productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos con stock bajo.
     */
    public List<ProductoDTO> obtenerStockBajo(Integer umbral) throws SQLException {
        List<Producto> productos = productoDAO.findStockBajo(umbral);
        return productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte Producto a DTO (separación de capas).
     */
    private ProductoDTO convertToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCantidad(producto.getCantidad());
        dto.setPrecio(producto.getPrecio());
        
        if (producto.getAlmacen() != null) {
            dto.setAlmacenId(producto.getAlmacen().getId());
            dto.setAlmacenNombre(producto.getAlmacen().getNombre());
        }
        
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaModificacion(producto.getFechaModificacion());
        dto.setUltimoUsuario(producto.getUltimoUsuario());
        
        return dto;
    }
}
```

---

## 4. CONTROLADOR JAVAFX

### LoginController.java

```java
package mx.unison.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.unison.models.Usuario;
import mx.unison.service.UsuarioService;
import mx.unison.util.logging.SystemLogger;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Controlador para la pantalla de login.
 * Usa FXML e inyección de dependencias.
 */
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblError;

    private UsuarioService usuarioService;
    private Consumer<Usuario> onLoginSuccess;

    /**
     * Constructor para inyección de dependencias.
     */
    public LoginController(UsuarioService usuarioService, Consumer<Usuario> onLoginSuccess) {
        this.usuarioService = usuarioService;
        this.onLoginSuccess = onLoginSuccess;
    }

    /**
     * Inicializa el controlador (llamado por JavaFX automáticamente).
     */
    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> manejarLogin());
        
        // Permitir login con Enter
        pfPassword.setOnAction(event -> manejarLogin());
        
        // Limpiar error cuando empieza a escribir
        tfUsuario.textProperty().addListener((obs, old, new_val) -> lblError.setText(""));
    }

    /**
     * Maneja el evento de login.
     */
    @FXML
    private void manejarLogin() {
        String nombre = tfUsuario.getText().trim();
        String password = pfPassword.getText();

        // Validar entrada
        if (nombre.isEmpty()) {
            mostrarError("Por favor ingrese el nombre de usuario");
            return;
        }
        if (password.isEmpty()) {
            mostrarError("Por favor ingrese la contraseña");
            return;
        }

        // Deshabilitar botón mientras se autentica
        btnLogin.setDisable(true);

        // Intentar autenticación (en thread adicional para no bloquear UI)
        new Thread(() -> {
            try {
                Usuario usuario = usuarioService.autenticar(nombre, password);
                
                // Ejecutar callback en thread de JavaFX
                javafx.application.Platform.runLater(() -> {
                    logger.info("Login exitoso para usuario: " + usuario.getNombre());
                    onLoginSuccess.accept(usuario);
                });
                
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    logger.warning("Login fallido: " + e.getMessage());
                    mostrarError("Credenciales inválidas");
                    btnLogin.setDisable(false);
                });
            }
        }).start();
    }

    /**
     * Muestra un mensaje de error en la UI.
     */
    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setStyle("-fx-text-fill: red;");
    }

    /**
     * Limpia los campos del formulario.
     */
    public void limpiar() {
        tfUsuario.clear();
        pfPassword.clear();
        lblError.setText("");
    }
}
```

---

## 5. EXCEPCIONES PERSONALIZADAS

### CredencialesInvalidasException.java

```java
package mx.unison.exception;

/**
 * Excepción lanzada cuando las credenciales de usuario son inválidas.
 */
public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }

    public CredencialesInvalidasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
```

### ValidationException.java

```java
package mx.unison.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Excepción lanzada cuando hay errores de validación.
 */
public class ValidationException extends Exception {
    private List<String> errores;

    public ValidationException(String mensaje) {
        super(mensaje);
        this.errores = new ArrayList<>();
    }

    public ValidationException(List<String> errores) {
        super("Errores de validación: " + String.join(", ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void agregarError(String error) {
        errores.add(error);
    }

    public boolean tieneErrores() {
        return !errores.isEmpty();
    }
}
```

---

## 6. UTILIDADES DE SEGURIDAD

### PasswordUtils.java

```java
package mx.unison.util.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidades para manejo seguro de contraseñas con BCrypt.
 */
public class PasswordUtils {
    private static final int LOG_ROUNDS = 10; // Número de rondas de BCrypt

    /**
     * Genera un hash BCrypt de una contraseña en plaintext.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verifica una contraseña plaintext contra su hash BCrypt.
     */
    public static boolean verifyPassword(String plainPassword, String hash) {
        if (plainPassword == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hash);
    }

    /**
     * Valida que la contraseña cumpla con requisitos mínimos.
     */
    public static boolean isPasswordValid(String password) {
        if (password == null) return false;
        
        // Requisitos: mínimo 6 caracteres
        // Opcional: agregar validaciones más estrictas aquí
        return password.length() >= 6;
    }

    /**
     * Genera una contraseña temporal aleatoria.
     */
    public static String generateTemporaryPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }
}
```

---

## RESUMEN DE VENTAJAS

### Antes (JDBC Manual)

```java
Database db = new Database();
var usr = db.authenticate(usuario, password);
```

### Después (ORMLite + Servicios)

```java
Usuario usr = usuarioService.autenticar(usuario, password);
```

**Ventajas del nuevo enfoque**:
- ✅ Validaciones centralizadas
- ✅ BCrypt en lugar de MD5
- ✅ Manejo de excepciones específicas
- ✅ Auditoría integrada
- ✅ Thread-safe
- ✅ Testeable (DAO injectable)
- ✅ Type-safe queries
- ✅ Separación clara de responsabilidades

---

*Para más detalles sobre la arquitectura, ver REPORTE_REINGENIERIA.md*

