package com.summonerscodex.controller;

import java.util.List;

import com.summonerscodex.model.Campeon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CampeonCompletoController {

    // Elementos de la interfaz gráfica
    @FXML
    private Label nombreCampeon, textoDescripcion, HPetiqueta, MPetiqueta, MSetiqueta, ARetiqueta, MRetiqueta, ADetiqueta, ASetiqueta;
    @FXML
    private ImageView habilidadQ, habilidadW, habilidadE, habilidadR, img_skins, splashArt;
    @FXML
    private Button btnAnterior, btnSiguiente, btnRegresar;

    private List<String> skins; // Lista de skins del campeón
    private int skinIndex = 0; // Índice de la skin actual

    /**
     * Método para establecer la información del campeón en la interfaz.
     * @param campeon El objeto Campeon que contiene la información.
     */
    public void setCampeonInfo(Campeon campeon) {
        // Configurar etiquetas con información del campeón
        nombreCampeon.setText(campeon.getId());
        setStatsLabels(campeon);

        // Cargar imágenes de habilidades
        cargarImagenConManejoDeErrores(habilidadQ, campeon.getHabilidadQUrl());
        cargarImagenConManejoDeErrores(habilidadW, campeon.getHabilidadWUrl());
        cargarImagenConManejoDeErrores(habilidadE, campeon.getHabilidadEUrl());
        cargarImagenConManejoDeErrores(habilidadR, campeon.getHabilidadRUrl());

        // Ajustar tamaños de las imágenes
        ajustarTamanoImagenes();

        // Cargar skins y splash art
        skins = campeon.getSkins();
        cargarImagenConManejoDeErrores(img_skins, skins.get(skinIndex));
        cargarImagenConManejoDeErrores(splashArt, campeon.getSplashArt());

        // Configurar eventos de botones para navegar entre skins
        btnAnterior.setOnAction(event -> mostrarSkinAnterior());
        btnSiguiente.setOnAction(event -> mostrarSkinSiguiente());
    }

    /**
     * Configura las etiquetas de estadísticas del campeón.
     * @param campeon El objeto Campeon que contiene la información.
     */
    private void setStatsLabels(Campeon campeon) {
        HPetiqueta.setText(String.valueOf(campeon.getStats().getHp()));
        MPetiqueta.setText(String.valueOf(campeon.getStats().getMp()));
        MSetiqueta.setText(String.valueOf(campeon.getStats().getMovespeed()));
        ARetiqueta.setText(String.valueOf(campeon.getStats().getArmor()));
        MRetiqueta.setText(String.valueOf(campeon.getStats().getSpellblock()));
        ADetiqueta.setText(String.valueOf(campeon.getStats().getAttackdamage()));
        ASetiqueta.setText(String.valueOf(campeon.getStats().getAttackspeed()));
        textoDescripcion.setText(campeon.getLore());
    }

    /**
     * Ajusta el tamaño de las imágenes de splash art y skins.
     */
    private void ajustarTamanoImagenes() {
        splashArt.setFitHeight(432);
        splashArt.setFitWidth(768);
        img_skins.setFitHeight(864);
        img_skins.setFitWidth(1536);
    }

    /**
     * Muestra la skin anterior del campeón.
     */
    @FXML
    private void mostrarSkinAnterior() {
        if (skins != null && !skins.isEmpty()) {
            skinIndex = (skinIndex - 1 + skins.size()) % skins.size(); // Calcular el nuevo índice
            cargarImagenConManejoDeErrores(img_skins, skins.get(skinIndex)); // Cargar la nueva imagen
        }
    }

    /**
     * Muestra la siguiente skin del campeón.
     */
    @FXML
    private void mostrarSkinSiguiente() {
        if (skins != null && !skins.isEmpty()) {
            skinIndex = (skinIndex + 1) % skins.size(); // Calcular el nuevo índice
            cargarImagenConManejoDeErrores(img_skins, skins.get(skinIndex)); // Cargar la nueva imagen
        }
    }

    /**
     * Carga una imagen en un ImageView manejando posibles errores.
     * @param imageView El ImageView donde se cargará la imagen.
     * @param url La URL de la imagen a cargar.
     */
    private void cargarImagenConManejoDeErrores(ImageView imageView, String url) {
        try {
            Image image = new Image(url); // Cargar la imagen desde la URL
            imageView.setImage(image); // Establecer la imagen en el ImageView
        } catch (IllegalArgumentException e) {
            System.err.println("Error al cargar la imagen desde la URL: " + url);
            e.printStackTrace(); // Imprimir el stack trace del error
        } catch (Exception e) {
            System.err.println("Ocurrió un error al cargar la imagen: " + e.getMessage());
            e.printStackTrace(); // Imprimir el mensaje de error
        }
    }
}
