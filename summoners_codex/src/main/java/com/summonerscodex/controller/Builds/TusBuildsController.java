package com.summonerscodex.controller.Builds;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.summonerscodex.services.MongoDBConexion;
import com.summonerscodex.utils.Session;

import java.io.IOException;
import java.util.List;

public class TusBuildsController {

    @FXML
    private VBox buildsContainer;

    @FXML
    private Button btnRegresar;

    @FXML
    private void initialize() {
        cargarBuilds();
    }
    // Método para cargar las builds del usuario autenticado
    public void cargarBuilds() {
    	//Obtener el usuario autenticado desde la sesión
        String usuarioAutenticado = Session.getInstance().getUsuarioAutenticado();
        System.out.println("Usuario autenticado: " + usuarioAutenticado);

        if (usuarioAutenticado == null || usuarioAutenticado.isEmpty()) {
            System.out.println("No hay usuario autenticado.");
            return;
        }

        MongoDBConexion.conectar();
        //Iniciar el hilo para cargar las builds
        new Thread(() -> cargarBuildsEnHilo(usuarioAutenticado)).start();
    }

    private void cargarBuildsEnHilo(String usuarioAutenticado) {
        MongoCollection<Document> usuariosCollection = MongoDBConexion.getDatabase().getCollection("usuarios");

        try {
            Document usuario = usuariosCollection.find(Filters.eq("usuario", usuarioAutenticado)).first();
            Platform.runLater(() -> procesarBuilds(usuario));
        } catch (MongoException e) {
            Platform.runLater(() -> System.out.println("Error al cargar las builds: " + e.getMessage()));
        } finally {
            MongoDBConexion.cerrarConexion();
        }
    }

    private void procesarBuilds(Document usuario) {
        if (usuario != null) {
            Object buildsObject = usuario.get("builds");

            if (buildsObject instanceof List<?>) {
                List<Document> builds = (List<Document>) buildsObject;

                buildsContainer.getChildren().clear();

                for (Document buildDoc : builds) {
                    HBox buildContainer = crearContenedorBuild(buildDoc);
                    buildsContainer.getChildren().add(buildContainer);
                }
            } else {
                System.out.println("El campo 'builds' no es una lista válida.");
            }
        } else {
            System.out.println("No se encontró el usuario o no tiene builds guardadas.");
        }
    }

    private HBox crearContenedorBuild(Document buildDoc) {
        String campeonNombre = buildDoc.getString("campeonNombre");
        String campeonImagenUrl = buildDoc.getString("campeonImagenUrl");
        List<String> objetosNombres = (List<String>) buildDoc.get("objetosNombres");
        List<String> objetosImagenesUrls = (List<String>) buildDoc.get("objetosImagenesUrls");

        HBox buildContainer = new HBox(10);
        buildContainer.setStyle("-fx-background-color: #2f2f2f; -fx-padding: 10; -fx-border-radius: 10;");

        ImageView campeonImageView = crearImagenView(campeonImagenUrl, 100, 100);
        Label campeonLabel = crearLabel(campeonNombre, 18);

        HBox objetosHBox = new HBox(10);
        if (objetosNombres != null && objetosImagenesUrls != null && objetosNombres.size() == objetosImagenesUrls.size()) {
            for (int i = 0; i < objetosNombres.size(); i++) {
                String objetoNombre = objetosNombres.get(i);
                String objetoImagenUrl = objetosImagenesUrls.get(i);

                if (objetoNombre != null && objetoImagenUrl != null) {
                    ImageView objetoImageView = crearImagenView(objetoImagenUrl, 50, 50);
                    Label objetoLabel = crearLabel(objetoNombre, 14);
                    objetosHBox.getChildren().addAll(objetoImageView, objetoLabel);
                }
            }
        }

        Button btnEliminar = new Button("Eliminar build");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5 10; -fx-border-radius: 5;");
        btnEliminar.setOnAction(e -> eliminarBuild(buildDoc, buildContainer));

        buildContainer.getChildren().addAll(campeonImageView, campeonLabel, objetosHBox, btnEliminar);
        return buildContainer;
    }

    private void eliminarBuild(Document buildDoc, HBox buildContainer) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar esta build?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                eliminarBuildDeBaseDeDatos(buildDoc, buildContainer);
            } else {
                System.out.println("Eliminación cancelada por el usuario.");
            }
        });
    }

    private void eliminarBuildDeBaseDeDatos(Document buildDoc, HBox buildContainer) {
        String usuarioAutenticado = Session.getInstance().getUsuarioAutenticado();

        if (usuarioAutenticado == null || usuarioAutenticado.isEmpty()) {
            System.out.println("No hay usuario autenticado.");
            return;
        }

        MongoDBConexion.conectar();
        MongoCollection<Document> usuariosCollection = MongoDBConexion.getDatabase().getCollection("usuarios");

        try {
            usuariosCollection.updateOne(
                Filters.eq("usuario", usuarioAutenticado),
                new Document("$pull", new Document("builds", buildDoc))
            );

            Platform.runLater(() -> buildsContainer.getChildren().remove(buildContainer));
            System.out.println("Build eliminada correctamente.");
        } catch (MongoException e) {
            System.out.println("Error al eliminar la build: " + e.getMessage());
        } finally {
            MongoDBConexion.cerrarConexion();
        }
    }

    private ImageView crearImagenView(String url, int width, int height) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        if (url != null && !url.isEmpty()) {
            Task<Image> task = new Task<>() {
                @Override
                protected Image call() {
                    return new Image(url, true); // 'true' activa la descarga en segundo plano
                }
            };

            task.setOnSucceeded(event -> imageView.setImage(task.getValue()));
            task.setOnFailed(event -> System.err.println("Error al cargar imagen: " + task.getException()));

            new Thread(task).start();
        } else {
            imageView.setImage(new Image("default_image_url"));
        }

        return imageView;
    }


    private Label crearLabel(String text, int fontSize) {
        Label label = new Label(text != null ? text : "Desconocido");
        label.setStyle("-fx-font-size: " + fontSize + "px; -fx-text-fill: white; -fx-padding: 5;");
        return label;
    }

    @FXML
    private void RegresarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Builds/SeleccionBuildPantalla.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }
}
