package com.summonerscodex.controller.Conceptos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ConceptosBasicosController {

    @FXML 
    private ImageView ImagenBase, ImagenTorre, imagenBaron, ImagenRol, ImagenCampeon;
    @FXML
    private ImageView NexoAzul, NexoRojo, Torreta, Inhibidor, Baron, Dragon, Top, Jg, Mid, Bot, Supp, Habilidad, Hechizo, Objeto;
    @FXML
    private BorderPane root;
    @FXML
    private Button btnRegresar;
    
    public void initialize() {
        
    }

    /**
     * Cambia la imagen de acuerdo al ID del ImageView clicado.
     * @param event El evento de clic del mouse.
     * @param imageViewTarget El ImageView que se va a actualizar.
     * @param imagePathBase La ruta base de la imagen.
     */
    private void cambiarImagen(MouseEvent event, ImageView imageViewTarget, String imagePathBase) {
        ImageView clickedImage = (ImageView) event.getSource();
        URL imageUrl = null;

        // Determinar la imagen a cargar basándose en el ID del ImageView clicado
        switch (clickedImage.getId()) {
            case "NexoAzul":
                imageUrl = getClass().getResource(imagePathBase + "imagenBaseAzul.png");
                break;
            case "NexoRojo":
                imageUrl = getClass().getResource(imagePathBase + "imagenBaseRoja.png");
                break;
            case "Torreta":
                imageUrl = getClass().getResource(imagePathBase + "imagenTorre.png");
                break;
            case "Inhibidor":
                imageUrl = getClass().getResource(imagePathBase + "imagenInhibidor.png");
                break;
            case "Baron":
                imageUrl = getClass().getResource(imagePathBase + "mapaBaron.png");
                break;
            case "Dragon":
                imageUrl = getClass().getResource(imagePathBase + "mapaDragon.png");
                break;
            case "Top":
                imageUrl = getClass().getResource(imagePathBase + "mapaTop.png");
                break;
            case "Jg":
                imageUrl = getClass().getResource(imagePathBase + "mapaJungla.png");
                break;
            case "Mid":
                imageUrl = getClass().getResource(imagePathBase + "mapaMid.png");
                break;
            case "Bot":
                imageUrl = getClass().getResource(imagePathBase + "mapaBot.png");
                break;
            case "Supp":
                imageUrl = getClass().getResource(imagePathBase + "mapaSupp.png");
                break;
            case "Habilidad":
                imageUrl = getClass().getResource(imagePathBase + "imagenHabilidades.png");
                break;
            case "Hechizo":
                imageUrl = getClass().getResource(imagePathBase + "imagenHechizos.png");
                break;
            case "Objeto":
                imageUrl = getClass().getResource(imagePathBase + "imagenObjetos.png");
                break;
            default:
                System.out.println("ID desconocido: " + clickedImage.getId());
                break;
        }

        // Cargar la imagen si la URL no es nula
        if (imageUrl != null) {
            imageViewTarget.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.out.println("Imagen no encontrada para ID: " + clickedImage.getId());
        }
    }

    @FXML
    private void CambiarFotoBase(MouseEvent event) {
        cambiarImagen(event, ImagenBase, "/com/summonerscodex/imagenes/");
    }

    @FXML
    private void CambiarFotoTorre(MouseEvent event) {
        cambiarImagen(event, ImagenTorre, "/com/summonerscodex/imagenes/");
    }

    @FXML
    private void CambiarFotoBaron(MouseEvent event) {
        cambiarImagen(event, imagenBaron, "/com/summonerscodex/imagenes/");
    }

    @FXML
    private void CambiarFotoRol(MouseEvent event) {
        cambiarImagen(event, ImagenRol, "/com/summonerscodex/imagenes/");
    }

    @FXML
    private void CambiarFotoLux(MouseEvent event) {
        cambiarImagen(event, ImagenCampeon, "/com/summonerscodex/imagenes/");
    }

    @FXML
    private void RegresarMenu() {
        try {
            // Cargar el FXML de la ventana de selección
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();

            // Obtener la referencia de la ventana actual usando btnRegresar
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
