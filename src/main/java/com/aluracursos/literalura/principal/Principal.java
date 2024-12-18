package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.service.AutorService;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LibroService;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    //Atributos
    public static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroService libroServicio;
    private AutorService autorServicio;

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroServicio = libroService;
        this.autorServicio = autorService;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {

            try {
                String menu = """
                ------------
                **Catálogo de libros en Literalura**
                1- Buscar libro por título
                2- Listar libros registrados
                3- Listar autores registrados
                4- Listar autores vivos en un determinado año
                5- Listar libros por idioma
                0- Salir
                
                Elija la opción a través de su número:""";

                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {

                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        //buscarAutoresVivosPorAnio();
                        break;
                    case 5:
                        System.out.println("Mensaje 5");
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida. Favor de introducir un número del menú.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ocurrió un error.");
                teclado.nextLine();
            }
        }
    }

    //Metodo para consultar información libros en la API
    private DatosResultados obtenerDatosResultados(String tituloLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ", "%20"));
        var datos = conversor.obtenerDatos(json, DatosResultados.class);
        return datos;
    }

    //Metodo para buscar información de un libro por el nombre
    private void buscarLibroPorTitulo() {
        System.out.print("Escribe el título del libro que deseas buscar: ");
        var tituloLibro = teclado.nextLine().toLowerCase();

        Optional<Libro> libroRegistrado = libroServicio.buscarLibroPorTitulo(tituloLibro);

        //Buscar el libro en la base de datos
        if (libroRegistrado.isPresent()) {

            System.out.println("El libro buscado ya está registrado.");

        } else {

            var datos = obtenerDatosResultados(tituloLibro);

            //Busca si el libro se encuentra en la API
            if (datos.listaLibros().isEmpty()){

                System.out.println("Libro no encontrado.");

            } else {

                DatosLibros datosLibros = datos.listaLibros().get(0);
                DatosAutores datosAutores = datosLibros.autores().get(0);
                String idioma = datosLibros.idiomas().get(0);
                Idiomas idiomas = Idiomas.fromString(idioma);

                Libro libro = new Libro(datosLibros);
                libro.setIdiomas(idiomas);

                Optional<Autor> autorRegistrado = autorServicio.buscarAutorPorNombre(datosAutores.nombre());

                if (autorRegistrado.isPresent()) {

                    System.out.println("El autor ya está registrado.");
                    Autor autorExiste = autorRegistrado.get();
                    libro.setAutor(autorExiste);

                } else {

                    Autor autor = new Autor(datosAutores);
                    autor = autorServicio.guardarAutor(autor);
                    libro.setAutor(autor);
                    autor.getLibros().add(libro);

                }//Finaliza tercer if

                libroServicio.guardarLibro(libro);
                System.out.println("\nLibro encontrado.\n");
                System.out.println(libro+"\n");
                System.out.println("Libro y autor guardado.\n");

            }//Finaliza segundo if

        }//Finaliza primer if

    }

    private void listarLibrosRegistrados() {

        List<Libro> libros = libroServicio.listarLibrosRegistrados();

        if (libros.isEmpty()) {

            System.out.println("No hay libros registrados.");

        } else {

            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);

        }//Finaliza if

    }//Finaliza método listar libros registrados

    private void listarAutoresRegistrados() {

        List<Autor> autores = autorServicio.listarAutoresRegistrados();


        if (autores.isEmpty()) {

            System.out.println("No hay autores registrados.");

        } else {

            for (Autor autor : autores) {

                List<Libro> librosPorAutorId = libroServicio.buscarLibrosPorAutorId(autor.getId());

                System.out.println("Autor: "+autor.getNombre());
                System.out.println("Fecha de Nacimiento: "+autor.getFechaNacimiento());
                System.out.println("Fecha de Fallecido: "+autor.getFechaFallecido());

                if (librosPorAutorId.isEmpty()) {

                    System.out.println("No hay libros registrados para este autor.");

                } else {

                    String librosRegistrados = librosPorAutorId.stream()
                            .map(Libro::getTitulo)
                                    .collect(Collectors.joining(", "));
                    System.out.println("Libros: ["+librosRegistrados+"]\n");

                }//Finaliza segundo if

            }//Finaliza bucle

        }//Finaliza primer if

    }//Finaliza método listar autores registrados

}
