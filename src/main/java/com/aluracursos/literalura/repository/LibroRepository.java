package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    //Buscar el nombre del libro en la base de datos
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    //Busca los libros registrados por el id del autor
    @Query("SELECT l FROM Libro l WHERE l.autor.id = :autorId")
    List<Libro> buscarLibrosPorAutorId(Long autorId);
}
