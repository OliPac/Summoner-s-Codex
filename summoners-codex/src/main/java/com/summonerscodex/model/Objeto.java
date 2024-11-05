package com.summonerscodex.model;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Objeto {
    private int id; // ID del Objeto
    private String name; // Nombre del objeto
    private String description; // Descripción en formato HTML
    private String plaintext; // Texto plano del objeto
    private List<String> into; // Lista de IDs de objetos en los que se transforma

    @JsonProperty("image")
    private ImageInfo imageInfo;

    @JsonProperty("gold")
    private Gold gold; // Asegúrate de que sea un atributo de tipo Gold

    @JsonProperty("stats")
    private Map<String, Double> stats; // Usamos un Map para almacenar estadísticas

    // Constructor por defecto
    public Objeto() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public List<String> getInto() {
        return into;
    }

    public void setInto(List<String> into) {
        this.into = into;
    }

    public Gold getGold() { // Método getter para gold
        return gold;
    }

    public void setGold(Gold gold) { // Método setter para gold
        this.gold = gold;
    }

    public Map<String, Double> getStats() { // Método getter para stats
        return stats;
    }

    public void setStats(Map<String, Double> stats) { // Método setter para stats
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Objeto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", plaintext='" + plaintext + '\'' +
                ", into=" + into +
                ", gold=" + gold +
                ", stats=" + stats +
                '}';
    }

    public String getImageUrl() {
        return "https://ddragon.leagueoflegends.com/cdn/14.21.1/img/item/" + imageInfo.getFull();
    }

    // Clase interna para la información de la imagen del objeto
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageInfo {
        private String full;

        public String getFull() {
            return full;
        }

        public void setFull(String full) {
            this.full = full;
        }

        @Override
        public String toString() {
            return "ImageInfo [full=" + full + "]";
        }
    }

    // Nueva clase interna para manejar el oro
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gold {
        private int base;
        private int sell;

        public int getBase() {
            return base;
        }

        public void setBase(int base) {
            this.base = base;
        }

        public int getSell() {
            return sell;
        }

        public void setSell(int sell) {
            this.sell = sell;
        }
    }
}
