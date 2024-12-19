package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepositorio;

    public Optional<Libro> buscarLibroPorTitulo(String titulo) {
        return libroRepositorio.findByTituloIgnoreCase(titulo);
    }

    public Libro guardarLibro(Libro libro) {
        return libroRepositorio.save(libro);
    }

    public List<Libro> listarLibrosRegistrados() {
        return libroRepositorio.findAll();
    }

    public List<Libro> buscarLibrosPorAutorId(Long id) {
        return libroRepositorio.buscarLibrosPorAutorId(id);
    }

}
