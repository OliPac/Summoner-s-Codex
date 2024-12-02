package com.summonerscodex.controller.Builds;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;  // Usamos HBox para alineación horizontal
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SeleccionBuildController implements Initializable {

    @FXML
    private HBox carouselContainer;  
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnRegresar;

    // Constantes para el tamaño de las tarjetas y la separación
    private static final int CARD_WIDTH = 450;
    private static final int CARD_HEIGHT = 650;
    private static final int CARD_SPACING = 100;

    // Rutas de los archivos FXML para las dos tarjetas
    private final String[] fxmlPaths = {
        "com/summonerscodex/views/Builds/TusBuilds.fxml",
        "com/summonerscodex/views/Builds/ConstruccionBuilds.fxml"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carouselContainer.setSpacing(CARD_SPACING); 
        loadCardsFromLocalFiles();
    }

    private void loadCardsFromLocalFiles() {
        String[] titles = {
            "Tus Builds", "Crear Builds"
        };

        String[] imagePaths = {
            "Conceptos_basicos.jpg",
            "constructor.jpg"
        };

        // Crear y agregar cada tarjeta al contenedor de la interfaz
        for (int i = 0; i < titles.length; i++) {
            StackPane card = createCard(titles[i], imagePaths[i], fxmlPaths[i]);
            carouselContainer.getChildren().add(card); 
        }
    }

    private StackPane createCard(String title, String imagePath, String fxmlPath) {
        StackPane card = new StackPane();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        // Cargar la imagen desde los recursos
        Image image = new Image(getClass().getResourceAsStream("/com/summonerscodex/imagenes/" + imagePath));
        Rectangle background = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(new ImagePattern(image));       
        VBox vbox = new VBox();
        vbox.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        vbox.setStyle("-fx-padding: 10;");
        Label label = new Label(title);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 24px;");
        vbox.getChildren().add(label);
        vbox.setTranslateY(10);
        card.getChildren().addAll(background, vbox);
        card.setOnMouseClicked(event -> {
            loadFXML(fxmlPath);
        });

        return card;
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot, 1536, 864);
            Stage stage = (Stage) carouselContainer.getScene().getWindow();
            stage.setScene(newScene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el FXML desde: " + fxmlPath);
        }
    }


    @FXML
    private void RegresarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();
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
