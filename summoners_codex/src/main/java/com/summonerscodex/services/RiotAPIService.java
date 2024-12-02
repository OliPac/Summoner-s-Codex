package com.summonerscodex.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.net.URL;

public class RiotAPIService {

    private static final String VERSION_URL = "https://ddragon.leagueoflegends.com/realms/euw.json";

    public static String obtenerUltimaVersion() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = new URL(VERSION_URL).openStream();
        JsonNode node = objectMapper.readTree(inputStream);
        return node.get("v").asText();  // Obtiene la versión de los datos
    }
}
