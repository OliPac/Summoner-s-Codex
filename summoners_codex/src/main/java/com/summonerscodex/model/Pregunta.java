package com.summonerscodex.model;

public class Pregunta {
    private String textoPregunta;
    private String opcionCorrecta;
    private String opcionA, opcionB, opcionC;

    public Pregunta(String textoPregunta, String opcionCorrecta, String opcionA, String opcionB, String opcionC) {
        this.textoPregunta = textoPregunta;
        this.opcionCorrecta = opcionCorrecta;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public String getOpcionCorrecta() {
        return opcionCorrecta;
    }

    public String getOpcionA() {
        return opcionA;
    }

    public String getOpcionB() {
        return opcionB;
    }

    public String getOpcionC() {
        return opcionC;
    }

    public boolean esCorrecta(String opcion) {
        return opcionCorrecta.equalsIgnoreCase(opcion);
    }
}
