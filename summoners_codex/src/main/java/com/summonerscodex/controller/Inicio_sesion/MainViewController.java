package com.summonerscodex.controller.Inicio_sesion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

    @FXML
    private Button btnIniciarSesion, btnRegistrarse; 

    @FXML
    private StackPane containerForm;

    private VBox signInForm, signUpForm; 

    // Maneja los eventos de clic en los botones para alternar entre formularios de inicio de sesión y registro
    @FXML
    public void actionEvent(ActionEvent e) {
        if (e.getSource() == btnIniciarSesion) {
            mostrarFormularioIniciarSesion();
        } else if (e.getSource() == btnRegistrarse) {
            mostrarFormularioRegistrarse();
        }
    }

    // Método de inicialización llamado después de que se haya cargado el FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            signInForm = cargarFormulario("/com/summonerscodex/views/InicioSesion/SignInForm.fxml");
            signUpForm = cargarFormulario("/com/summonerscodex/views/InicioSesion/SignUpForm.fxml");       
            containerForm.getChildren().addAll(signInForm, signUpForm);              
            signInForm.setVisible(true);
            signUpForm.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, "Error al cargar los formularios", ex);
        }
    }

    // Carga un formulario desde el archivo FXML especificado
    private VBox cargarFormulario(String url) throws IOException {
        return FXMLLoader.load(getClass().getResource(url)); // Cargar el diseño FXML
    }

    // Mostrar el formulario de inicio de sesión y ocultar el formulario de registro
    private void mostrarFormularioIniciarSesion() {
        signInForm.setVisible(true);
        signUpForm.setVisible(false);
    }

    // Mostrar el formulario de registro y ocultar el formulario de inicio de sesión
    private void mostrarFormularioRegistrarse() {
        signUpForm.setVisible(true);
        signInForm.setVisible(false);
    }
}
