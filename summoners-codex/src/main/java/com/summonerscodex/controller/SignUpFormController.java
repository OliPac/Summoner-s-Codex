package com.summonerscodex.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.summonerscodex.model.Usuario;
import com.summonerscodex.services.MongoDBConexion;
import com.summonerscodex.utils.ValidadorUsuario;
import com.summonerscodex.services.UsuarioService;

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
        MongoDBConexion.conectar();
        usuarioService = new UsuarioService(); // Inicializa el servicio de usuario
    }

    // Método para borrar los campos de texto
    @FXML
    private void borradoTextoRegistrar(ActionEvent e) {
        // Limpia los campos de texto del formulario de registro
        textCorreo.clear();
        textUsuario.clear();
        textContraseña.clear();
        textConfirmarContraseña.clear();
    }

    @FXML
    private void validarCorreo(ActionEvent event) {
        // Obtiene los valores de los campos de texto
        String usuario = textUsuario.getText();
        String correo = textCorreo.getText();
        String contraseña = textContraseña.getText();
        String confirmarContraseña = textConfirmarContraseña.getText();

        // Validar que los campos no estén vacíos
        if (sonCamposVacios(usuario, correo, contraseña, confirmarContraseña)) {
            mostrarAlerta("Campos Vacíos", "Por favor, completa todos los campos.");
            return; // Salir del método si hay campos vacíos
        }

        // Validar que la contraseña coincida con la confirmación
        if (!contraseña.equals(confirmarContraseña)) {
            mostrarAlerta("Contraseña Invalida", "Las contraseñas no coinciden.");
            return; // Salir del método si las contraseñas no coinciden
        }

        // Validar que el correo sea válido
        if (!ValidadorUsuario.esCorreoValido(correo)) {
            mostrarAlerta("Correo no válido", "Por favor, ingresa un correo electrónico válido.");
            return; // Salir del método si el correo no es válido
        }

        // Crear el nuevo usuario y guardar en la base de datos
        Usuario nuevoUsuario = new Usuario(usuario, correo, contraseña);
        String resultado = usuarioService.guardarUsuario(nuevoUsuario);

        // Mostrar alerta solo si hay un error
        if (!resultado.equals("Registro exitoso.")) {
            mostrarAlerta("Error en registro", resultado);
        } else {
            // Cambiar a la pantalla de campeones si el registro es exitoso
            cambiarAPantallaCampeones();
        }
    }

    // Método para cambiar a la pantalla de campeones
    private void cambiarAPantallaCampeones() {
        try {
            // Cargar la vista FXML usando el método loadForm
            Parent pantallaCampeones = loadForm("/com/summonerscodex/views/Seleccion_de_pantalla.fxml");

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(pantallaCampeones));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            // Manejo de excepciones durante la carga de la nueva pantalla
            logger.error("Error al cambiar a la pantalla de campeones", ex);
            mostrarAlerta("Error", "No se pudo cambiar a la pantalla de campeones: " + ex.getMessage());
        }
    }

    // Método para cargar una vista FXML
    private Parent loadForm(String url) throws IOException {
        return FXMLLoader.load(getClass().getResource(url));
    }

    // Método para mostrar un mensaje de alerta
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para verificar si algún campo está vacío
    private boolean sonCamposVacios(String usuario, String correo, String contraseña, String confirmarContraseña) {
        return usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty();
    }
}
