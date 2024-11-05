package com.summonerscodex.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DragonesController {

    @FXML
    private Label titleLabel;
    
    // Imágenes de los dragones
    @FXML
    private ImageView dragonFuegoImage,ScuttleCrab,dragonAguaImage,dragonMontanaImage,dragonNubeImage,dragonHextechImage,dragonTecnoquimicoImage,dragonAncianoImage;
    @FXML
    private Button btnRegresar;
    @FXML
    public void initialize() {
        // Configuración adicional, si es necesaria
        titleLabel.setText("Dragones en League of Legends");
     
    }
    @FXML
    private void RegresarMenu() {
        try {
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/MapaGrieta.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true); 
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }
}
