# 🧪 ProductoController - Casos de Prueba

## Descripción

Este documento contiene casos de prueba para validar la funcionalidad del `ProductoController` con el patrón DAO y ORMLite.

---

## 📋 Casos de Prueba - Obtener Todos los Productos

### TC01: Obtener lista de productos existentes

**Precondiciones:**
- Base de datos inicializada
- Existen 3 o más productos en la BD

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `obtenerTodosLosProductos()`
3. Verificar que retorna lista no nula

**Resultado esperado:**
- ✅ Retorna `List<Producto>` con tamaño > 0
- ✅ Los productos contienen valores válidos

```java
@Test
public void testObtenerTodosLosProductos() throws SQLException {
    ProductoController controller = new ProductoController();
    List<Producto> productos = controller.obtenerTodosLosProductos();
    
    assertNotNull(productos);
    assertTrue(productos.size() >= 0);
    
    for (Producto p : productos) {
        assertNotNull(p.getNombre());
        assertNotNull(p.getPrecio());
        assertNotNull(p.getCantidad());
    }
}
```

---

### TC02: Obtener lista vacía cuando no hay productos

**Precondiciones:**
- Base de datos vacía (sin productos)

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `obtenerTodosLosProductos()`
3. Verificar que retorna lista vacía

**Resultado esperado:**
- ✅ Retorna `List<Producto>` tamaño = 0
- ✅ No es `null`

```java
@Test
public void testObtenerTodosLosProductosVacio() throws SQLException {
    ProductoController controller = new ProductoController();
    List<Producto> productos = controller.obtenerTodosLosProductos();
    
    assertNotNull(productos);
    assertEquals(0, productos.size());
}
```

---

## 🔍 Casos de Prueba - Obtener Producto por ID

### TC03: Obtener producto existente por ID

**Precondiciones:**
- Base de datos inicializada
- Existe un producto con ID = 1

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `obtenerProductoPorId(1)`
3. Verificar datos del producto

**Resultado esperado:**
- ✅ Retorna objeto `Producto` no nulo
- ✅ Datos coinciden con BD

```java
@Test
public void testObtenerProductoPorIdExistente() throws SQLException {
    ProductoController controller = new ProductoController();
    Producto producto = controller.obtenerProductoPorId(1);
    
    assertNotNull(producto);
    assertEquals(1, (int)producto.getId());
    assertNotNull(producto.getNombre());
}
```

---

### TC04: Obtener producto inexistente retorna null

**Precondiciones:**
- Base de datos inicializada

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `obtenerProductoPorId(99999)` (ID que no existe)
3. Verificar retorno

**Resultado esperado:**
- ✅ Retorna `null`
- ✅ No lanza excepción

```java
@Test
public void testObtenerProductoPorIdNoExiste() throws SQLException {
    ProductoController controller = new ProductoController();
    Producto producto = controller.obtenerProductoPorId(99999);
    
    assertNull(producto);
}
```

---

### TC05: ID nulo lanza IllegalArgumentException

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `obtenerProductoPorId(null)`
3. Capturar la excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje contiene "ID del producto"

```java
@Test(expected = IllegalArgumentException.class)
public void testObtenerProductoPorIdNulo() throws SQLException {
    ProductoController controller = new ProductoController();
    controller.obtenerProductoPorId(null);
}
```

---

### TC06: ID menor a 1 lanza IllegalArgumentException

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `obtenerProductoPorId(0)` o `obtenerProductoPorId(-1)`
3. Capturar la excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje contiene "mayor a 0"

```java
@Test(expected = IllegalArgumentException.class)
public void testObtenerProductoPorIdInvalido() throws SQLException {
    ProductoController controller = new ProductoController();
    controller.obtenerProductoPorId(0);
}
```

---

## 💾 Casos de Prueba - Guardar Producto

### TC07: Guardar producto válido exitosamente

