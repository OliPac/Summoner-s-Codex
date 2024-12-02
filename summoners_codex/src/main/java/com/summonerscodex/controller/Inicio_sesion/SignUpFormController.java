package com.summonerscodex.controller.Inicio_sesion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.summonerscodex.model.Usuario;
import com.summonerscodex.services.MongoDBConexion;
import com.summonerscodex.utils.Session;
import com.summonerscodex.utils.ValidadorUsuario;
import com.summonerscodex.services.UsuarioService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpFormController implements Initializable {

    private UsuarioService usuarioService; 

    @FXML
    private Button btnLimpiarRegistrarse, btnIngresar;

    @FXML
    private TextField textUsuario, textCorreo, textContraseña, textConfirmarContraseña;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa la conexión a MongoDB
        MongoDBConexion.conectar();
        usuarioService = new UsuarioService(); 
    }

    @FXML
    private void borradoTextoRegistrar(ActionEvent e) {
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
            return; 
        }

        // Validar que la contraseña coincida con la confirmación
        if (!contraseña.equals(confirmarContraseña)) {
            mostrarAlerta("Contraseña Inválida", "Las contraseñas no coinciden.");
            return; 
        }

        // Validar que el correo sea válido
        if (!ValidadorUsuario.esCorreoValido(correo)) {
            mostrarAlerta("Correo no válido", "Por favor, ingresa un correo electrónico válido.");
            return; 
        }

        // Crear el nuevo usuario y guardar en la base de datos
        Usuario nuevoUsuario = new Usuario(usuario, correo, contraseña);
        String resultado = usuarioService.guardarUsuario(nuevoUsuario);

        // Mostrar alerta solo si hay un error
        if (!resultado.equals("Registro exitoso.")) {
            mostrarAlerta("Error en registro", resultado);
        } else {
            // Guardamos el usuario en la sesión
            Session.getInstance().setUsuarioAutenticado(usuario);  // Guarda el usuario en la sesión
            cambiarAPantallaSeleccion();
        }

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
            mostrarAlerta("Error", "No se pudo cambiar a la pantalla de campeones: " + ex.getMessage());
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        // Muestra un cuadro de alerta con el mensaje especificado
        Alert alert = new Alert(AlertType.ERROR);
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
