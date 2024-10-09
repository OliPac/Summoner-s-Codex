package com.summonerscodex.controller;

import com.summonerscodex.services.icons.CampeonesImageServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.List;
import java.util.stream.Collectors;

public class PantallaCampeonesController {

    @FXML
    private GridPane imageContainer;

    @FXML
    private TextField textFieldBuscar;  // Asegúrate de que el TextField esté vinculado

    private List<String> todasLasImagenes;  // Lista que contiene todas las URLs de las imágenes
    private CampeonesImageServices campeonImageServices = new CampeonesImageServices();

    public void addImages(List<String> imageUrls) {
        imageContainer.getChildren().clear();

        int columnCount = 11; // Número de columnas deseadas
        int imageCount = imageUrls.size();

        int rowCount = (int) Math.ceil((double) imageCount / columnCount);

        for (int i = 0; i < imageCount; i++) {
            ImageView imageView = new ImageView();
            Image image = new Image(imageUrls.get(i));
            imageView.setImage(image);
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);

            int row = i / columnCount;
            int column = i % columnCount;

            imageContainer.add(imageView, column, row);
        }
    }

    public void cargarImagenes() {
        todasLasImagenes = campeonImageServices.obtenerUrlsDesdeMongoDB();  // Cargar todas las imágenes una sola vez
        addImages(todasLasImagenes);  // Añadir todas las imágenes inicialmente
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) imageContainer.getScene().getWindow();
            // Ajustar la ventana a la pantalla
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
        });

        cargarImagenes();  // Cargar las imágenes al iniciar

        // Agregar un listener al TextField para detectar cambios en el texto
        textFieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> filtrarImagenesPorNombre(newValue));
    }

    // Método para filtrar imágenes según el texto introducido en el TextField
    private void filtrarImagenesPorNombre(String filtro) {
        List<String> imagenesFiltradas = todasLasImagenes.stream()
            .filter(url -> url.toLowerCase().contains(filtro.toLowerCase()))  // Filtrar las imágenes que coincidan con el nombre del campeón
            .collect(Collectors.toList());

        addImages(imagenesFiltradas);  // Actualizar el GridPane con las imágenes filtradas
    }
}
