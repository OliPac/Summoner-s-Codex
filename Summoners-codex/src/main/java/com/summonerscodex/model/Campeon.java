package com.summonerscodex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Campeon {
    private String id;
    private String name;
    private String title;
    private String lore;

    @JsonProperty("image")
    private ImageInfo imageInfo;

    @JsonProperty("stats")
    private Stats stats;

    @JsonProperty("skins")
    private List<Skin> skinsList; // Lista de skins

    private static final String BASE_HABILIDAD_URL = "https://ddragon.leagueoflegends.com/cdn/14.20.1/img/spell/";

    // Constructor por defecto
    public Campeon() {}

    // Método para obtener la URL de la imagen del campeón
    public String getImageUrl() {
        return "https://ddragon.leagueoflegends.com/cdn/14.20.1/img/champion/" + imageInfo.getFull();
    }

    // Método para obtener la URL de la splash art del campeón
    public String getSplashArt() {
        return "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + id + "_0.jpg";
    }

    // Método para obtener la lista de URLs de skins
    public List<String> getSkins() {
        List<String> skins = new ArrayList<>();
        // Agregar cada skin en función de la lista de skins disponibles
        if (skinsList != null) {
            for (Skin skin : skinsList) {
                // Solo agregar skins cuyo número sea mayor que 0
                if (skin.getNum() > 0) {
                    String skinUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + id + "_" + skin.getNum() + ".jpg";
                    skins.add(skinUrl);
                }
            }
        }
        return skins;
    }

    // Métodos para obtener las URLs de habilidades
    public String getHabilidadQUrl() {
        return BASE_HABILIDAD_URL + id + "Q.png";
    }

    public String getHabilidadWUrl() {
        return BASE_HABILIDAD_URL + id + "W.png";
    }

    public String getHabilidadEUrl() {
        return BASE_HABILIDAD_URL + id + "E.png";
    }

    public String getHabilidadRUrl() {
        return BASE_HABILIDAD_URL + id + "R.png";
    }

    // Clase interna para la información de la imagen del campeón
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

    // Nueva clase interna para manejar las estadísticas
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats {
        private int hp;
        private int mp;
        private int movespeed;
        private int armor;
        private int spellblock;
        private int attackdamage;
        private double attackspeed;

        public int getHp() {
            return hp;
        }

        public void setHp(int hp) {
            this.hp = hp;
        }

        public int getMp() {
            return mp;
        }

        public void setMp(int mp) {
            this.mp = mp;
        }

        public int getMovespeed() {
            return movespeed;
        }

        public void setMovespeed(int movespeed) {
            this.movespeed = movespeed;
        }

        public int getArmor() {
            return armor;
        }

        public void setArmor(int armor) {
            this.armor = armor;
        }

        public int getSpellblock() {
            return spellblock;
        }

        public void setSpellblock(int spellblock) {
            this.spellblock = spellblock;
        }

        public int getAttackdamage() {
            return attackdamage;
        }

        public void setAttackdamage(int attackdamage) {
            this.attackdamage = attackdamage;
        }

        public double getAttackspeed() {
            return attackspeed;
        }

        public void setAttackspeed(double attackspeed) {
            this.attackspeed = attackspeed;
        }

        @Override
        public String toString() {
            return "Stats [hp=" + hp + ", mp=" + mp + ", movespeed=" + movespeed + 
                   ", armor=" + armor + ", spellblock=" + spellblock + 
                   ", attackdamage=" + attackdamage + ", attackspeed=" + attackspeed + "]";
        }
    }

    // Clase interna para manejar la información de las skins
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Skin {
        private int num; // Número de la skin
        private String name; // Nombre de la skin

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Skin [num=" + num + ", name=" + name + "]";
        }
    }

    // Getters y setters de la clase Campeon
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Campeon [id=" + id + ", name=" + name + ", title=" + title + ", lore=" + lore + 
               ", imageInfo=" + imageInfo + ", stats=" + stats + ", skinsList=" + skinsList + "]";
    }
}
