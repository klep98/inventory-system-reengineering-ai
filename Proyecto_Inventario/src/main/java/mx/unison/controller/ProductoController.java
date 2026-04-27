package mx.unison.controller;

import com.j256.ormlite.dao.Dao;
import mx.unison.database.DatabaseManager;
import mx.unison.models.Producto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductoController {
    private final Dao<Producto, Integer> productoDao;

    public ProductoController() throws SQLException {
        this.productoDao = DatabaseManager.getInstance().getProductoDao();
    }

    public List<Producto> obtenerTodosLosProductos() throws SQLException {
        List<Producto> productos = productoDao.queryForAll();
        return productos != null ? productos : new ArrayList<>();
    }

    public Integer guardarProducto(Producto producto, String usuarioActual) throws SQLException {
        Objects.requireNonNull(producto, "El producto no puede ser nulo");
        validarProducto(producto);

        String now = java.time.LocalDateTime.now().toString();
        if (producto.getFechaCreacion() == null) {
            producto.setFechaCreacion(now);
        }
        producto.setFechaModificacion(now);
        producto.setUltimoUsuario(usuarioActual);

        productoDao.create(producto);
        return producto.getId();
    }

    public boolean eliminarProducto(Integer id) throws SQLException {
        if (id == null) return false;
        return productoDao.deleteById(id) > 0;
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("Precio debe ser mayor a 0");
        }
    }
}