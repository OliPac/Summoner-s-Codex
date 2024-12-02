package com.summonerscodex.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.summonerscodex.model.Pregunta;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PreguntaService {

    public List<Pregunta> obtenerPreguntas() {
    	MongoDBConexion.conectar();
        MongoDatabase database = MongoDBConexion.getDatabase();
        MongoCollection<Document> collection = database.getCollection("preguntas");  

        List<Pregunta> questions = new ArrayList<>();

        try {
            // Utilizamos un agregador con sample para obtener 10 preguntas aleatorias
            List<Document> result = collection.aggregate(
                    List.of(
                            Aggregates.sample(10),
                            Aggregates.project(Projections.fields( // 
                                    Projections.include("textoPregunta", "opcionCorrecta", "opcionA", "opcionB", "opcionC")
                            ))
                    )
            ).into(new ArrayList<>());

            // Convertir los documentos a objetos Question
            for (Document doc : result) {
                String textoPregunta = doc.getString("textoPregunta");
                String opcionCorrecta = doc.getString("opcionCorrecta");
                String opcionA = doc.getString("opcionA");
                String opcionB = doc.getString("opcionB");
                String opcionC = doc.getString("opcionC");

                // Crear una nueva pregunta y agregarla a la lista
                Pregunta question = new Pregunta(textoPregunta, opcionCorrecta, opcionA, opcionB, opcionC);
                questions.add(question);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener preguntas desde MongoDB: " + e.getMessage());
        }finally {
        	MongoDBConexion.cerrarConexion();
        }

        return questions;
        
    }
}
