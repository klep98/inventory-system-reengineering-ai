# 📦 Inventory System Reengineering (AI-Powered)

## 🎓 Proyecto: Gestión de la Calidad del Software II
**Institución:** Universidad de Sonora  
**Alumno:** Romo Urías Caleb Alexey  
**Profesor:** Aguilera Luzania José Luis  

---

## 📝 ¿Cómo ejecutar?
Ejecutar la clase Launcher.java

## 📝 Descripción
Este proyecto representa la **reingeniería integral** de un sistema de inventario legado (Tarea 12). El objetivo principal fue elevar los estándares de calidad del software aplicando patrones de diseño modernos, seguridad avanzada y una interfaz de usuario intuitiva, todo esto asistido por Inteligencia Artificial (**GitHub Copilot**).

## 🚀 Tecnologías Utilizadas
* **Java 23**: Lenguaje base para la lógica de negocio.
* **JavaFX & FXML**: Para una interfaz gráfica moderna, responsiva y desacoplada.
* **ORMLite**: Implementación de Object-Relational Mapping para la persistencia en SQLite.
* **JUnit 5**: Framework para pruebas unitarias exhaustivas.
* **BCrypt**: Hashing de alta seguridad para la protección de credenciales.
* **Maven**: Gestión de dependencias y ciclo de vida del proyecto.

## 🏗️ Arquitectura del Software
El sistema ha sido rediseñado bajo una arquitectura de **N-Capas** siguiendo el patrón **MVC**:

1.  **mx.unison.modelos**: Entidades del sistema (Producto, Almacén, Usuario) con encapsulamiento total y anotaciones ORM.
2.  **mx.unison.datos (DAO)**: Capa de persistencia que abstrae las operaciones de la base de datos, eliminando el uso de SQL manual.
3.  **mx.unison.controladores**: Gestión de la lógica de la interfaz y mediación entre la vista y los servicios.
4.  **mx.unison.vistas**: Definiciones visuales en FXML y estilos personalizados en CSS.
5.  **mx.unison.utileria**: Clases de soporte para conexión a DB, hashing de seguridad y validaciones.

## 🛠️ Mejoras Significativas (Reingeniería)
* **Abstracción de Datos**: Migración de JDBC plano a **ORMLite**, logrando consultas *type-safe* y manejo automático de conexiones (*Pooling*).
* **Seguridad Critica**: Sustitución del algoritmo MD5 por **BCrypt** para el almacenamiento de contraseñas, cumpliendo con estándares actuales de ciberseguridad.
* **UI Modernizada**: Cambio de Swing a **JavaFX**, logrando una separación real entre el diseño visual y la lógica de control.
* **Mantenibilidad**: Reducción del acoplamiento en un **66%** y aumento de la testabilidad mediante la inyección de dependencias.

## 📸 Evidencia Visual
### Interfaz Principal
![Main View](screenshots/main-view.png)

### Gestión de Inventario
![Inventory](screenshots/inventory-list.png)

### Formularios Inteligentes
![Product Form](screenshots/product-form.png)

---
*Proyecto desarrollado como parte de la formación académica en Ingeniería en Sistemas de Información.*
