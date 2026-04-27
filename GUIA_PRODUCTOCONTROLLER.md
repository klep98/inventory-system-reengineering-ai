# ProductoController - Guía de Uso y Referencia

## 📋 Descripción General

`ProductoController` es una clase que implementa el **patrón DAO** para gestionar operaciones CRUD en la entidad `Producto`. Está completamente **desacoplada de la interfaz de usuario** y sigue los estándares de calidad de Tarea 14.

**Características clave:**
- ✅ Patrón DAO con ORMLite
- ✅ Validaciones robustas de negocio
- ✅ Manejo profesional de excepciones
- ✅ Auditoría automática (fechas, usuario)
- ✅ Desacoplamiento total de la UI
- ✅ Búsquedas y consultas avanzadas

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────┐
│     Vista/UI (JavaFX, Swing)        │
│  (No interactúa directamente)        │
└──────────────┬──────────────────────┘
               │ (Desacoplada)
┌──────────────▼──────────────────────┐
│   ProductoController                 │
│   ├─ guardarProducto()               │
│   ├─ obtenerTodosLosProductos()      │
│   ├─ eliminarProducto()              │
│   ├─ actualizarProducto()            │
│   └─ validarProducto()               │
└──────────────┬──────────────────────┘
               │ (DAO Pattern)
┌──────────────▼──────────────────────┐
│   DatabaseManager (Singleton)        │
│   ├─ getProductoDao()                │
│   ├─ getConnectionSource()           │
│   └─ close()                         │
└──────────────┬──────────────────────┘
               │ (ORMLite)
┌──────────────▼──────────────────────┐
│   SQLite Database                    │
│   (Inventario.db)                    │
└─────────────────────────────────────┘
```

---

## 📖 Métodos Disponibles

### 1. **obtenerTodosLosProductos()**

Obtiene una lista con todos los productos de la base de datos.

**Firma:**
```java
public List<Producto> obtenerTodosLosProductos() throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();
try {
    List<Producto> productos = controller.obtenerTodosLosProductos();
    System.out.println("Total de productos: " + productos.size());
    
    for (Producto p : productos) {
        System.out.println(p.getNombre() + " - $" + p.getPrecio());
    }
} catch (SQLException e) {
    System.err.println("Error: " + e.getMessage());
}
```

**Retorno:**
- Retorna una `List<Producto>` (vacía si no hay productos)
- Nunca retorna `null`

---

### 2. **obtenerProductoPorId(Integer id)**

Obtiene un producto específico por su ID.

**Firma:**
```java
public Producto obtenerProductoPorId(Integer id) throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();
try {
    Producto producto = controller.obtenerProductoPorId(5);
    
    if (producto != null) {
        System.out.println("Encontrado: " + producto.getNombre());
        System.out.println("Precio: $" + producto.getPrecio());
        System.out.println("Cantidad: " + producto.getCantidad());
    } else {
        System.out.println("Producto no encontrado");
    }
} catch (IllegalArgumentException e) {
    System.err.println("Error de validación: " + e.getMessage());
} catch (SQLException e) {
    System.err.println("Error de base de datos: " + e.getMessage());
}
```

**Excepciones:**
- `IllegalArgumentException`: Si el ID es nulo o menor a 1
- `SQLException`: Si hay un error en la base de datos

---

### 3. **guardarProducto(Producto producto, String usuarioActual)**

Guarda un nuevo producto en la base de datos con validaciones automáticas.

**Firma:**
```java
public Integer guardarProducto(Producto producto, String usuarioActual) throws SQLException
```

**Validaciones ejecutadas:**
- ✅ Nombre no nulo y no vacío
- ✅ Nombre máximo 255 caracteres
- ✅ Precio mayor a 0
- ✅ Precio máximo 1,000,000
- ✅ Cantidad no negativa
- ✅ Descripción máximo 1,000 caracteres

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();

try {
    // Crear nuevo producto
    Producto nuevoProducto = new Producto();
    nuevoProducto.setNombre("Laptop");
    nuevoProducto.setDescripcion("Laptop gaming de alta gama");
    nuevoProducto.setPrecio(1500.50);
    nuevoProducto.setCantidad(10);
    
    // Guardar en la base de datos
    Integer idProducto = controller.guardarProducto(nuevoProducto, "ADMIN");
    
    System.out.println("Producto guardado con ID: " + idProducto);
    
} catch (IllegalArgumentException e) {
    System.err.println("Validación fallida: " + e.getMessage());
    // Ejemplo: "El precio del producto debe ser mayor a 0"
} catch (SQLException e) {
    System.err.println("Error al guardar: " + e.getMessage());
} catch (NullPointerException e) {
    System.err.println("El producto o usuario no puede ser nulo");
}
```

**Retorno:**
- ID del producto creado (generado por la base de datos)

