package com.summonerscodex.services.icons;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.summonerscodex.services.MongoDBConexion;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class CampeonesImageServices {

    // Método para insertar una URL en MongoDB
    public void insertUrlInMongoDB(String championName, String imageUrl) {
        MongoDatabase database = MongoDBConexion.getDatabase();

        if (database != null) {
            MongoCollection<Document> collection = database.getCollection("Iconos campeones");

            // Verificar si la URL ya existe
            try (MongoCursor<Document> cursor = collection.find(Filters.eq("url", imageUrl)).iterator()) {
                if (cursor.hasNext()) {
                    System.out.println("La URL ya existe para este campeón.");
                    return; // Sale del método si la URL ya existe
                }
            } // El cursor se cierra automáticamente aquí

            // Crear un nuevo documento para la URL
            Document document = new Document("nombre", championName)
                    .append("url", imageUrl);

            // Insertar el documento en la colección
            collection.insertOne(document);
            System.out.println("URL guardada para el campeón: " + championName);
        } else {
            System.out.println("No se pudo obtener la base de datos de MongoDB.");
        }
    }

    // Método para obtener las URLs de las imágenes de campeones desde MongoDB
    public List<String> obtenerUrlsDesdeMongoDB() {
        List<String> imageUrls = new ArrayList<>(); // Lista para almacenar las URLs
        MongoDatabase database = MongoDBConexion.getDatabase();
        
        if (database != null) {
            MongoCollection<Document> collection = database.getCollection("Iconos campeones");
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    String imageUrl = doc.getString("url"); // Suponiendo que la URL se guarda bajo la clave "url"
                    imageUrls.add(imageUrl); // Añadir la URL a la lista
                }
            }
        } else {
            System.out.println("No se pudo obtener la base de datos de MongoDB.");
        }
        return imageUrls; // Devuelve la lista de URLs
    }

    // Método para comprobar si la URL ya existe en MongoDB
    public boolean existeEnMongoDB(String imageUrl) {
        MongoDatabase database = MongoDBConexion.getDatabase();

        if (database != null) {
            MongoCollection<Document> collection = database.getCollection("Iconos campeones");

            try (MongoCursor<Document> cursor = collection.find(Filters.eq("url", imageUrl)).iterator()) {
                return cursor.hasNext(); // Retorna true si existe, false si no
            } // El cursor se cierra automáticamente aquí
        } else {
            System.out.println("No se pudo obtener la base de datos de MongoDB.");
            return false; // Retorna false si no se puede obtener la base de datos
        }
    }
}