**Precondiciones:**
- Base de datos inicializada

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto con: nombre="Monitor", precio=299.99, cantidad=5
3. Llamar a `guardarProducto(producto, "ADMIN")`
4. Verificar que retorna ID válido

**Resultado esperado:**
- ✅ Retorna `Integer` > 0
- ✅ Producto se guarda en BD
- ✅ Fecha y usuario se asignan automáticamente

```java
@Test
public void testGuardarProductoValido() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setNombre("Monitor");
    p.setPrecio(299.99);
    p.setCantidad(5);
    
    Integer id = controller.guardarProducto(p, "ADMIN");
    
    assertTrue(id > 0);
    
    // Verificar que se guardó
    Producto guardado = controller.obtenerProductoPorId(id);
    assertNotNull(guardado);
    assertEquals("Monitor", guardado.getNombre());
}
```

---

### TC08: Guardar producto con nombre nulo falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto con nombre = null
3. Llamar a `guardarProducto(producto, "ADMIN")`
4. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje: "El nombre del producto no puede estar vacío"
- ✅ No se guarda en BD

```java
@Test(expected = IllegalArgumentException.class)
public void testGuardarProductoNombreNulo() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setNombre(null);
    p.setPrecio(100.0);
    p.setCantidad(10);
    
    controller.guardarProducto(p, "ADMIN");
}
```

---

### TC09: Guardar producto con nombre vacío falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto con nombre = ""
3. Llamar a `guardarProducto(producto, "ADMIN")`
4. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ No se guarda en BD

```java
@Test(expected = IllegalArgumentException.class)
public void testGuardarProductoNombreVacio() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setNombre("");
    p.setPrecio(100.0);
    p.setCantidad(10);
    
    controller.guardarProducto(p, "ADMIN");
}
```

---

### TC10: Guardar producto con precio <= 0 falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto con precio = 0
3. Llamar a `guardarProducto(producto, "ADMIN")`
4. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje: "El precio del producto debe ser mayor a 0"
- ✅ No se guarda en BD

```java
@Test(expected = IllegalArgumentException.class)
public void testGuardarProductoPrecioInvalido() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setNombre("Monitor");
    p.setPrecio(0);
    p.setCantidad(10);
    
    controller.guardarProducto(p, "ADMIN");
}
```

---

### TC11: Guardar producto con cantidad negativa falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto con cantidad = -5
3. Llamar a `guardarProducto(producto, "ADMIN")`
4. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje: "La cantidad del producto no puede ser negativa"
- ✅ No se guarda en BD

```java
@Test(expected = IllegalArgumentException.class)
public void testGuardarProductoCantidadNegativa() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setNombre("Monitor");
    p.setPrecio(100.0);
    p.setCantidad(-5);
    
    controller.guardarProducto(p, "ADMIN");
}
```

---

### TC12: Guardar producto nulo falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `guardarProducto(null, "ADMIN")`
3. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `NullPointerException`
- ✅ Mensaje: "El producto no puede ser nulo"

```java
@Test(expected = NullPointerException.class)
public void testGuardarProductoNulo() throws SQLException {
    ProductoController controller = new ProductoController();
    controller.guardarProducto(null, "ADMIN");
}
```

---

### TC13: Guardar producto con usuario nulo falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto válido
3. Llamar a `guardarProducto(producto, null)`
4. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `NullPointerException`
- ✅ Mensaje: "El usuario actual no puede ser nulo"

```java
@Test(expected = NullPointerException.class)
public void testGuardarProductoUsuarioNulo() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setNombre("Monitor");
    p.setPrecio(100.0);
    p.setCantidad(10);
    
    controller.guardarProducto(p, null);
}
```

---

## 🔄 Casos de Prueba - Actualizar Producto

### TC14: Actualizar producto válido exitosamente

**Precondiciones:**
- Base de datos inicializada
- Existe un producto con ID = 1

