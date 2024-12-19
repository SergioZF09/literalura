package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepositorio;

    public Optional<Autor> buscarAutorPorNombre(String nombre) {
        return autorRepositorio.findByNombreIgnoreCase(nombre);
    }

    public Autor guardarAutor(Autor autor) {

//        if (autorRepositorio.existsByNombre(autor.getNombre())) {
//            throw new RuntimeException("El autor ya está registrado");
//        }

        return autorRepositorio.save(autor);
    }

    public List<Autor> listarAutoresRegistrados() {
        return autorRepositorio.findAll();
    }

    public List<Autor> buscarAutoresVivosPorAnio(int autorAnio) {
        return autorRepositorio.buscarAutoresVivosPorAnio(autorAnio);
    }
}
