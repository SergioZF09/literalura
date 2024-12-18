package com.aluracursos.literalura.model;

public enum Idiomas {
    INGLES("en"),
    ESPANOL("es");

    private String idiomasGutendex;

    Idiomas (String idiomasGutendex) {
        this.idiomasGutendex = idiomasGutendex;
    }

    public static Idiomas fromString (String text) {
        for (Idiomas idiomas : Idiomas.values()) {
            if (idiomas.idiomasGutendex.equalsIgnoreCase(text)) {
                return idiomas;
            }
        }
        throw new IllegalArgumentException("No se encontró ningún idioma: " + text);
    }
}
