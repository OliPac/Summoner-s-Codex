package com.summonerscodex.controller.Mapa;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BaronController {

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView larvasImage,escurridizoImage,baronImage,heraldoImage;

    @FXML
    private Button btnRegresar;

    @FXML
    public void initialize() {    
        titleLabel.setText("Monstruos Neutrales: Vacío, Heraldo, Barón y Escurridizo");
    }

    @FXML
    private void RegresarMenu() {
        try {
            // Cargar el FXML de la ventana de selección
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Mapa/MapaGrieta.fxml"));
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
