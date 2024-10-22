package com.summonerscodex.controller;

import com.summonerscodex.services.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        usuarioService = new UsuarioService();
        mascaraContraseña(txtContraseñaSignIn, txtContraseñaSignInMascara, checkContraseñaSignIn);
    }

    public void mascaraContraseña(PasswordField pass, TextField text, CheckBox check) {
        text.setVisible(false);
        text.setManaged(false);
        
        text.managedProperty().bind(check.selectedProperty());
        text.visibleProperty().bind(check.selectedProperty());
        
        text.textProperty().bindBidirectional(pass.textProperty());

        check.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                text.setText(pass.getText());
                pass.setVisible(false);
                text.requestFocus();
            } else {
                pass.setText(text.getText());
                pass.setVisible(true);
            }
        });
    }

    @FXML
    public void manejarIngreso(ActionEvent event) {
        String username = txtUsuarioSignIn.getText();
        String password = txtContraseñaSignIn.getText();

        boolean autenticado = usuarioService.autenticarUsuario(username, password);

        if (autenticado) {
            cambiarAPantallaCampeones();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText(null);
            alert.setContentText("Usuario o contraseña incorrectos.");
            alert.showAndWait();
        }
    }

    public void borradoTexto(ActionEvent e) {
        txtUsuarioSignIn.clear();
        txtContraseñaSignInMascara.clear();
        txtContraseñaSignIn.clear();
    }

    public void cambiarAPantallaCampeones() {
        try {
            // Cargar la vista FXML usando el método loadForm
            Parent pantallaCampeones = loadForm("/com/summonerscodex/views/Pantalla_de_campeones.fxml");
            
            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(pantallaCampeones));
            stage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cambiar a la pantalla de campeones");
            alert.setContentText("Error: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private Parent loadForm(String url) throws IOException {    
        return FXMLLoader.load(getClass().getResource(url));    
    }

}
