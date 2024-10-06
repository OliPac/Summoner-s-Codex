package com.summonerscodex.services.icons;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChampionImageUrlExtractor {
    private static CampeonesImageServices campeonImageServices = new CampeonesImageServices(); // Inicializa aquí

    public static void guardarUrlsCampeones() {
        try {
            // URL del archivo JSON con los campeones
            String championJsonUrl = "https://ddragon.leagueoflegends.com/cdn/14.19.1/data/en_US/champion.json";

            // Crear un HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Obtener el archivo JSON con la lista de campeones
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(championJsonUrl))
                    .GET()
                    .build();

            // Enviar la solicitud y obtener la respuesta en formato String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsear el JSON de la respuesta
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject dataObject = jsonObject.getAsJsonObject("data");

            // Iterar sobre cada campeón en el JSON
            for (Map.Entry<String, com.google.gson.JsonElement> entry : dataObject.entrySet()) {
                String championName = entry.getKey();

                // Construir la URL de la imagen del campeón
                String imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.19.1/img/champion/" + championName + ".png";

                // Aquí guardarías la URL en MongoDB
                System.out.println("URL de la imagen de " + championName + ": " + imageUrl);
                
                // Llamada a la función que inserte la URL en MongoDB
                campeonImageServices.insertUrlInMongoDB(championName, imageUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}