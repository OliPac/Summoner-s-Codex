package com.summonerscodex.controller;

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

    private List<Objeto> todosLosObjetos;  // Lista que contiene todos los objetos
    private ObjetoServices objetoServices = new ObjetoServices(); // Servicio para obtener objetos

    public void addImages(List<Objeto> objetos) {
        imageContainer.getChildren().clear();  // Limpiar el contenedor antes de agregar nuevas imágenes

        // Eliminar duplicados basándonos en el nombre del objeto
        List<Objeto> objetosUnicos = objetos.stream()
            .collect(Collectors.toMap(Objeto::getName, obj -> obj, (obj1, obj2) -> obj1))
            .values()
            .stream()
            .collect(Collectors.toList());

        int columnCount = 11; // Número de columnas en el GridPane
        int imageCount = objetosUnicos.size();

        // Se necesita realizar las operaciones de UI dentro de Platform.runLater
        for (int i = 0; i < imageCount; i++) {
            final int index = i; // Capturar el índice para usarlo dentro del nuevo hilo
            Objeto objeto = objetosUnicos.get(i);

            // Crear un VBox para contener la imagen y la etiqueta
            VBox vbox = new VBox();
            vbox.setSpacing(5); // Espacio entre la imagen y la etiqueta

            ImageView imageView = new ImageView();

            // Configuración del ImageView (carga de imagen, tamaño, etc.)
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(event -> manejarClickEnObjeto(objeto)); // Manejar clic

            // Crear la etiqueta con el nombre del objeto
            Label label = new Label(objeto.getName());
            label.setWrapText(true); // Permitir que el texto se ajuste
            label.setStyle("-fx-font-size: 14;"); // Cambiar tamaño de fuente si es necesario
            label.setMaxWidth(120); // Ajustar el ancho máximo de la etiqueta
            label.setTextFill(Color.WHITE);

            // Cargar la imagen en un hilo separado
            new Thread(() -> {
                Image image = new Image(objeto.getImageUrl()); // Cargar la imagen
                // Actualizar la UI en el hilo de la aplicación
                Platform.runLater(() -> {
                    imageView.setImage(image);

                    // Añadir la imagen y la etiqueta al VBox
                    vbox.getChildren().addAll(imageView, label);

                    int row = index / columnCount;
                    int column = index % columnCount;

                    // Agregar el VBox al GridPane
                    imageContainer.add(vbox, column, row);
                });
            }).start(); // Inicia el hilo para cargar la imagen
        }
    }

    private void manejarClickEnObjeto(Objeto objeto) {
        try {
            // Cargar el FXML de la ventana del objeto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/ObjetoCompleto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            ObjetoCompletoController controller = loader.getController();

            // Pasar el objeto al controlador
            controller.setObjetoInfo(objeto);

            // Crear una nueva escena y mostrarla
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // La ventana será modal
            stage.setTitle(objeto.getName());
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
            System.err.println("Error al abrir la ventana del objeto: " + e.getMessage());
        }
    }

    public void cargarImagenes() {
        Task<List<Objeto>> cargarImagenesTask = new Task<>() {
            @Override
            protected List<Objeto> call() throws Exception {
                // Cargar todos los objetos desde la base de datos
                return objetoServices.obtenerObjetosDesdeDataDragon(); // Cambiar a obtenerObjetosDesdeDataDragon
            }

            @Override
            protected void succeeded() {
                // Cuando se complete la carga de objetos, se actualiza la lista y se añaden a la vista
                try {
                    todosLosObjetos = get();
                    addImages(todosLosObjetos);
                } catch (Exception e) {
                    System.err.println("Error al obtener los objetos: " + e.getMessage());
                }
            }

            @Override
            protected void failed() {
                // Manejo de errores en caso de que la carga falle
                Throwable e = getException();
                System.err.println("Error al cargar objetos: " + e.getMessage());
            }
        };

        new Thread(cargarImagenesTask).start(); // Inicia el Task en un hilo separado
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

        cargarImagenes(); // Cargar las imágenes al iniciar

        // Agregar un listener al TextField para detectar cambios en el texto
        textFieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> filtrarImagenesPorNombre(newValue));
    }

    // Método para filtrar imágenes según el texto introducido en el TextField
    private void filtrarImagenesPorNombre(String filtro) {
        List<Objeto> objetosFiltrados = todosLosObjetos.stream()
            .filter(objeto -> objeto.getName().toLowerCase().contains(filtro.toLowerCase())) // Filtrar los objetos que coincidan con el nombre
            .collect(Collectors.toMap(Objeto::getName, obj -> obj, (obj1, obj2) -> obj1)) // Filtrar duplicados por 'name'
            .values()
            .stream()
            .collect(Collectors.toList());

        addImages(objetosFiltrados); // Actualizar el GridPane con las imágenes filtradas
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
