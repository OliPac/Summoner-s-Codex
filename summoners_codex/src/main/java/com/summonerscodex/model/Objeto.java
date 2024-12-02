package com.summonerscodex.model;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.summonerscodex.services.RiotAPIService;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Objeto {
    private int id; 
    private String name; 
    private String description; 
    private String plaintext; 
    private List<String> into; 
    private String VERSION;
    @JsonProperty("image")
    private ImageInfo imageInfo;

    @JsonProperty("gold")
    private Gold gold; 

    @JsonProperty("stats")
    private Map<String, Double> stats; 

    
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

    public Gold getGold() { 
        return gold;
    }

    public void setGold(Gold gold) {
        this.gold = gold;
    }

    public Map<String, Double> getStats() { 
        return stats;
    }

    public void setStats(Map<String, Double> stats) { 
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

    public String getImageUrl() throws Exception {
    	VERSION = RiotAPIService.obtenerUltimaVersion();
        return "https://ddragon.leagueoflegends.com/cdn/"+VERSION+"/img/item/" + imageInfo.getFull();
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
