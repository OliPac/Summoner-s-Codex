package com.summonerscodex.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.summonerscodex.model.Usuario;
import com.summonerscodex.services.MongoDBConexion;
import com.summonerscodex.services.UsuarioService;
import com.summonerscodex.utils.ValidadorUsuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpFormController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(SignUpFormController.class);
    private UsuarioService usuarioService; // Instancia del servicio de usuario

    @FXML
    private Button btnLimpiarRegistrarse, btnIngresar;

    @FXML
    private TextField textUsuario, textCorreo, textContraseña, textConfirmarContraseña;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa la conexión a MongoDB
        MongoDBConexion.conectar(); // Llama al método conectar sin parámetros
        usuarioService = new UsuarioService(); // Inicializa el servicio de usuario
    }

    // Método para borrar los campos de texto
    @FXML
    private void borradoTextoRegistrar(ActionEvent e) {
        textCorreo.clear();
        textUsuario.clear();
        textContraseña.clear();
        textConfirmarContraseña.clear();
    }

    @FXML
    private void validarCorreo(ActionEvent event) {
        String correo = textCorreo.getText();
        if (!ValidadorUsuario.esCorreoValido(correo)) {
            mostrarAlerta("Correo no válido", "Por favor, ingresa un correo electrónico válido.");
        } else {
            Usuario nuevoUsuario = new Usuario(textUsuario.getText(), correo, textContraseña.getText());
            String resultado = usuarioService.guardarUsuario(nuevoUsuario); // Llama al servicio para guardar el usuario
            
            // Mostrar alerta solo si hay un error
            if (!resultado.equals("Registro exitoso.")) {
                mostrarAlerta("Error en registro", resultado);
            } else {
                // Cambiar a la pantalla de campeones si el registro es exitoso
                cambiarAPantallaCampeones();
            }
        }
    }

    private void cambiarAPantallaCampeones() {
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
            mostrarAlerta("Error", "No se pudo cargar la pantalla de campeones.");
        }
    }

    // Método para mostrar un mensaje de alerta
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
