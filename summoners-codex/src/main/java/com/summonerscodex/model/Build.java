package com.summonerscodex.model;

import java.util.List;

public class Build {
    private String nombreCampeon;
    private List<String> objetos;

    // Constructor
    public Build(String nombreCampeon, List<String> objetos) {
        this.nombreCampeon = nombreCampeon;
        this.objetos = objetos;
    }

    // Getters y Setters
    public String getNombreCampeon() {
        return nombreCampeon;
    }

    public void setNombreCampeon(String nombreCampeon) {
        this.nombreCampeon = nombreCampeon;
    }

    public List<String> getObjetos() {
        return objetos;
    }

    public void setObjetos(List<String> objetos) {
        this.objetos = objetos;
    }
}
