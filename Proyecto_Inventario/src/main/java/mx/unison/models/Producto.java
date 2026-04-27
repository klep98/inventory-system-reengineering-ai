package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable(tableName = "productos")
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
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
    private String fechaCreacion;

    @DatabaseField
    private String fechaModificacion;

    @DatabaseField
    private String ultimoUsuario;

    public Producto() {}

    public Producto(String nombre, Integer cantidad, Double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaCreacion = java.time.LocalDateTime.now().toString();
    }

    // Getters y Setters corregidos
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Almacen getAlmacen() { return almacen; }
    public void setAlmacen(Almacen almacen) { this.almacen = almacen; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(String fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public String getUltimoUsuario() { return ultimoUsuario; }
    public void setUltimoUsuario(String ultimoUsuario) { this.ultimoUsuario = ultimoUsuario; }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", nombre='" + nombre + "'}";
    }
}