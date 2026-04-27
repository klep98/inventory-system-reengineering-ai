package mx.unison.controller; // Asegúrate que el package coincida con tu carpeta

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.unison.models.Producto;
import java.sql.SQLException;

public class ProductoViewHandler {
    @FXML private TextField txtNombre, txtPrecio, txtCantidad;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId, colStock;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;

    private ProductoController controller;

    @FXML
    public void initialize() {
        try {
            controller = new ProductoController();
            configurarTabla();
            cargarDatos();
        } catch (SQLException e) {
            mostrarAlerta("Error de BD", e.getMessage());
        }
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    private void cargarDatos() throws SQLException {
        tablaProductos.setItems(FXCollections.observableArrayList(controller.obtenerTodosLosProductos()));
    }

    @FXML
    private void handleGuardar() {
        try {
            Producto p = new Producto(txtNombre.getText(),
                    Integer.parseInt(txtCantidad.getText()),
                    Double.parseDouble(txtPrecio.getText()));
            controller.guardarProducto(p, "Caleb_Romo");
            cargarDatos();
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                controller.eliminarProducto(seleccionado.getId());
                cargarDatos();
            } catch (SQLException e) {
                mostrarAlerta("Error", e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.clear(); txtPrecio.clear(); txtCantidad.clear();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}