package app;

import com.summonerscodex.services.icons.ChampionImageUrlExtractor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/com/summonerscodex/views/MainView.fxml"));
        //Cargar URL de iconos
        ChampionImageUrlExtractor.guardarUrlsCampeones();
        // Crear la escena y asignarla al escenario
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Summoner's Codex"); // Puedes establecer un título para la ventana
        stage.show(); // Mostrar el escenario
    }

    public static void main(String[] args) {
        launch(args); // Lanza la aplicación
    }
}