package com.summonerscodex.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.summonerscodex.model.Usuario;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public String guardarUsuario(Usuario usuario) {
        MongoDatabase database = MongoDBConexion.getDatabase();

        if (database != null) {
            MongoCollection<Document> collection = database.getCollection("usuarios");
            Document usuarioExistente = collection.find(Filters.eq("usuario", usuario.getUsuario())).first();
            Document correoExistente = collection.find(Filters.eq("correo", usuario.getCorreo())).first();

            if (usuarioExistente != null) {
                logger.warn("El usuario ya existe: " + usuario.getUsuario());
                return "El nombre de usuario ya está en uso. Por favor, elige otro.";
            } else if (correoExistente != null) {
                logger.warn("El correo ya está en uso: " + usuario.getCorreo());
                return "El correo ya está en uso. Por favor, utiliza otro.";
            } else {
                Document nuevoUsuario = new Document("usuario", usuario.getUsuario())
                        .append("correo", usuario.getCorreo())
                        .append("contraseña", usuario.getContraseña());
                collection.insertOne(nuevoUsuario);
                logger.info("Usuario registrado con éxito: " + usuario.getUsuario());
                return "Registro exitoso.";
            }
        } else {
            logger.error("No se pudo obtener la base de datos de MongoDB.");
            return "No se pudo conectar a la base de datos. Inténtalo más tarde.";
        }
    }

    public boolean autenticarUsuario(String username, String password) {
        MongoDatabase database = MongoDBConexion.getDatabase();
        
        if (database != null) {
            MongoCollection<Document> collection = database.getCollection("usuarios");
            try (MongoCursor<Document> cursor = collection.find(Filters.eq("usuario", username)).iterator()) {
                if (cursor.hasNext()) {
                    Document userDoc = cursor.next();
                    String storedPassword = userDoc.getString("contraseña");
                    return password.equals(storedPassword);
                }
            }
        } else {
            logger.error("No se pudo obtener la base de datos de MongoDB.");
        }
        
        return false;
    }
}