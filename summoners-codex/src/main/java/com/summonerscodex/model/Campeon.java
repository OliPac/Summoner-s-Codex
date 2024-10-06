package com.summonerscodex.model;

public class Campeon {
    private String nombre;
    private String titulo;
    private String descripcion;
    private String lore;

    // Constructor
    public Campeon(String nombre, String titulo, String descripcion,String lore) {
        this.nombre = nombre;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.lore = lore;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}
}
