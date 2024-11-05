package com.summonerscodex.controller;

import com.summonerscodex.services.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInFormController implements Initializable {

    @FXML
    private TextField txtUsuarioSignIn, txtContraseñaSignInMascara;

    @FXML
    private PasswordField txtContraseñaSignIn;

    @FXML
    private Button btnLimpiar, btnIngresar;

    @FXML
    private CheckBox checkContraseñaSignIn;

    private UsuarioService usuarioService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa el servicio de usuario y configura la máscara de contraseña
        usuarioService = new UsuarioService();
        configurarMascaraContraseña();
    }

    private void configurarMascaraContraseña() {
        // Configura la visibilidad y el enlace bidireccional para la contraseña
        txtContraseñaSignInMascara.setVisible(false);
        txtContraseñaSignInMascara.setManaged(false);

        txtContraseñaSignInMascara.managedProperty().bind(checkContraseñaSignIn.selectedProperty());
        txtContraseñaSignInMascara.visibleProperty().bind(checkContraseñaSignIn.selectedProperty());
        txtContraseñaSignInMascara.textProperty().bindBidirectional(txtContraseñaSignIn.textProperty());

        // Agrega un listener para cambiar entre campos de contraseña
        checkContraseñaSignIn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            txtContraseñaSignIn.setVisible(!newValue);
            if (newValue) {
                txtContraseñaSignInMascara.requestFocus(); 
            }
        });
    }

    @FXML
    private void manejarIngreso(ActionEvent event) {
        // Maneja la acción de inicio de sesión
        String username = txtUsuarioSignIn.getText();
        String password = txtContraseñaSignIn.getText();

        if (usuarioService.autenticarUsuario(username, password)) {
            cambiarAPantallaCampeones(); 
        } else {
            mostrarAlerta("Error de inicio de sesión", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void borradoTexto(ActionEvent event) {
        // Limpia los campos de texto
        txtUsuarioSignIn.clear();
        txtContraseñaSignInMascara.clear();
        txtContraseñaSignIn.clear();
    }

    private void cambiarAPantallaCampeones() {
        // Cambia a la pantalla de campeones
        try {
            Parent pantallaCampeones = cargarFXML("/com/summonerscodex/views/Seleccion_de_pantalla.fxml");
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(pantallaCampeones));
            stage.setMaximized(true); 
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo cambiar a la pantalla de campeones: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Parent cargarFXML(String url) throws IOException {
        return FXMLLoader.load(getClass().getResource(url));
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        // Muestra un cuadro de alerta con el mensaje especificado
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null); 
        alert.setContentText(mensaje);
        alert.showAndWait(); 
    }

    /**
     * Método para aumentar la escala del botón cuando se pasa el ratón por encima.
     */
    @FXML
    public void escalaBoton(MouseEvent event) {
        Button boton = (Button) event.getSource(); // Obtiene el botón que dispara el evento
        Scale scaleUp = new Scale(1.1, 1.1, boton.getWidth() / 2, boton.getHeight() / 2); // Crea un nuevo escalado
        boton.getTransforms().add(scaleUp); // Aplica el escalado al botón
    }

    /**
     * Método para restaurar el tamaño original del botón cuando se quita el ratón.
     */
    @FXML
    public void reducirBoton(MouseEvent event) {
        Button boton = (Button) event.getSource(); // Obtiene el botón que dispara el evento
        boton.getTransforms().clear(); // Elimina todas las transformaciones para regresar al tamaño original
    }
}
