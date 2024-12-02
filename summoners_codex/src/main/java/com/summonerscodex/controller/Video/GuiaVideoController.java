package com.summonerscodex.controller.Video;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.summonerscodex.services.CampeonesServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GuiaVideoController {

    @FXML
    private WebView webViewVideo;

    @FXML
    private GridPane gridPaneVideos;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton, btnRegresar;

    private static final String API_KEY = "AIzaSyAiYyAD6HTAIHPVj9lxm_05rb5BIANpgGw";
    private CampeonesServices campeonesServices = new CampeonesServices();

    // Video predeterminado 
    private static final String VIDEO_PRED = "c1gbuKFFL-4"; 

    // Método para cargar el video en el WebView
    private void cargarVideo(String videoId) {
        String videoUrl = "https://www.youtube.com/embed/" + videoId + "?start=3"; 
        WebEngine webEngine = webViewVideo.getEngine();
        webEngine.load(videoUrl);
    }

    // Método para manejar la búsqueda
    @FXML
    private void buscarGuia() {
        String campeon = searchField.getText().trim();
        if (campeon.isEmpty()) return;

        // Validar que el nombre del campeón sea válido
        if (campeonesServices.obtenerCampeonesDesdeDataDragon().stream().noneMatch(c -> c.getName().equalsIgnoreCase(campeon))) {
            System.out.println("Campeón no válido.");
            return;
        }

        // Llamada a la API de YouTube para buscar videos
        buscarVideosCampeon(campeon);
    }

    // Método para buscar videos en la API de YouTube
    private void buscarVideosCampeon(String campeon) {
        try {
            String query = URLEncoder.encode(campeon + " League of Legends guide", StandardCharsets.UTF_8);
            String urlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + query + "&type=video&key=" + API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject response = new Gson().fromJson(reader, JsonObject.class);
                JsonArray items = response.getAsJsonArray("items");
                Platform.runLater(() -> mostrarVideos(items));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar los videos en el GridPane
    private void mostrarVideos(JsonArray items) {
        gridPaneVideos.getChildren().clear(); 

        int maxResults = 6;  // Limitar a los primeros 6 resultados
        for (int i = 0; i < Math.min(items.size(), maxResults); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            String videoId = item.getAsJsonObject("id").get("videoId").getAsString();
            JsonObject snippet = item.getAsJsonObject("snippet");
            String title = snippet.get("title").getAsString();
            String thumbnailUrl = snippet.getAsJsonObject("thumbnails").getAsJsonObject("high").get("url").getAsString();

            // Crear una miniatura con el título del video
            Button videoButton = new Button(title);
            videoButton.setGraphic(new ImageView(new Image(thumbnailUrl)));
            videoButton.setOnAction(e -> cargarVideo(videoId));

            gridPaneVideos.add(videoButton, i % 3, i / 3); 
        }
    }

    // Método para regresar al menú
    @FXML
    private void RegresarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gridPaneVideos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }

    // Método para cargar el video predeterminado al iniciar
    @FXML
    private void initialize() {
        cargarVideo(VIDEO_PRED); 
    }
}
