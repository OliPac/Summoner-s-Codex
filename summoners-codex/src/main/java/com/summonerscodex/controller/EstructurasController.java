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

public class EstructurasController {

    @FXML
    private Label titleLabel;
    
    @FXML
    private ImageView shopImage,baseImage,topLaneTier1Image,topLaneTier2Image,midLaneTier1Image,midLaneTier2Image,botLaneTier1Image,botLaneTier2Image,inhibitorTopImage,inhibitorMidImage,inhibitorBotImage,nexusImage;         
  
    private Button btnRegresar;
    @FXML
    private void initialize() {
        titleLabel.setText("Estructuras en League of Legends");
    }
    @FXML
    private void RegresarMenu() {
        try {
            // Cargar el FXML de la ventana de selección
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/MapaGrieta.fxml"));
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
