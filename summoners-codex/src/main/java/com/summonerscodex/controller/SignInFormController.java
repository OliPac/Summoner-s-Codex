package com.summonerscodex.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInFormController implements Initializable {

    @FXML
    private TextField txtUsuarioSignIn, txtContraseñaSignInMascara;

    @FXML
    private PasswordField txtContraseñaSignIn;
    @FXML
    private Button btnLimpiar,btnIngresar;

    @FXML
    private CheckBox checkContraseñaSignIn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mascaraContraseña(txtContraseñaSignIn, txtContraseñaSignInMascara, checkContraseñaSignIn);

    }

    // Método para mostrar/ocultar la contraseña
    public void mascaraContraseña(PasswordField pass, TextField text, CheckBox check) {
        // Inicialmente oculta el campo de texto
        text.setVisible(false);
        text.setManaged(false);

        // Vincula la visibilidad y la gestión del campo de texto a la selección del checkbox
        text.managedProperty().bind(check.selectedProperty());
        text.visibleProperty().bind(check.selectedProperty());

        // Sincroniza el texto del campo de contraseña con el campo de texto
        text.textProperty().bindBidirectional(pass.textProperty());

        // Añade un listener para el CheckBox que actualiza el campo de contraseña y el TextField
        check.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Si el CheckBox está seleccionado, se muestra el TextField
                text.setText(pass.getText());
                pass.setVisible(false); // Oculta el PasswordField
                text.requestFocus(); // Pone el foco en el TextField
            } else {
                // Si el CheckBox está deseleccionado, se oculta el TextField
                pass.setText(text.getText());
                pass.setVisible(true); // Muestra el PasswordField
            }
        });
    }
    public void borradoTexto(ActionEvent e) {
    	   txtUsuarioSignIn.setText("");
    	   txtContraseñaSignInMascara.setText("");
    	  txtContraseñaSignIn.setText("");

    }


}
