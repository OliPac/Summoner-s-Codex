package com.summonerscodex.controller.Mapa;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MapaController {

    @FXML
    private ImageView mapImage, iconBaron, iconoGromp, iconLobos, iconCangrejo, 
                      iconBlue, iconoPicuchillo, iconoRed, iconoKrug, 
                      iconCangrejo2, iconoDrake, iconoTorre;
   
    @FXML
    private Label monsterName, monsterDescription, monsterStats;
     
    @FXML
    private Button btnRegresar;

    @FXML
    private void initialize() {
        // Cargar la imagen del mapa principal
        loadImage(mapImage, "/com/summonerscodex/imagenes/mapa.jpg");
        
        // Cargar los íconos de los monstruos
        loadImage(iconBaron, "/com/summonerscodex/imagenes/iconoBaron.jpg");
        loadImage(iconoGromp, "/com/summonerscodex/imagenes/iconoGromp.jpg");
        loadImage(iconLobos, "/com/summonerscodex/imagenes/iconoLobos.jpg");
        loadImage(iconCangrejo, "/com/summonerscodex/imagenes/iconoCangrejo.jpg");
        loadImage(iconBlue, "/com/summonerscodex/imagenes/iconoBlue.jpg");
        loadImage(iconoPicuchillo, "/com/summonerscodex/imagenes/iconoPicuchillo.jpg");
        loadImage(iconoRed, "/com/summonerscodex/imagenes/iconoRed.jpg");
        loadImage(iconoKrug, "/com/summonerscodex/imagenes/iconoKrug.jpg");
        loadImage(iconCangrejo2, "/com/summonerscodex/imagenes/iconoCangrejo.jpg");
        loadImage(iconoDrake, "/com/summonerscodex/imagenes/iconoDrake.jpg");
        loadImage(iconoTorre, "/com/summonerscodex/imagenes/iconoTorre.jpg");
    }

    // Método para cargar una imagen en un ImageView
    private void loadImage(ImageView imageView, String path) {
        imageView.setImage(new Image(getClass().getResourceAsStream(path)));
    }

    // Método para cargar una nueva escena
    private void loadScene(MouseEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            // Obtener la referencia de la ventana actual desde el origen del evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la escena: " + e.getMessage());
        }
    }

    @FXML
    private void showMonsterInfo(MouseEvent event) {
        loadScene(event, "/com/summonerscodex/views/Mapa/MonstruosNeutrales.fxml");
    }

    @FXML
    private void showTorreInfo(MouseEvent event) {
        loadScene(event, "/com/summonerscodex/views/Mapa/Estructuras.fxml");
    }

    @FXML
    private void showDrakeInfo(MouseEvent event) {
        loadScene(event, "/com/summonerscodex/views/Mapa/Dragones.fxml");
    }

    @FXML
    private void showBaronInfo(MouseEvent event) {
        loadScene(event, "/com/summonerscodex/views/Mapa/Baron.fxml");
    }

    @FXML
    private void RegresarMenu() {
        try {
            // Cargar el FXML de la ventana de selección
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();

            // Obtener la referencia de la ventana actual usando btnRegresar
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }
}
