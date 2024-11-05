package com.summonerscodex.controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
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
    private Button leftButton; 
    @FXML
    private Button rightButton; 
    @FXML
    private ScrollPane scrollPane; 

    // Constantes para dimensiones y espaciado de las tarjetas
    private static final int CARD_WIDTH = 450;
    private static final int CARD_HEIGHT = 650;
    private static final int CARD_SPACING = 300;
    private static final int TRANSITION_DURATION = 300;

    private int currentIndex = 0; 
    private final List<StackPane> cards = new ArrayList<>(); 
    private boolean isNavigating = false; 

    // Rutas de archivos FXML
    private final String[] fxmlPaths = {
        "src/main/resources/com/summonerscodex/views/ConceptosBasicos.fxml",
        "src/main/resources/com/summonerscodex/views/Pantalla_de_campeones.fxml",
        "src/main/resources/com/summonerscodex/views/Pantalla_de_objetos.fxml",
        "src/main/resources/com/summonerscodex/views/Guia_video.fxml",
        "src/main/resources/com/summonerscodex/views/MapaGrieta.fxml",
        "src/main/resources/com/summonerscodex/views/Construccion_de_builds.fxml",
        "src/main/resources/com/summonerscodex/views/Trivia.fxml"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar ScrollPane y desactivar arrastre manual
        scrollPane.setContent(carouselContainer);
        scrollPane.setPannable(false);
        loadCardsFromLocalFiles();
        updateCardPositions(); 
        leftButton.setOnAction(event -> navigate(-1));
        rightButton.setOnAction(event -> navigate(1));

        // Ajustar el tamaño de la ventana una vez que todos los componentes estén cargados
        Platform.runLater(this::adjustWindowSize);
    }

    private void loadCardsFromLocalFiles() {
        String[] titles = {
            "Conceptos básicos", "Campeones", "Objetos", 
            "Builds", "Mapa de la Grieta", "Estadísticas", "Mi perfil"
        };

        String[] imagePaths = {
            "src/main/resources/com/summonerscodex/imagenes/Conceptos_basicos.jpg",
            "src/main/resources/com/summonerscodex/imagenes/Campeones.jpg",
            "src/main/resources/com/summonerscodex/imagenes/Objetos.jpg",
            "src/main/resources/com/summonerscodex/imagenes/Builds.jpg",
            "src/main/resources/com/summonerscodex/imagenes/Mapa_de_la_grieta.jpg",
            "src/main/resources/com/summonerscodex/imagenes/Estadisticas.jpg",
            "src/main/resources/com/summonerscodex/imagenes/mi_perfil.jpg"
        };

        // Crear y agregar cada tarjeta al contenedor del carrusel
        for (int i = 0; i < titles.length; i++) {
            StackPane card = createCard(titles[i], imagePaths[i], fxmlPaths[i]);
            cards.add(card);
            carouselContainer.getChildren().add(card);
        }
    }

    private StackPane createCard(String title, String imagePath, String fxmlPath) {
        StackPane card = new StackPane();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        // Establecer la imagen de fondo de la tarjeta
        Rectangle background = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(new ImagePattern(new Image(new File(imagePath).toURI().toString())));

        VBox vbox = new VBox();
        vbox.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        vbox.setStyle("-fx-padding: 10;");

        // Crear y estilizar el título de la tarjeta
        Label label = new Label(title);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 24px;");

        vbox.getChildren().add(label);
        vbox.setTranslateY(10);

        // Agregar el fondo y el título a la tarjeta
        card.getChildren().addAll(background, vbox);

        // Agregar evento de clic del mouse para cargar el FXML correspondiente
        card.setOnMouseClicked(event -> {
            if (currentIndex == cards.indexOf(card)) {
                loadFXML(fxmlPath);
            }
        });

        return card; 
    }

    private void loadFXML(String fxmlPath) {
        try {
            // Cargar la nueva escena desde FXML
            FXMLLoader loader = new FXMLLoader(new File(fxmlPath).toURI().toURL());
            Stage stage = (Stage) root.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el FXML desde: " + fxmlPath);
        }
    }

    private void navigate(int direction) {
        if (isNavigating) return; 
        isNavigating = true;

        // Actualizar el índice actual según la dirección
        currentIndex = (currentIndex + direction + cards.size()) % cards.size();
        updateCardPositions(); 
        // Pausa la transición para permitir una navegación suave
        PauseTransition pause = new PauseTransition(Duration.millis(TRANSITION_DURATION));
        pause.setOnFinished(event -> isNavigating = false);
        pause.play();
    }

    private void updateCardPositions() {
        for (int i = 0; i < cards.size(); i++) {
            StackPane card = cards.get(i);
            int position = i - currentIndex;
            // Calcular la traducción horizontal
            double translateX = position * CARD_SPACING; 
            // Escalar las tarjetas según la posición
            double scale = 1 - 0.1 * Math.abs(position); 
            TranslateTransition transition = new TranslateTransition(Duration.millis(TRANSITION_DURATION), card);
            transition.setToX(translateX);
            transition.play();

            // Establecer la escala y opacidad según la posición
            card.setScaleX(scale);
            card.setScaleY(scale);
            card.setOpacity(position == 0 ? 1.0 : 0.4);
            card.setCursor(position == 0 ? javafx.scene.Cursor.HAND : javafx.scene.Cursor.DEFAULT);
        }

        // Ajustar la posición de desplazamiento horizontal del ScrollPane
        double hScrollPosition = (double) currentIndex / (cards.size() - 1);
        scrollPane.setHvalue(hScrollPosition);
    }

    private void adjustWindowSize() {
        // Asegurarse de que root no sea nulo antes de acceder a la escena
        if (root != null && root.getScene() != null) {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setWidth(screenBounds.getWidth());
                stage.setHeight(screenBounds.getHeight());
                stage.setX(0);
                stage.setY(0);
            } else {
                System.err.println("El escenario es nulo.");
            }
        } else {
            System.err.println("El root o la escena son nulos.");
        }
    }
}
