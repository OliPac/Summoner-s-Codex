package com.summonerscodex.controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Seleccion_de_pantallaController implements Initializable {
    @FXML
    private BorderPane root;
    @FXML
    private HBox carouselContainer;
    @FXML
    private Button leftButton, rightButton;
    @FXML
    private ScrollPane scrollPane;

    private static final int CARD_WIDTH = 450;
    private static final int CARD_HEIGHT = 650;
    private static final int CARD_SPACING = 300;
    private static final int TRANSITION_DURATION = 300;

    private static int currentIndex = 0; 
    private final List<StackPane> cards = new ArrayList<>();
    private boolean isNavigating = false;

    private final String[] fxmlPaths = {
        "com/summonerscodex/views/Conceptos/ConceptosBasicos.fxml",
        "com/summonerscodex/views/Campeones/Pantalla_de_campeones.fxml",
        "com/summonerscodex/views/Objetos/Pantalla_de_objetos.fxml",
        "com/summonerscodex/views/Video/Guia_video.fxml",
        "com/summonerscodex/views/Mapa/MapaGrieta.fxml",
        "com/summonerscodex/views/Builds/SeleccionBuildPantalla.fxml",
        "com/summonerscodex/views/Trivia/Trivia.fxml"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setContent(carouselContainer);
        scrollPane.setPannable(false);
        loadCardsFromLocalFiles();
        updateCardPositions();
        leftButton.setOnAction(event -> navigate(-1));
        rightButton.setOnAction(event -> navigate(1));
        Platform.runLater(this::adjustWindowSize);
    }

    private void loadCardsFromLocalFiles() {
        String[] titles = {
            "Conceptos básicos", "Campeones", "Objetos",
            "Videoguias", "Mapa de la Grieta", "Construcción de builds", "Trivia"
        };

        String[] imagePaths = {
            "Conceptos_basicos.jpg",
            "Campeones.jpg",
            "Objetos.jpg",
            "guia.jpg",
            "Mapa_de_la_grieta.jpg",
            "constructor.jpg",
            "trivia.jpg"
        };

        for (int i = 0; i < titles.length; i++) {
            StackPane card = createCard(titles[i], imagePaths[i], fxmlPaths[i]);
            cards.add(card);
            carouselContainer.getChildren().add(card);
        }
    }

    private StackPane createCard(String title, String imagePath, String fxmlPath) {
        StackPane card = new StackPane();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

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
            if (currentIndex == cards.indexOf(card)) {
                currentIndex = cards.indexOf(card); // Guardar el índice actual
                loadFXML(fxmlPath);
            }
        });

        return card;
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(newScene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el FXML desde: " + fxmlPath);
        }
    }

    private void navigate(int direction) {
        if (isNavigating) return;
        isNavigating = true;

        currentIndex = (currentIndex + direction + cards.size()) % cards.size();
        updateCardPositions();
        PauseTransition pause = new PauseTransition(Duration.millis(TRANSITION_DURATION));
        pause.setOnFinished(event -> isNavigating = false);
        pause.play();
    }

    private void updateCardPositions() {
        for (int i = 0; i < cards.size(); i++) {
            StackPane card = cards.get(i);
            int position = i - currentIndex;
            double translateX = position * CARD_SPACING;
            double scale = 1 - 0.1 * Math.abs(position);
            TranslateTransition transition = new TranslateTransition(Duration.millis(TRANSITION_DURATION), card);
            transition.setToX(translateX);
            transition.play();

            card.setScaleX(scale);
            card.setScaleY(scale);
            card.setOpacity(position == 0 ? 1.0 : 0.4);
            card.setCursor(position == 0 ? javafx.scene.Cursor.HAND : javafx.scene.Cursor.DEFAULT);
        }

        double hScrollPosition = (double) currentIndex / (cards.size() - 1);
        scrollPane.setHvalue(hScrollPosition);
    }

    private void adjustWindowSize() {
        if (root != null && root.getScene() != null) {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setWidth(screenBounds.getWidth());
                stage.setHeight(screenBounds.getHeight());
                stage.setX(0);
                stage.setY(0);
            }
        }
    }
}