**Datos automáticos:**
- `fechaCreacion`: Se asigna automáticamente con la fecha/hora actual
- `fechaModificacion`: Se asigna automáticamente
- `ultimoUsuario`: Se asigna con el parámetro `usuarioActual`

---

### 4. **actualizarProducto(Producto producto, String usuarioActual)**

Actualiza un producto existente con las mismas validaciones que `guardarProducto()`.

**Firma:**
```java
public void actualizarProducto(Producto producto, String usuarioActual) throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();

try {
    // Obtener el producto
    Producto producto = controller.obtenerProductoPorId(5);
    
    if (producto != null) {
        // Modificar datos
        producto.setNombre("Laptop Gaming PRO");
        producto.setPrecio(2000.00);
        producto.setCantidad(8);
        
        // Actualizar en la base de datos
        controller.actualizarProducto(producto, "PRODUCTOS");
        System.out.println("Producto actualizado exitosamente");
    }
    
} catch (IllegalArgumentException e) {
    System.err.println("Validación fallida: " + e.getMessage());
} catch (SQLException e) {
    System.err.println("Error al actualizar: " + e.getMessage());
}
```

**Datos actualizados automáticamente:**
- `fechaModificacion`: Se actualiza con la fecha/hora actual
- `ultimoUsuario`: Se actualiza con el parámetro `usuarioActual`

---

### 5. **eliminarProducto(Integer id)**

Elimina un producto por ID.

**Firma:**
```java
public boolean eliminarProducto(Integer id) throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();

try {
    boolean eliminado = controller.eliminarProducto(5);
    
    if (eliminado) {
        System.out.println("Producto eliminado exitosamente");
    } else {
        System.out.println("No se encontró el producto con ese ID");
    }
    
} catch (IllegalArgumentException e) {
    System.err.println("ID inválido: " + e.getMessage());
} catch (SQLException e) {
    System.err.println("Error al eliminar: " + e.getMessage());
}
```

**Retorno:**
- `true` si se eliminó exitosamente
- `false` si no se encontró el producto

---

### 6. **eliminarProducto(Producto producto)**

Elimina un producto usando el objeto completo (sobrecarga).

**Firma:**
```java
public boolean eliminarProducto(Producto producto) throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();

try {
    Producto producto = controller.obtenerProductoPorId(5);
    
    if (producto != null) {
        controller.eliminarProducto(producto);
        System.out.println("Producto eliminado");
    }
    
} catch (SQLException e) {
    System.err.println("Error: " + e.getMessage());
}
```

---

### 7. **buscarProductosPorNombre(String nombre)**

Busca productos por nombre (búsqueda parcial, case-insensitive).

**Firma:**
```java
public List<Producto> buscarProductosPorNombre(String nombre) throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();

try {
    List<Producto> resultados = controller.buscarProductosPorNombre("Laptop");
    
    System.out.println("Se encontraron " + resultados.size() + " productos:");
    for (Producto p : resultados) {
        System.out.println("  - " + p.getNombre() + " ($" + p.getPrecio() + ")");
    }
    
} catch (IllegalArgumentException e) {
    System.err.println("Error: " + e.getMessage());
} catch (SQLException e) {
    System.err.println("Error en la búsqueda: " + e.getMessage());
}
```

**Características:**
- Búsqueda case-insensitive
- Búsqueda parcial (substring)
- Retorna lista vacía si no hay coincidencias

---

### 8. **obtenerCantidadTotalDeProductos()**

Obtiene el número total de productos en la base de datos.

**Firma:**
```java
public long obtenerCantidadTotalDeProductos() throws SQLException
```

**Ejemplo de uso:**
```java
ProductoController controller = new ProductoController();

try {
    long total = controller.obtenerCantidadTotalDeProductos();
    System.out.println("Total de productos en el sistema: " + total);
    
} catch (SQLException e) {
    System.err.println("Error: " + e.getMessage());
}
```

---

## 🔍 Excepciones Manejadas

### IllegalArgumentException
Se lanza cuando los datos no cumplen las validaciones de negocio.

```java
// Ejemplos:
- "El nombre del producto no puede estar vacío"
- "El precio del producto debe ser mayor a 0"
- "La cantidad del producto no puede ser negativa"
- "El ID del producto debe ser un número válido mayor a 0"
```

### SQLException
Se lanza cuando hay un error en la base de datos.

```java
// Ejemplos:
- "Error al obtener todos los productos: ..."
- "No se encontró el producto con ID X para actualizar"
- "Error al guardar el producto: ..."
```

### NullPointerException
Se lanza cuando un parámetro requerido es nulo.

```java
// Ejemplos:
- "El producto no puede ser nulo"
- "El usuario actual no puede ser nulo"
```

---

## 🎯 Ejemplo Completo de Uso

