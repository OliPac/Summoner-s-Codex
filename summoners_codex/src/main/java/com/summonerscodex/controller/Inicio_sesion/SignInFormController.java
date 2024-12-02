package com.summonerscodex.controller.Inicio_sesion;

import com.summonerscodex.services.UsuarioService;
import com.summonerscodex.utils.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

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

        // Enlaza las propiedades de visibilidad y administración del TextField con el CheckBox
        txtContraseñaSignInMascara.managedProperty().bind(checkContraseñaSignIn.selectedProperty());
        txtContraseñaSignInMascara.visibleProperty().bind(checkContraseñaSignIn.selectedProperty());
        txtContraseñaSignInMascara.textProperty().bindBidirectional(txtContraseñaSignIn.textProperty()); // Sincroniza el texto

        // Agrega un listener para cambiar entre campos de contraseña
        checkContraseñaSignIn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Cambia entre el PasswordField y el TextField para ver la contraseña
            txtContraseñaSignIn.setVisible(!newValue); 
            if (newValue) {
                txtContraseñaSignInMascara.requestFocus();
            }
        });
    }

    @FXML
    private void manejarIngreso(ActionEvent event) {
        String username = txtUsuarioSignIn.getText();
        String password = txtContraseñaSignIn.getText();

        if (usuarioService.autenticarUsuario(username, password)) {
            // Si la autenticación es exitosa, se guarda el usuario en la sesión
            Session.getInstance().setUsuarioAutenticado(username);
            cambiarAPantallaSeleccion();
        } else {
            mostrarAlerta("Error de inicio de sesión", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void borradoTexto(ActionEvent event) {     
        txtUsuarioSignIn.clear();
        txtContraseñaSignInMascara.clear();
        txtContraseñaSignIn.clear();
    }

    private void cambiarAPantallaSeleccion() {
        // Cambia a la pantalla de seleccion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent pantallaCampeones = loader.load();
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(pantallaCampeones));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo cambiar a la pantalla de campeones: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        // Muestra un cuadro de alerta con el mensaje especificado
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); 
    }
}
