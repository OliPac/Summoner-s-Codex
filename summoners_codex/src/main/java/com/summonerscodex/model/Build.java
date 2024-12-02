package com.summonerscodex.model;

import java.util.List;

public class Build {

    private Campeon campeon;  
    private List<String> items;  

    public Build(Campeon campeon, List<String> items) {
        this.campeon = campeon;
        this.items = items;
    }
    public Campeon getCampeon() {
        return campeon;
    }

    public List<String> getItems() {
        return items;
    }
    public void addObjeto(String objeto) {
        if (objeto != null && !objeto.isEmpty()) {
            this.items.add(objeto);
        }
    }
    public void removeObjeto(String objeto) {
        this.items.remove(objeto);
    }
    public boolean tieneObjetos() {
        return !this.items.isEmpty();
    }
    public void setCampeon(Campeon campeon) {
        this.campeon = campeon;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
    public String getCampeonImageUrl() throws Exception {
        return campeon != null ? campeon.getImageUrl() : "";  
    }
}