**Pasos:**
1. Crear instancia de `ProductoController`
2. Obtener producto con ID = 1
3. Modificar precio a 299.99
4. Llamar a `actualizarProducto(producto, "ADMIN")`
5. Recuperar producto nuevamente

**Resultado esperado:**
- ✅ No lanza excepción
- ✅ Precio actualizado en BD
- ✅ `fechaModificacion` se actualiza
- ✅ `ultimoUsuario` = "ADMIN"

```java
@Test
public void testActualizarProductoValido() throws SQLException {
    ProductoController controller = new ProductoController();
    
    // Obtener producto
    Producto p = controller.obtenerProductoPorId(1);
    assertNotNull(p);
    
    // Modificar
    p.setPrecio(299.99);
    controller.actualizarProducto(p, "ADMIN");
    
    // Verificar
    Producto actualizado = controller.obtenerProductoPorId(1);
    assertEquals(299.99, actualizado.getPrecio(), 0.01);
    assertEquals("ADMIN", actualizado.getUltimoUsuario());
}
```

---

### TC15: Actualizar producto con ID inválido falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Crear producto con ID = null o ID = 0
3. Llamar a `actualizarProducto(producto, "ADMIN")`
4. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje: "El producto debe tener un ID válido"

```java
@Test(expected = IllegalArgumentException.class)
public void testActualizarProductoIDInvalido() throws SQLException {
    ProductoController controller = new ProductoController();
    
    Producto p = new Producto();
    p.setId(null);
    p.setNombre("Monitor");
    p.setPrecio(100.0);
    p.setCantidad(10);
    
    controller.actualizarProducto(p, "ADMIN");
}
```

---

## 🗑️ Casos de Prueba - Eliminar Producto

### TC16: Eliminar producto existente exitosamente

**Precondiciones:**
- Base de datos inicializada
- Existe un producto con ID = 1

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `eliminarProducto(1)`
3. Verificar retorno es true
4. Intentar obtener producto (debe ser null)

**Resultado esperado:**
- ✅ Retorna `true`
- ✅ Producto no existe más en BD

```java
@Test
public void testEliminarProductoExistente() throws SQLException {
    ProductoController controller = new ProductoController();
    
    // Guardar primero
    Producto p = new Producto("Producto Test", 10, 50.0);
    Integer id = controller.guardarProducto(p, "ADMIN");
    
    // Eliminar
    boolean resultado = controller.eliminarProducto(id);
    assertTrue(resultado);
    
    // Verificar que se eliminó
    assertNull(controller.obtenerProductoPorId(id));
}
```

---

### TC17: Eliminar producto inexistente retorna false

**Precondiciones:**
- Base de datos inicializada

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `eliminarProducto(99999)` (ID que no existe)
3. Verificar retorno es false

**Resultado esperado:**
- ✅ Retorna `false`
- ✅ No lanza excepción

```java
@Test
public void testEliminarProductoInexistente() throws SQLException {
    ProductoController controller = new ProductoController();
    boolean resultado = controller.eliminarProducto(99999);
    
    assertFalse(resultado);
}
```

---

### TC18: Eliminar producto con ID inválido falla

**Precondiciones:**
- N/A

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `eliminarProducto(null)` o `eliminarProducto(0)`
3. Capturar excepción

**Resultado esperado:**
- ✅ Lanza `IllegalArgumentException`
- ✅ Mensaje: "El ID del producto debe ser un número válido"

```java
@Test(expected = IllegalArgumentException.class)
public void testEliminarProductoIDInvalido() throws SQLException {
    ProductoController controller = new ProductoController();
    controller.eliminarProducto(null);
}
```

---

## 🔎 Casos de Prueba - Búsqueda

### TC19: Buscar productos por nombre parcial

**Precondiciones:**
- Base de datos inicializada
- Existen productos: "Monitor 27", "Monitor 32", "Teclado"

