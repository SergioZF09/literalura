package com.aluracursos.literalura.model;

public enum Idiomas {
    //inglés
    en("en"),
    //español
    es("es"),
    //francés
    fr("fr"),
    //húngaro
    hu("hu"),
    //finés
    fi("fi"),
    //portugués
    pt("pt");

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
