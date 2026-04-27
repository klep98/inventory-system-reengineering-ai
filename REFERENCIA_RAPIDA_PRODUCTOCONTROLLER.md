# 📋 ProductoController - Referencia Rápida

## Métodos CRUD Principales

| Método | Parámetros | Retorno | Excepciones |
|--------|-----------|---------|------------|
| `obtenerTodosLosProductos()` | - | `List<Producto>` | `SQLException` |
| `obtenerProductoPorId(Integer id)` | `id` | `Producto \| null` | `SQLException`, `IllegalArgumentException` |
| `guardarProducto(Producto, String usuario)` | Producto, usuario | `Integer (ID)` | `SQLException`, `IllegalArgumentException`, `NullPointerException` |
| `actualizarProducto(Producto, String usuario)` | Producto, usuario | `void` | `SQLException`, `IllegalArgumentException`, `NullPointerException` |
| `eliminarProducto(Integer id)` | `id` | `boolean` | `SQLException`, `IllegalArgumentException` |
| `eliminarProducto(Producto)` | Producto | `boolean` | `SQLException`, `IllegalArgumentException`, `NullPointerException` |
| `buscarProductosPorNombre(String nombre)` | nombre | `List<Producto>` | `SQLException`, `IllegalArgumentException` |
| `obtenerCantidadTotalDeProductos()` | - | `long` | `SQLException` |

---

## ✅ Validaciones Automáticas

### Nombre
- ✓ No es nulo ni vacío
- ✓ Máximo 255 caracteres
- ❌ Excepción: `"El nombre del producto no puede estar vacío"`

### Precio
- ✓ No es nulo
- ✓ Mayor a 0
- ✓ Máximo 1,000,000
- ❌ Excepción: `"El precio del producto debe ser mayor a 0"`

### Cantidad
- ✓ No es nula
- ✓ No negativa (≥ 0)
- ❌ Excepción: `"La cantidad del producto no puede ser negativa"`

### Descripción (Opcional)
- ✓ Máximo 1,000 caracteres
- ❌ Excepción: `"La descripción del producto no puede exceder 1000 caracteres"`

---

## 🔄 Auditoría Automática

En cada operación se registran automáticamente:

| Campo | En `guardarProducto()` | En `actualizarProducto()` |
|-------|------------------------|---------------------------|
| `fechaCreacion` | ⏱️ Se asigna NOW() | 🔒 No se modifica |
| `fechaModificacion` | ⏱️ Se asigna NOW() | ⏱️ Se actualiza NOW() |
| `ultimoUsuario` | 👤 Se asigna FROM parámetro | 👤 Se actualiza FROM parámetro |

---

## 🚀 Inicialización

```java
// Constructor simple - obtiene el DAO del Singleton
ProductoController controller = new ProductoController();
```

---

## 📊 Ejemplo Rápido - CRUD Completo

```java
ProductoController controller = new ProductoController();

// CREATE
Producto p = new Producto("Mouse", 50, 25.99);
Integer id = controller.guardarProducto(p, "ADMIN");

// READ
Producto recuperado = controller.obtenerProductoPorId(id);

// UPDATE
recuperado.setPrecio(20.99);
controller.actualizarProducto(recuperado, "ADMIN");

// DELETE
controller.eliminarProducto(id);
```

---

## 🎯 Casos de Uso Comunes

### Listar todos los productos
```java
List<Producto> todos = controller.obtenerTodosLosProductos();
```

### Crear un nuevo producto
```java
Producto p = new Producto("Teclado", 100, 45.50);
p.setDescripcion("Teclado mecánico RGB");
Integer id = controller.guardarProducto(p, "PRODUCTOS");
```

### Actualizar precio
```java
Producto p = controller.obtenerProductoPorId(5);
p.setPrecio(39.99);
controller.actualizarProducto(p, "ADMIN");
```

### Buscar productos
```java
List<Producto> resultados = controller.buscarProductosPorNombre("Monitor");
```

### Eliminar un producto
```java
boolean eliminado = controller.eliminarProducto(5);
if (eliminado) System.out.println("Eliminado");
```

### Contar total
```java
long total = controller.obtenerCantidadTotalDeProductos();
System.out.println("Total: " + total);
```

---

## ⚠️ Errores Comunes y Soluciones

### ❌ "El precio del producto debe ser mayor a 0"
```java
// Incorrecto
Producto p = new Producto("Item", 10, 0);
controller.guardarProducto(p, "ADMIN"); // Error

// Correcto
Producto p = new Producto("Item", 10, 15.99);
controller.guardarProducto(p, "ADMIN"); // OK
```

### ❌ "El nombre del producto no puede estar vacío"
```java
// Incorrecto
Producto p = new Producto("", 10, 15.99);
controller.guardarProducto(p, "ADMIN"); // Error

// Correcto
Producto p = new Producto("Mouse", 10, 15.99);
controller.guardarProducto(p, "ADMIN"); // OK
```

### ❌ "La cantidad del producto no puede ser negativa"
```java
// Incorrecto
Producto p = new Producto("Item", -5, 15.99);
controller.guardarProducto(p, "ADMIN"); // Error

// Correcto
Producto p = new Producto("Item", 0, 15.99);
controller.guardarProducto(p, "ADMIN"); // OK
```

### ❌ "El producto no puede ser nulo"
```java
// Incorrecto
controller.guardarProducto(null, "ADMIN"); // NullPointerException

// Correcto
Producto p = new Producto("Item", 10, 15.99);
controller.guardarProducto(p, "ADMIN"); // OK
```

---

## 🏗️ Desacoplamiento de la UI

El controlador NO depende de nuestra UI:
- ✅ No importa clases de Swing/JavaFX
- ✅ No accede a componentes visuales
- ✅ Lógica pura de negocio
- ✅ Puede ser usado desde CLI, API REST, etc.

---

## 📈 Características Avanzadas

### Búsqueda case-insensitive
```java
// Todos estos encuentran "Laptop"
controller.buscarProductosPorNombre("laptop");
controller.buscarProductosPorNombre("LAPTOP");
controller.buscarProductosPorNombre("LaPtOp");
```

### Búsqueda parcial
```java
// Encuentra: "Monitor 27", "Monitor 32", "Monitor 4K"
controller.buscarProductosPorNombre("Monitor");
```

### Validación de límites
```java
// Precio máximo: 1,000,000
Producto p = new Producto("Item", 100, 999999.99); // OK
Producto p = new Producto("Item", 100, 2000000.00); // Error

// Nombre máximo: 255 caracteres
// Descripción máxima: 1000 caracteres
```

---

## 🔐 Seguridad

- ✅ Validaciones ANTES de BD
- ✅ Prepared Statements (via ORMLite)
- ✅ Manejo de excepciones
- ✅ Sin inyección SQL
- ✅ Auditoría de cambios

---

## 📞 Soporte

Tipo de Error | Acción |
|---|---|
| `IllegalArgumentException` | Datos inválidos - revisar validaciones |
| `SQLException` | Problema con BD - revisar conexión |
| `NullPointerException` | Parámetro nulo - verificar inicialización |

---

**Estado:** ✅ Producción  
**Versión:** 1.0  
**Última actualización:** 2026-04-27

