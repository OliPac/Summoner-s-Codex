package com.summonerscodex.controller;

import com.summonerscodex.model.Objeto;
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

    // Método para establecer la información del objeto
    public void setObjetoInfo(Objeto objeto) {
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
        objectTransformationsImages.getChildren().clear(); // Limpia el contenedor de imágenes previas
        if (objeto.getInto() != null && !objeto.getInto().isEmpty()) {
            for (String intoId : objeto.getInto()) {
                String transformationImageUrl = "https://ddragon.leagueoflegends.com/cdn/14.21.1/img/item/" + intoId + ".png";
                ImageView transformationImageView = new ImageView(new Image(transformationImageUrl));
                transformationImageView.setFitHeight(120); // Tamaño de cada imagen
                transformationImageView.setFitWidth(120);
                objectTransformationsImages.getChildren().add(transformationImageView); // Añadir al contenedor
            }
        } else {
        	Label etiquetaSinTransformacion = new Label("No hay transformaciones disponibles");
        	etiquetaSinTransformacion.setStyle("-fx-font-size: 16; -fx-text-fill: #555;"); // Personaliza el estilo del texto
            objectTransformationsImages.getChildren().add(etiquetaSinTransformacion);
        }
        }
    }

