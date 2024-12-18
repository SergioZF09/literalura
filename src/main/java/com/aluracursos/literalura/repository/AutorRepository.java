package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    //Buscar el nombre del autor en la base de datos
    Optional<Autor> findByNombreIgnoreCase(String nombre);
}
