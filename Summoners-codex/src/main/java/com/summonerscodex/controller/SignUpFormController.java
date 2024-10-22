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
        String usuario = textUsuario.getText();
        String correo = textCorreo.getText();
        String contraseña = textContraseña.getText();
        String confirmarContraseña = textConfirmarContraseña.getText();
        
        // Validar que los campos no estén vacíos
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
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
    // Método para mostrar un mensaje de alerta
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
