package com.aluracursos.literalura.model;

public class Autor {
    //Atributos
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    //Constructor vacío
    public Autor() {}

    //Constructor con atributos

    public Autor(String nombre, Integer fechaNacimiento, Integer fechaFallecimiento) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
    }

    //Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    //toString

    @Override
    public String toString() {
        return "Nombre: " + nombre + '\n' +
                "Fecha de Nacimiento: " + fechaNacimiento + '\n' +
                "Fecha de Fallecimiento: " + fechaFallecimiento;
    }

}
