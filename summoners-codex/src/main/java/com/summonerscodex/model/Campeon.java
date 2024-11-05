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
    private List<Skin> skinsList;

    @JsonProperty("spells")
    private List<Ability> abilities;

    // Constructor por defecto
    public Campeon() {}

    // Método para obtener la URL de la imagen del campeón
    public String getImageUrl() {
        return "https://ddragon.leagueoflegends.com/cdn/14.21.1/img/champion/" + imageInfo.getFull();
    }

    // Método para obtener la URL de la splash art del campeón
    public String getSplashArt() {
        return "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + id + "_0.jpg";
    }

    // Método para obtener la lista de URLs de skins
    public List<String> getSkins() {
        List<String> skins = new ArrayList<>();
        if (skinsList != null) {
            for (Skin skin : skinsList) {
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
        return getAbilityUrl(0);
    }

    public String getHabilidadWUrl() {
        return getAbilityUrl(1);
    }

    public String getHabilidadEUrl() {
        return getAbilityUrl(2);
    }

    public String getHabilidadRUrl() {
        return getAbilityUrl(3);
    }

    private String getAbilityUrl(int index) {
        if (abilities != null && index < abilities.size()) {
            return "https://ddragon.leagueoflegends.com/cdn/14.21.1/img/spell/" + abilities.get(index).getImage().getFull();
        }
        return null;
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
        private int num;
        private String name;

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

    // Clase interna para manejar la información de las habilidades
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ability {
        private String id;
        private String name;
        private String description;
        private TooltipImage image;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public TooltipImage getImage() {
            return image;
        }

        public void setImage(TooltipImage image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "Ability [id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + "]";
        }
    }

    // Clase interna para manejar la información de la imagen de la habilidad
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TooltipImage {
        private String full;

        public String getFull() {
            return full;
        }

        public void setFull(String full) {
            this.full = full;
        }

        @Override
        public String toString() {
            return "TooltipImage [full=" + full + "]";
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

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        return "Campeon [id=" + id + ", name=" + name + ", title=" + title + ", lore=" + lore + 
               ", imageInfo=" + imageInfo + ", stats=" + stats + ", skinsList=" + skinsList + ", abilities=" + abilities + "]";
    }
}
