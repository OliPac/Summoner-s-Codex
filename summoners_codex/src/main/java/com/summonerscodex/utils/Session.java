package com.summonerscodex.utils;

public class Session {
    private static Session instancia;
    private String usuarioAutenticado;

    
    private Session() {}

    
    public static Session getInstance() {
        if (instancia == null) {
            instancia = new Session();
        }
        return instancia;
    }

    public void setUsuarioAutenticado(String usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public String getUsuarioAutenticado() {
        return usuarioAutenticado;
    }


    public void cerrarSesion() {
        usuarioAutenticado = null;
    }
}
