package com.summonerscodex.controller.Campeones;

import com.summonerscodex.model.Campeon;
import com.summonerscodex.services.CampeonesServices;
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

public class PantallaCampeonesController {

    @FXML
    private GridPane imageContainer;
    
    @FXML
    private Button btnRegresar;

    @FXML
    private TextField textFieldBuscar;  

    private List<Campeon> todosLosCampeones; 
    
    private CampeonesServices campeonImageServices = new CampeonesServices();

    public void addImages(List<Campeon> campeones) {
        imageContainer.getChildren().clear();  

        int columnCount = 11;
        int imageCount = campeones.size();

        for (int i = 0; i < imageCount; i++) {
            final int index = i; 
            Campeon campeon = campeones.get(i);
            
            VBox vbox = new VBox();
            // Espacio entre la imagen y la etiqueta
            vbox.setSpacing(5); 

            ImageView imageView = new ImageView();

            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(event -> manejarClickEnCampeon(campeon)); 

            // Cargar la imagen en un hilo separado
            new Thread(() -> {
                final Image[] image = new Image[1]; 
                try {
                    image[0] = new Image(campeon.getImageUrl());
                } catch (Exception e) {                
                    e.printStackTrace();
                    image[0] = new Image("path/to/default/image.png"); 
                }

                // Actualizar la UI en el hilo de la aplicación
                Platform.runLater(() -> {
                	// Usamos el valor contenido en el array
                    imageView.setImage(image[0]);                               
                    Label label = new Label(campeon.getName());
                    label.setWrapText(true); 
                    label.setStyle("-fx-font-size: 14;");
                    label.setTextFill(Color.WHITE);

                    // Añadir la imagen y la etiqueta al VBox
                    vbox.getChildren().addAll(imageView, label);

                    int row = index / columnCount;
                    int column = index % columnCount;

                    // Agregar el VBox al GridPane
                    imageContainer.add(vbox, column, row);
                });
            }).start(); 
        }
    }

    private void manejarClickEnCampeon(Campeon campeon) {
        try {
            // Cargar el FXML de la ventana del campeón
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Campeones/CampeonCompleto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            CampeonCompletoController controller = loader.getController();

            // Pasar el campeón al controlador
            controller.setCampeonInfo(campeon);        
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setTitle(campeon.getName());
            stage.setScene(new Scene(root));

            // Ajustar la ventana al tamaño de la pantalla
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.setMaximized(true);
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

        new Thread(cargarImagenesTask).start();  
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
            stage.setMaximized(true);
        });
        // Cargar las imágenes al iniciar
        cargarImagenes();  

        // Agregar un listener al TextField para detectar cambios en el texto
        textFieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> filtrarImagenesPorNombre(newValue));
    }

    // Método para filtrar imágenes según el texto introducido en el TextField
    private void filtrarImagenesPorNombre(String filtro) {
        List<Campeon> campeonesFiltrados = todosLosCampeones.stream()
            .filter(campeon -> campeon.getName().toLowerCase().contains(filtro.toLowerCase()))  
            .collect(Collectors.toList());

        addImages(campeonesFiltrados);
    }

    @FXML
    private void RegresarMenu() {
        try {
            // Cargar el FXML de la ventana de selección
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();

            // Obtener la referencia de la ventana actual
            Stage stage = (Stage) imageContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }
}
