package com.summonerscodex.controller;

import com.summonerscodex.model.Campeon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class CampeonCompletoController {

    @FXML
    private Label nombreCampeon, textoDescripcion;
    @FXML
    private Label HPetiqueta, MPetiqueta, MSetiqueta, ARetiqueta, MRetiqueta, ADetiqueta, ASetiqueta;
    @FXML
    private ImageView habilidadQ, habilidadW, habilidadE, habilidadR, img_skins, splashArt;
    @FXML
    private Button btnAnterior, btnSiguiente;

    private List<String> skins; // Lista para almacenar las URLs de las skins
    private int skinIndex = 0; // Índice de la skin actual

    // Método para inicializar la información del campeón
    public void setCampeonInfo(Campeon campeon) {
        // Asignar valores a las etiquetas
        nombreCampeon.setText(campeon.getId()); 
        HPetiqueta.setText(String.valueOf(campeon.getStats().getHp()));
        MPetiqueta.setText(String.valueOf(campeon.getStats().getMp()));
        MSetiqueta.setText(String.valueOf(campeon.getStats().getMovespeed()));
        ARetiqueta.setText(String.valueOf(campeon.getStats().getArmor()));
        MRetiqueta.setText(String.valueOf(campeon.getStats().getSpellblock()));
        ADetiqueta.setText(String.valueOf(campeon.getStats().getAttackdamage()));
        ASetiqueta.setText(String.valueOf(campeon.getStats().getAttackspeed()));
        textoDescripcion.setText(campeon.getLore());

        // Cargar imágenes de habilidades
        cargarImagenConManejoDeErrores(habilidadQ, campeon.getHabilidadQUrl());
        cargarImagenConManejoDeErrores(habilidadW, campeon.getHabilidadWUrl());
        cargarImagenConManejoDeErrores(habilidadE, campeon.getHabilidadEUrl());
        cargarImagenConManejoDeErrores(habilidadR, campeon.getHabilidadRUrl());

        // Cargar imagen del campeón
        splashArt.setFitHeight(432);
        splashArt.setFitWidth(768);
        img_skins.setFitHeight(864);
        img_skins.setFitWidth(1536);
        skins = campeon.getSkins(); 
        cargarImagenConManejoDeErrores(img_skins, skins.get(skinIndex)); 
        cargarImagenConManejoDeErrores(splashArt, campeon.getSplashArt()); 

        // Configurar botones
        btnAnterior.setOnAction(event -> mostrarSkinAnterior());
        btnSiguiente.setOnAction(event -> mostrarSkinSiguiente());
    }

    @FXML
    private void mostrarSkinAnterior() {
        if (skins != null && !skins.isEmpty()) {
            skinIndex = (skinIndex - 1 + skins.size()) % skins.size(); // Mover hacia atrás, volviendo al final si es necesario
            cargarImagenConManejoDeErrores(img_skins, skins.get(skinIndex)); // Cargar la nueva skin
        }
    }

    @FXML
    private void mostrarSkinSiguiente() {
        if (skins != null && !skins.isEmpty()) {
            skinIndex = (skinIndex + 1) % skins.size(); // Mover hacia adelante, volviendo al inicio si es necesario
            cargarImagenConManejoDeErrores(img_skins, skins.get(skinIndex)); // Cargar la nueva skin
        }
    }

    private void cargarImagenConManejoDeErrores(ImageView imageView, String url) {
        try {
            Image image = new Image(url);
            imageView.setImage(image);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al cargar la imagen desde la URL: " + url);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