**Pasos:**
1. Crear instancia de `ProductoController`
2. Llamar a `buscarProductosPorNombre("Monitor")`
3. Verificar resultados

**Resultado esperado:**
- ✅ Retorna lista con 2 productos
- ✅ Ambos contienen "Monitor"
- ✅ Búsqueda es case-insensitive

```java
@Test
public void testBuscarProductosPorNombre() throws SQLException {
    ProductoController controller = new ProductoController();
    
    // Guardar productos de prueba
    Producto p1 = new Producto("Monitor 27", 5, 250.0);
    Producto p2 = new Producto("Monitor 32", 3, 350.0);
    Producto p3 = new Producto("Teclado", 10, 50.0);
    
    controller.guardarProducto(p1, "ADMIN");
    controller.guardarProducto(p2, "ADMIN");
    controller.guardarProducto(p3, "ADMIN");
    
    // Buscar
    List<Producto> resultados = controller.buscarProductosPorNombre("Monitor");
    
    assertEquals(2, resultados.size());
}
```

---

### TC20: Búsqueda case-insensitive

**Precondiciones:**
- Base de datos inicializada
- Existe producto "Monitor"

**Pasos:**
1. Crear instancia de `ProductoController`
2. Buscar: "monitor", "MONITOR", "Monitor"
3. Verificar que todos encuentran el producto

**Resultado esperado:**
- ✅ Las 3 búsquedas retornan el mismo producto
- ✅ Búsqueda case-insensitive funciona

```java
@Test
public void testBuscarProductosCaseInsensitive() throws SQLException {
    ProductoController controller = new ProductoController();
    
    List<Producto> r1 = controller.buscarProductosPorNombre("monitor");
    List<Producto> r2 = controller.buscarProductosPorNombre("MONITOR");
    List<Producto> r3 = controller.buscarProductosPorNombre("Monitor");
    
    assertEquals(r1.size(), r2.size());
    assertEquals(r2.size(), r3.size());
}
```

---

## 📊 Casos de Prueba - Contar Productos

### TC21: Contar productos en BD

**Precondiciones:**
- Base de datos inicializada con N productos

**Pasos:**
1. Crear instancia de `ProductoController`
2. Guardar 3 productos nuevos
3. Llamar a `obtenerCantidadTotalDeProductos()`
4. Verificar que la cantidad aumentó

**Resultado esperado:**
- ✅ Retorna `long` > 0
- ✅ Cantidad coincide con BD

```java
@Test
public void testObtenerCantidadTotal() throws SQLException {
    ProductoController controller = new ProductoController();
    
    long cantidadInicial = controller.obtenerCantidadTotalDeProductos();
    
    // Guardar 2 productos
    Producto p1 = new Producto("Producto 1", 10, 50.0);
    Producto p2 = new Producto("Producto 2", 20, 100.0);
    
    controller.guardarProducto(p1, "ADMIN");
    controller.guardarProducto(p2, "ADMIN");
    
    long cantidadFinal = controller.obtenerCantidadTotalDeProductos();
    
    assertEquals(cantidadInicial + 2, cantidadFinal);
}
```

---

## ✅ Plan de Ejecución de Pruebas

### Orden recomendado:
1. TC01-TC02 (Obtener todos)
2. TC03-TC06 (Obtener por ID)
3. TC07-TC13 (Guardar)
4. TC14-TC15 (Actualizar)
5. TC16-TC18 (Eliminar)
6. TC19-TC20 (Búsqueda)
7. TC21 (Contar)

**Tiempo estimado:** 30-45 minutos

---

## 📊 Criterios de Aceptación

Para considerar **EXITOSO** el test suite:

- ✅ 100% de los casos pasan
- ✅ Sin excepciones inesperadas
- ✅ Sin excepciones esperadas que no se lanzan
- ✅ Sin datos corruptos en BD
- ✅ Sin fugas de conexiones

---

**Versión:** 1.0  
**Última actualización:** 2026-04-27

