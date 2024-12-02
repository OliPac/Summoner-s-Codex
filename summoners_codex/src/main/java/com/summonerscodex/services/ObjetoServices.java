package com.summonerscodex.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summonerscodex.model.Objeto;  // Asegúrate de que esta clase exista
import com.summonerscodex.model.ObjetoResponse;  // Crear esta clase para mapear la respuesta

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjetoServices {
    private String version;
    private static final String DATA_DRAGON_URL_TEMPLATE = "https://ddragon.leagueoflegends.com/cdn/%s/data/es_ES/item.json";
    private Map<String, Objeto> objetosCache = new HashMap<>(); 

    public ObjetoServices() {
        try {
            this.version = RiotAPIService.obtenerUltimaVersion();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar la versión de RiotAPIService", e);
        }
    }


    // Método para obtener todos los objetos con su información desde Data Dragon
    public List<Objeto> obtenerObjetosDesdeDataDragon() {
        List<Objeto> objetos = new ArrayList<>();
        String dataDragonUrl = String.format(DATA_DRAGON_URL_TEMPLATE, version);
        try {     
            URL url = new URL(dataDragonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {    
                try (InputStream inputStream = connection.getInputStream()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    // Mapear la respuesta JSON a un objeto de ObjetoResponse
                    ObjetoResponse response = objectMapper.readValue(inputStream, ObjetoResponse.class);
                    // Extraer información de cada objeto y almacenarlos en caché
                    response.getData().forEach((key, objeto) -> {
                        objetos.add(objeto);
                        objetosCache.put(String.valueOf(objeto.getId()), objeto);
                    });
                }
            } else {
                System.err.println("Error al obtener los datos de Data Dragon. Código de respuesta: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al procesar la solicitud: " + e.getMessage());
        }
        return objetos;
    }
}
