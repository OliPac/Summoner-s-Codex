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

    private static final String DATA_DRAGON_URL = "https://ddragon.leagueoflegends.com/cdn/14.21.1/data/es_ES/item.json";
    private Map<String, Objeto> objetosCache; // Cache para objetos

    // Método para obtener todos los objetos con su información desde Data Dragon
    public List<Objeto> obtenerObjetosDesdeDataDragon() {
        List<Objeto> objetos = new ArrayList<>();
        objetosCache = new HashMap<>(); // Inicializar el caché

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

                // Mapear la respuesta JSON a un objeto de ObjetoResponse
                ObjetoResponse response = objectMapper.readValue(inputStream, ObjetoResponse.class);
                
                // Extraer información de cada objeto y almacenarlos en caché
                response.getData().forEach((key, objeto) -> {
                    objetos.add(objeto);
                    objetosCache.put(String.valueOf(objeto.getId()), objeto); // Convertir el ID a String
                });
            } else {
                System.out.println("Error al obtener los datos de Data Dragon. Código de respuesta: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
        }

        return objetos;
    }
}
