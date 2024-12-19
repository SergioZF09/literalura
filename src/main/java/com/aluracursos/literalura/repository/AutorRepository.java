package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    //Buscar el nombre del autor en la base de datos
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    //Busca los autores vivos por un determinado año
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento < :anioAutor AND a.fechaFallecido > :anioAutor")
    List<Autor> buscarAutoresVivosPorAnio(int anioAutor);
}
