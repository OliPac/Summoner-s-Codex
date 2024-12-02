package com.summonerscodex.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summonerscodex.model.Campeon;
import com.summonerscodex.model.CampeonResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CampeonesServices {

    private final String version;
    private final String DATA_DRAGON_URL;

    public CampeonesServices() {
        try {
            // Obtener la última versión desde la clase RiotAPIService
            this.version = RiotAPIService.obtenerUltimaVersion();
            this.DATA_DRAGON_URL = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/es_ES/championFull.json";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar CampeonesServices", e);
        }
    }

    // Método para obtener todos los campeones con su información desde Data Dragon
    public List<Campeon> obtenerCampeonesDesdeDataDragon() {
        List<Campeon> campeones = new ArrayList<>();
        try {
            URL url = new URL(DATA_DRAGON_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {            
                try (InputStream inputStream = connection.getInputStream()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    // Mapear la respuesta JSON a un objeto de ChampionResponse
                    CampeonResponse response = objectMapper.readValue(inputStream, CampeonResponse.class);
                    // Extraer información de cada campeón
                    response.getData().forEach((key, campeon) -> campeones.add(campeon));             }
            } else {
                System.err.println("Error al obtener los datos de Data Dragon. Código de respuesta: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al procesar la solicitud: " + e.getMessage());
        }
        return campeones;
    }
}
