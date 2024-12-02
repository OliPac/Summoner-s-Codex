package com.summonerscodex.controller.Objetos;

import com.summonerscodex.model.Objeto;
import com.summonerscodex.services.ObjetoServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PantallaObjetosController {

    @FXML
    private GridPane imageContainer;

    @FXML
    private Button btnRegresar;

    @FXML
    private TextField textFieldBuscar;

    private List<Objeto> todosLosObjetos;
    private final ObjetoServices objetoServices = new ObjetoServices(); // Declarar como final
    public PantallaObjetosController() { 
    }
    public void addImages(List<Objeto> objetos) {
        imageContainer.getChildren().clear();

        // Filtrar objetos duplicados
        List<Objeto> objetosUnicos = objetos.stream()
            .distinct() 
            .collect(Collectors.toList());

        int columnCount = 11;

        Platform.runLater(() -> {
            for (int index = 0; index < objetosUnicos.size(); index++) { 
                final int i = index; 
                Objeto objeto = objetosUnicos.get(i);
                VBox vbox = new VBox();
                vbox.setSpacing(5);

                ImageView imageView = new ImageView();
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                imageView.setPreserveRatio(true);
                imageView.setOnMouseClicked(event -> {
                    try {
                        manejarClickEnObjeto(objeto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                Label label = new Label(objeto.getName());
                label.setWrapText(true);
                label.setStyle("-fx-font-size: 14;");
                label.setMaxWidth(120);
                label.setTextFill(Color.WHITE);

                new Thread(() -> {
                    try {
                        Image image = new Image(objeto.getImageUrl());
                        Platform.runLater(() -> {
                            imageView.setImage(image);
                            vbox.getChildren().addAll(imageView, label);

                            int row = i / columnCount;
                            int column = i % columnCount;
                            imageContainer.add(vbox, column, row);
                        });
                    } catch (Exception e) {
                        System.err.println("Error al cargar imagen para: " + objeto.getName());
                    }
                }).start();
            }
        });
    }

    private void manejarClickEnObjeto(Objeto objeto) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Objetos/ObjetoCompleto.fxml"));
            Parent root = loader.load();

            ObjetoCompletoController controller = loader.getController();
            controller.setObjetoInfo(objeto);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(objeto.getName());
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al abrir la ventana del objeto: " + e.getMessage());
        }
    }

    public void cargarImagenes() {
        Task<List<Objeto>> cargarImagenesTask = new Task<>() {
            @Override
            protected List<Objeto> call() {
                return objetoServices.obtenerObjetosDesdeDataDragon();
            }

            @Override
            protected void succeeded() {
                todosLosObjetos = getValue();
                addImages(todosLosObjetos);
            }

            @Override
            protected void failed() {
                System.err.println("Error al cargar objetos: " + getException().getMessage());
            }
        };
        new Thread(cargarImagenesTask).start();
    }

    @FXML
    public void initialize() {
        cargarImagenes();
        textFieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> filtrarImagenesPorNombre(newValue));
    }

    private void filtrarImagenesPorNombre(String filtro) {
        List<Objeto> objetosFiltrados = todosLosObjetos.stream()
            .filter(objeto -> objeto.getName().toLowerCase().contains(filtro.toLowerCase()))
            .collect(Collectors.toList());
        addImages(objetosFiltrados);
    }

    @FXML
    private void RegresarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) imageContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }
}
