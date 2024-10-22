package com.summonerscodex.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summonerscodex.model.Campeon;
import com.summonerscodex.model.ChampionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CampeonesServices {

    private static final String DATA_DRAGON_URL = "https://ddragon.leagueoflegends.com/cdn/14.20.1/data/es_ES/championFull.json";

    // Método para obtener todos los campeones con su información desde Data Dragon
    public List<Campeon> obtenerCampeonesDesdeDataDragon() {
        List<Campeon> campeones = new ArrayList<>();

        try {
            // Crear conexión HTTP
            URL url = new URL(DATA_DRAGON_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Verificar la respuesta
            if (connection.getResponseCode() == 200) {
                // Leer los datos de la respuesta
                InputStream inputStream = connection.getInputStream();
                ObjectMapper objectMapper = new ObjectMapper();

                // Mapear la respuesta JSON a un objeto de ChampionResponse
                ChampionResponse response = objectMapper.readValue(inputStream, ChampionResponse.class);
                
                // Extraer información de cada campeón
                response.getData().forEach((key, campeon) -> {
                    campeones.add(campeon);
                });
            } else {
                System.out.println("Error al obtener los datos de Data Dragon. Código de respuesta: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
        }

        return campeones;
    }
}
