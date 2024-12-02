package com.summonerscodex.controller.Objetos;

import com.summonerscodex.model.Objeto;
import com.summonerscodex.services.RiotAPIService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.jsoup.Jsoup;

public class ObjetoCompletoController {

    @FXML
    private ImageView objectImage; 

    @FXML
    private Label objectName,objectDescription,objectPlainText,objectGoldBase,objectGoldSell,objectStats; 

    @FXML
    private HBox objectTransformationsImages;
    
    private String VERSION;
   
    

    // Método para establecer la información del objeto
    public void setObjetoInfo(Objeto objeto) throws Exception {
    	VERSION = RiotAPIService.obtenerUltimaVersion();
        objectName.setText(objeto.getName());

        // Convierte la descripción HTML a texto plano
        String plainDescription = Jsoup.parse(objeto.getDescription()).text();
        objectDescription.setText(plainDescription);
        objectPlainText.setText(objeto.getPlaintext());

        // Manejo del costo en oro
        objectGoldBase.setText(String.valueOf(objeto.getGold().getBase()));
        objectGoldSell.setText(String.valueOf(objeto.getGold().getSell()));

        // Manejo de estadísticas
        if (objeto.getStats() != null && !objeto.getStats().isEmpty()) {
            StringBuilder statsBuilder = new StringBuilder();
            objeto.getStats().forEach((key, value) -> {
                statsBuilder.append(key).append(": ").append(value).append("\n");
            });
            objectStats.setText(statsBuilder.toString().trim());
        } else {
            objectStats.setText("No hay estadísticas disponibles");
        }

        // Cargar la imagen principal del objeto
        Image mainImage = new Image(objeto.getImageUrl());
        objectImage.setImage(mainImage);

        // Cargar imágenes de transformaciones
        objectTransformationsImages.getChildren().clear();
        if (objeto.getInto() != null && !objeto.getInto().isEmpty()) {
            for (String intoId : objeto.getInto()) {
                String transformationImageUrl = "https://ddragon.leagueoflegends.com/cdn/"+ VERSION +"/img/item/" + intoId + ".png";
                ImageView transformationImageView = new ImageView(new Image(transformationImageUrl));
                transformationImageView.setFitHeight(120); 
                transformationImageView.setFitWidth(120);
                objectTransformationsImages.getChildren().add(transformationImageView); 
            }
        } else {
        	Label etiquetaSinTransformacion = new Label("No hay transformaciones disponibles");
        	etiquetaSinTransformacion.setStyle("-fx-font-size: 16; -fx-text-fill: #555;"); 
            objectTransformationsImages.getChildren().add(etiquetaSinTransformacion);
        }
        }
    }