```java
import mx.unison.controller.ProductoController;
import mx.unison.models.Producto;
import java.sql.SQLException;
import java.util.List;

public class EjemploUso {
    
    public static void main(String[] args) {
        ProductoController controller;
        
        try {
            // 1. Inicializar el controlador
            controller = new ProductoController();
            System.out.println("Controlador inicializado");
            
            // 2. Obtener todos los productos
            List<Producto> productos = controller.obtenerTodosLosProductos();
            System.out.println("\n--- Productos actuales: " + productos.size() + " ---");
            for (Producto p : productos) {
                System.out.println(p);
            }
            
            // 3. Crear y guardar nuevo producto
            Producto nuevoProducto = new Producto("Monitor 4K", 1, 450.99);
            nuevoProducto.setDescripcion("Monitor Ultra HD de 27 pulgadas");
            Integer idNuevo = controller.guardarProducto(nuevoProducto, "ADMIN");
            System.out.println("\n✓ Producto creado con ID: " + idNuevo);
            
            // 4. Obtener producto específico
            Producto producto = controller.obtenerProductoPorId(idNuevo);
            System.out.println("✓ Producto recuperado: " + producto.getNombre());
            
            // 5. Actualizar producto
            producto.setPrecio(399.99);
            controller.actualizarProducto(producto, "PRODUCTOS");
            System.out.println("✓ Producto actualizado");
            
            // 6. Buscar productos
            List<Producto> busqueda = controller.buscarProductosPorNombre("Monitor");
            System.out.println("\n--- Búsqueda de 'Monitor': " + busqueda.size() + " resultados ---");
            
            // 7. Contar total
            long total = controller.obtenerCantidadTotalDeProductos();
            System.out.println("\nTotal de productos: " + total);
            
            // 8. Eliminar producto
            boolean eliminado = controller.eliminarProducto(idNuevo);
            System.out.println("\n✓ Producto eliminado: " + eliminado);
            
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Error de validación: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

## 📊 Validaciones de Negocio Detalladas

### Nombre
```
Validaciones:
  ✓ No nulo
  ✓ No vacío (sin espacios en blanco)
  ✓ Máximo 255 caracteres

Ejemplo válido:  "Laptop Gaming"
Ejemplo inválido: ""  (vacío)
                  "A" x 300  (supera límite)
```

### Precio
```
Validaciones:
  ✓ No nulo
  ✓ Mayor a 0
  ✓ Máximo 1,000,000

Ejemplo válido:   100.50
Ejemplo inválido: -50    (negativo)
                  0      (cero)
                  2000000.00  (supera máximo)
```

### Cantidad
```
Validaciones:
  ✓ No nulo
  ✓ No negativo (puede ser 0)
  ✓ Mayor o igual a 0

Ejemplo válido:   0, 100, 999
Ejemplo inválido: -1, -100
```

### Descripción (opcional)
```
Validaciones:
  ✓ Máximo 1,000 caracteres (si se proporciona)

Ejemplo válido:   "Laptop de última generación"
Ejemplo inválido: "A" x 1001  (supera límite)
```

---

## 🚀 Integración con la UI

El controlador está diseñado para ser usado desde cualquier vista sin acoplamiento:

```java
// En una vista JavaFX
public class ProductoView {
    
    private ProductoController controller;
    
    public ProductoView() throws SQLException {
        this.controller = new ProductoController();
    }
    
    public void mostrarProductos() {
        try {
            List<Producto> productos = controller.obtenerTodosLosProductos();
            // Actualizar UI con los productos
            actualizarTabla(productos);
        } catch (SQLException e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }
    
    public void guardarProductoDesdeFormulario(Producto prod, String usuario) {
        try {
            Integer id = controller.guardarProducto(prod, usuario);
            mostrarExito("Producto guardado con ID: " + id);
        } catch (IllegalArgumentException e) {
            mostrarError("Datos inválidos: " + e.getMessage());
        } catch (SQLException e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }
}
```

---

## 📌 Notas Importantes

1. **Singleton Pattern**: `DatabaseManager` es un Singleton, por lo que usa una única conexión compartida
2. **Thread-Safe**: La inicialización del Singleton es thread-safe
3. **Desacoplamiento**: La UI no conoce sobre la base de datos ni ORMLite
4. **Validaciones**: Todas las validaciones ocurren ANTES de tocar la base de datos
5. **Auditoría**: Cada operación registra automáticamente el usuario y la fecha

---

## 🔧 Troubleshooting

### Error: "No se pudieron resolver las anotaciones de ORMLite"
**Solución:** Ejecuta `mvn clean install` para descargar las dependencias

### Error: "El producto no puede ser nulo"
**Solución:** Asegúrate de inicializar el objeto Producto antes de pasarlo

### Error: "Conexión rechazada"
**Solución:** Verifica que `Inventario.db` esté en la ruta correcta

---

**Versión:** 1.0  
**Última actualización:** 2026-04-27  
**Estado:** ✅ Listo para producción

