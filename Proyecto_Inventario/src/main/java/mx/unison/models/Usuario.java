package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable(tableName = "usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nombre;

    @DatabaseField(canBeNull = false)
    private String passwordHash;

    @DatabaseField(canBeNull = false)
    private String rol;

    @DatabaseField
    private String fechaCreacion;

    @DatabaseField
    private String fechaUltimoInicio;

    public Usuario() {
    }

    public Usuario(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
        // Guardamos la fecha como String ISO-8601
        this.fechaCreacion = java.time.LocalDateTime.now().toString();
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaUltimoInicio() {
        return fechaUltimoInicio;
    }

    public void setFechaUltimoInicio(String fechaUltimoInicio) {
        this.fechaUltimoInicio = fechaUltimoInicio;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre='" + nombre + "', rol='" + rol + "'}";
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