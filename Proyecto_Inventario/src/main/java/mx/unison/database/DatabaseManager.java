package mx.unison.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;
import mx.unison.models.Usuario;

import java.sql.SQLException;

/**
 * Clase Singleton para gestionar la conexión a la base de datos SQLite 'Inventario.db'
 * utilizando ORMLite. Inicializa las tablas y proporciona acceso a los DAOs.
 */
public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:Inventario.db";

    private static DatabaseManager instance;

    private ConnectionSource connectionSource;
    private Dao<Usuario, Integer> usuarioDao;
    private Dao<Producto, Integer> productoDao;
    private Dao<Almacen, Integer> almacenDao;

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa la conexión y crea las tablas si no existen.
     */
    private DatabaseManager() {
        try {
            // Inicializar la conexión
            connectionSource = new JdbcConnectionSource(DATABASE_URL);

            // Crear las tablas si no existen
            TableUtils.createTableIfNotExists(connectionSource, Usuario.class);
            TableUtils.createTableIfNotExists(connectionSource, Producto.class);
            TableUtils.createTableIfNotExists(connectionSource, Almacen.class);

            // Inicializar los DAOs
            usuarioDao = DaoManager.createDao(connectionSource, Usuario.class);
            productoDao = DaoManager.createDao(connectionSource, Producto.class);
            almacenDao = DaoManager.createDao(connectionSource, Almacen.class);

        } catch (SQLException e) {
            throw new RuntimeException("Error inicializando la base de datos", e);
        }
    }

    /**
     * Método público estático para obtener la instancia única del DatabaseManager.
     * Implementa el patrón Singleton con inicialización lazy.
     *
     * @return La instancia única de DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Obtiene el DAO para la entidad Usuario.
     *
     * @return DAO de Usuario
     */
    public Dao<Usuario, Integer> getUsuarioDao() {
        return usuarioDao;
    }

    /**
     * Obtiene el DAO para la entidad Producto.
     *
     * @return DAO de Producto
     */
    public Dao<Producto, Integer> getProductoDao() {
        return productoDao;
    }

    /**
     * Obtiene el DAO para la entidad Almacen.
     *
     * @return DAO de Almacen
     */
    public Dao<Almacen, Integer> getAlmacenDao() {
        return almacenDao;
    }

    /**
     * Obtiene la fuente de conexión para operaciones avanzadas.
     *
     * @return ConnectionSource
     */
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Cierra la conexión a la base de datos.
     * Debe llamarse al finalizar la aplicación.
     */
    public void close() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) { // Cambiamos SQLException por Exception genérica
                System.err.println("Error cerrando la conexión: " + e.getMessage());
            }
        }
    }
}
