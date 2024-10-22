package com.summonerscodex.controller;

import com.summonerscodex.model.Campeon;
import com.summonerscodex.services.CampeonesServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class PantallaCampeonesController {

    @FXML
    private GridPane imageContainer;

    @FXML
    private TextField textFieldBuscar;  

    private List<Campeon> todosLosCampeones;  // Lista que contiene todos los campeones
    private CampeonesServices campeonImageServices = new CampeonesServices();

    public void addImages(List<Campeon> campeones) {
        imageContainer.getChildren().clear();  // Limpiar el contenedor antes de agregar nuevas imágenes

        int columnCount = 11;
        int imageCount = campeones.size();

        // Se necesita realizar las operaciones de UI dentro de Platform.runLater
        for (int i = 0; i < imageCount; i++) {
            final int index = i;  // Necesitamos capturar el índice para usarlo dentro del nuevo hilo
            Campeon campeon = campeones.get(i);
            ImageView imageView = new ImageView();

            // Configuración del ImageView (carga de imagen, tamaño, etc.)
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(event -> manejarClickEnCampeon(campeon)); // Manejar clic

            // Cargar la imagen en un hilo separado
            new Thread(() -> {
                Image image = new Image(campeon.getImageUrl()); // Cargar la imagen
                // Actualizar la UI en el hilo de la aplicación
                Platform.runLater(() -> {
                    imageView.setImage(image);
                    int row = index / columnCount;
                    int column = index % columnCount;

                    // Agregar el ImageView al GridPane
                    imageContainer.add(imageView, column, row);
                });
            }).start(); // Inicia el hilo para cargar la imagen
        }
    }

    private void manejarClickEnCampeon(Campeon campeon) {
        try {
            // Cargar el FXML de la ventana del campeón
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/CampeonCompleto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            CampeonCompletoController controller = loader.getController();

            // Pasar el campeón al controlador
            controller.setCampeonInfo(campeon);

            // Crear una nueva escena y mostrarla
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // La ventana será modal
            stage.setTitle(campeon.getName());
            stage.setScene(new Scene(root));

            // Ajustar la ventana al tamaño de la pantalla
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            // Mostrar la ventana
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al abrir la ventana del campeón: " + e.getMessage());
        }
    }


    public void cargarImagenes() {
        Task<List<Campeon>> cargarImagenesTask = new Task<>() {
            @Override
            protected List<Campeon> call() throws Exception {
                // Cargar todos los campeones desde la base de datos
                return campeonImageServices.obtenerCampeonesDesdeDataDragon();
            }

            @Override
            protected void succeeded() {
                // Cuando se complete la carga de campeones, se actualiza la lista y se añaden a la vista
                try {
                    todosLosCampeones = get();
                    addImages(todosLosCampeones);
                } catch (Exception e) {
                    System.err.println("Error al obtener los campeones: " + e.getMessage());
                }
            }

            @Override
            protected void failed() {
                // Manejo de errores en caso de que la carga falle
                Throwable e = getException();
                System.err.println("Error al cargar campeones: " + e.getMessage());
            }
        };

        new Thread(cargarImagenesTask).start();  // Inicia el Task en un hilo separado
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
        List<Campeon> campeonesFiltrados = todosLosCampeones.stream()
            .filter(campeon -> campeon.getName().toLowerCase().contains(filtro.toLowerCase()))  // Filtrar los campeones que coincidan con el nombre
            .collect(Collectors.toList());

        addImages(campeonesFiltrados);  // Actualizar el GridPane con las imágenes filtradas
    }
}
