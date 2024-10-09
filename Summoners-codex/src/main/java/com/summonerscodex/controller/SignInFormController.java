package com.summonerscodex.controller;

import com.summonerscodex.services.UsuarioService; // Asegúrate de importar tu servicio de usuario
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class SignInFormController implements Initializable {

    @FXML
    private TextField txtUsuarioSignIn, txtContraseñaSignInMascara;

    @FXML
    private PasswordField txtContraseñaSignIn;
    @FXML
    private Button btnLimpiar, btnIngresar;

    @FXML
    private CheckBox checkContraseñaSignIn;

    private UsuarioService usuarioService; // Servicio de autenticación

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioService = new UsuarioService(); // Inicializa el servicio
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

    // Método para manejar el evento de ingreso
    @FXML
    public void manejarIngreso(ActionEvent event) {
        String username = txtUsuarioSignIn.getText();
        String password = txtContraseñaSignIn.getText(); // Obtiene la contraseña (enmascarada)

        // Verifica las credenciales
        boolean autenticado = usuarioService.autenticarUsuario(username, password);
        
        if (autenticado) {
            // Alerta de éxito
        	cambiarAPantallaCampeones();
            
            // Aquí puedes redirigir al usuario a otra vista, por ejemplo:
            // cambiarVistaAlTablero();
        } else {
            // Alerta de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText(null);
            alert.setContentText("Usuario o contraseña incorrectos.");
            alert.showAndWait();
        }
    }

    public void borradoTexto(ActionEvent e) {
        txtUsuarioSignIn.setText("");
        txtContraseñaSignInMascara.setText("");
        txtContraseñaSignIn.setText("");
    }
    public void cambiarAPantallaCampeones() {
        try {
            // Cargar el nuevo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Pantalla_de_campeones.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena con el nuevo contenido
            Stage stage = (Stage) btnIngresar.getScene().getWindow(); // Obtén el escenario actual
            stage.setScene(new Scene(root)); // Establece la nueva escena
            stage.show(); // Muestra el nuevo escenario

        } catch (Exception e) {
            e.printStackTrace(); // Manejo de excepciones
          
        }
    }
}
