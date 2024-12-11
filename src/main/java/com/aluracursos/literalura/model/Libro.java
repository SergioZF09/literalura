package com.aluracursos.literalura.model;

import java.util.List;

public class Libro {

    //Atributos
    private String titulo;
    private List<DatosAutores> autores;
    private List<String> idiomas;
    private Double numeroDescargas;

    //Constructor con atributos
    public Libro() {}

    //Constructor con atributos
    public Libro(String titulo, List<DatosAutores> autores, List<String> idiomas, Double numeroDescargas) {
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = idiomas;
        this.numeroDescargas = numeroDescargas;
    }

    //Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<DatosAutores> getAutores() {
        return autores;
    }

    public void setAutores(List<DatosAutores> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    //toString

    @Override
    public String toString() {
        return "Título: " + titulo + '\n' +
                "Autores: " + autores + '\n' +
                "Idiomas: " + idiomas + '\n' +
                "Número de Descargas: " + numeroDescargas;
    }

}
