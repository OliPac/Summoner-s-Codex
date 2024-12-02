package com.summonerscodex.services;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConexion {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static final String CONNECTION_STRING = "mongodb+srv://oliverespinar:pacmaniaco@cluster0.ienfh.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DB_NAME = "base_de_datos"; 

    // Conexión a MongoDB
    public static void conectar() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();     
        try {
            ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(serverApi)
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(DB_NAME);
            database.runCommand(new Document("ping", 1));
            System.out.println("Conectado a MongoDB en la base de datos: " + DB_NAME);
        } catch (MongoException e) {
            System.out.println("Error al conectar a MongoDB: " + e.getMessage());
        }
    }

    // Obtener la base de datos
    public static MongoDatabase getDatabase() {
        return database;
    }

    // Cerrar la conexión con MongoDB
    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }

}
