package com.summonerscodex.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBConexion {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBConexion.class);
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // Cambia estos valores según tus necesidades
    private static final String CONNECTION_STRING = "mongodb+srv://oliverespinar:pacmaniaco@cluster0.ienfh.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DB_NAME = "base_de_datos"; // Cambia esto por el nombre de tu base de datos

    // Método para iniciar la conexión
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
            // Enviar un ping para confirmar una conexión exitosa
            database.runCommand(new Document("ping", 1));
            logger.info("Conectado a MongoDB en la base de datos: " + DB_NAME);
        } catch (MongoException e) {
            logger.error("Error al conectar a MongoDB", e);
        }
    }

    // Método para obtener la base de datos
    public static MongoDatabase getDatabase() {
        return database;
    }

    // Método para cerrar la conexión
    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("Conexión a MongoDB cerrada.");
        }
    }
}
