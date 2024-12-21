package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.service.AutorService;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LibroService;
import org.springframework.dao.DataIntegrityViolationException;

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
                        buscarAutoresVivosPorAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida. Favor de introducir un número del menú.");
                }
            } catch (InputMismatchException e) {
                System.out.println("OOpción inválida. Favor de introducir un número del menú.");
                teclado.nextLine();
            }
        }
    }

    //Metodo para consultar información sobre libros en la API
    private DatosResultados obtenerDatosResultados(String tituloLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ", "%20"));
        var datos = conversor.obtenerDatos(json, DatosResultados.class);
        return datos;
    }//Finaliza método obtenerDatosResultados

    //Metodo para buscar información de un libro por el título
    private void buscarLibroPorTitulo() {

        System.out.print("Escribe el título del libro que deseas buscar: ");
        var tituloLibro = teclado.nextLine().toUpperCase();

        //Busca en la base de datos el libro por título
        Optional<Libro> libroRegistrado = libroServicio.buscarLibroPorTitulo(tituloLibro);

        //Buscar el libro en la base de datos
        if (libroRegistrado.isPresent()) {
            System.out.println("El libro buscado ya está registrado.");
        } else {
            //Busca los datos del libro en la API
            var datos = obtenerDatosResultados(tituloLibro);

            //Busca si el libro se encuentra en la API
            if (datos.listaLibros().isEmpty()){
                //Si no se encuentra dicho libro, muestra el siguiente mensaje
                System.out.println(" No se encontró el libro buscado.");
            } else {
                //Si se encuentra en la API, obtiene cada dato del libro buscado con el autor o autores
                // y se guardan en su respectivo record. Y el idioma se transforma en cadena
                DatosLibros datosLibros = datos.listaLibros().get(0);
                DatosAutores datosAutores = datosLibros.autores().get(0);
                String idioma = datosLibros.idiomas().get(0);
                Idiomas idiomas = Idiomas.fromString(idioma);

                //Se guarda toda la info de datosLibros en la clase Libro y se guarda manualmente el primer idioma
                Libro libro = new Libro(datosLibros);
                libro.setIdiomas(idiomas);

                //Busca en la base de datos el autor del libro buscado por su nombre
                Optional<Autor> autorRegistrado = autorServicio.buscarAutorPorNombre(datosAutores.nombre());

                //Verifica si el dicho autor está registrado
                if (autorRegistrado.isPresent()) {
                    //Si está registrado, sólo se guarda el autor en el libro buscado
                    System.out.println("El autor ya está registrado.");
                    Autor autorExiste = autorRegistrado.get();
                    libro.setAutor(autorExiste);
                } else {
                    //Si no está registrado, se registra el autor en la base de datos
                    // Se guarda dicho autor en el libro buscado y dicho libro se guarda en el autor
                    Autor autor = new Autor(datosAutores);
                    autor = autorServicio.guardarAutor(autor);
                    libro.setAutor(autor);
                    autor.getLibros().add(libro);
                }//Finaliza tercer if

                try {
                    //Se guarda el libro buscado en la base de datos
                    libroServicio.guardarLibro(libro);
                    System.out.println("\nLibro encontrado.\n");
                    System.out.println(libro+"\n");
                    System.out.println("Libro y autor guardado.\n");
                } catch (DataIntegrityViolationException e){
                    //Si el libro está registrado, no se guarda
                    System.out.println("El libro ya está registrado.");
                }//Finaliza try-catch
            }//Finaliza segundo if
        }//Finaliza primer if

    }//Finaliza método buscar libro por título

    //Metodo para listar los libros registrados
    private void listarLibrosRegistrados() {

        //Obtiene en la base de datos todos los libros registrados
        List<Libro> libros = libroServicio.listarLibrosRegistrados();

        //Verifica si existen libros registrados en la base de datos
        if (libros.isEmpty()) {
            //Si no existen libros registrados, muestra el siguiente mensaje
            System.out.println("No se encontraron libros registrados.");
        } else {
            //Si existen libros, los muestra ordenados alfabéticamente por título
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }//Finaliza if

    }//Finaliza método listar libros registrados

    //Metodo para listar los autores con sus libros registrados
    private void listarAutoresRegistrados() {

        //Obtiene en la base de datos todos los autores registrados
        List<Autor> autores = autorServicio.listarAutoresRegistrados();

        //Verifica si existen autores registrados en la base de datos
        if (autores.isEmpty()) {
            //Si no existen autores registrados, muestra el siguiente mensaje
            System.out.println("No se encontraron autores registrados.");
        } else {
            //Si existen autores, los muestra en un forEach
            for (Autor autor : autores) {
                //Busca en la base de datos los libros del autor por id de dicho autor
                List<Libro> librosPorAutorId = libroServicio.buscarLibrosPorAutorId(autor.getId());

                //Muestra la información de todos los autores registrados con sus libros
                System.out.println("----- AUTOR -----");
                System.out.println("Autor: "+autor.getNombre());
                System.out.println("Fecha de Nacimiento: "+autor.getFechaNacimiento());
                System.out.println("Fecha de Fallecido: "+autor.getFechaFallecido());

                //Verifica si existen libros registrados en la base de datos
                if (librosPorAutorId.isEmpty()) {
                    //Si no existen libros registrados, muestra el siguiente mensaje
                    System.out.println("No se encontraron libros registrados para este autor.");
                } else {
                    //Si existen libros, muestra los títulos de los libros del autor indicado y juntos como lista
                    String librosRegistrados = librosPorAutorId.stream()
                            .map(Libro::getTitulo)
                                    .collect(Collectors.joining(", "));
                    System.out.println("Libros: ["+librosRegistrados+"]");
                    System.out.println("-------------------\n");
                }//Finaliza segundo if
            }//Finaliza bucle for
        }//Finaliza primer if

    }//Finaliza método listar autores registrados

    //Metodo para buscar los autores vivos por el año determinado
    private void buscarAutoresVivosPorAnio() {

        System.out.print("Escribe el año vivo de autor(es) que desea buscar: ");
        var anioDelAutor = teclado.nextInt();

        //Busca en la base de datos el autor o autores que estaban vivos por su año
        List<Autor> buscarAutoresPorAnio = autorServicio.buscarAutoresVivosPorAnio(anioDelAutor);

        //Verifica si existen autores que estaban vivos por su año
        if (buscarAutoresPorAnio.isEmpty()) {
            //Si no existen dichos autores, muestra el siguiente mensaje
            System.out.println("No se encontraron autores vivos por el año buscado.");
        } else {
            //Si existen dichos autores, los muestra en un forEach y hace mismo proceso que el metodo pasado
            for (Autor autoresVivos : buscarAutoresPorAnio) {
                List<Libro> librosAutoresVivosPorId = libroServicio.buscarLibrosPorAutorId(autoresVivos.getId());

                System.out.println("----- AUTOR -----");
                System.out.println("Autor: "+autoresVivos.getNombre());
                System.out.println("Fecha de Nacimiento: "+autoresVivos.getFechaNacimiento());
                System.out.println("Fecha de Fallecido: "+autoresVivos.getFechaFallecido());

                if (librosAutoresVivosPorId.isEmpty()) {
                    System.out.println("No se encontraron libros registrados para este autor.");
                } else {
                    String librosRegistrados = librosAutoresVivosPorId.stream()
                            .map(Libro::getTitulo)
                            .collect(Collectors.joining(", "));
                    System.out.println("Libros: ["+librosRegistrados+"]");
                    System.out.println("-------------------\n");
                }//Finaliza segundo if
            }//Finaliza bucle for
        }//Finaliza primer if

    }//Finaliza método buscar autores vivos por determinado año

    //Metodo para listar libros por idioma
    private void listarLibrosPorIdioma() {

        System.out.println("""
                Estos son los idiomas disponibles:
                - es -> Español
                - en -> Inglés
                - fr -> Francés
                - pt -> Portugués
                """
        );

        System.out.print("Escribe el idioma abreviado para buscar los libros: ");
        var nombreIdioma = teclado.nextLine();

        //Valida la información que se escribe en la consola
        try {
            //Busca en la base de datos los libros por idioma
            List<Libro> buscarLibrosPorIdioma = libroServicio.buscarLibroPorIdiomas(Idiomas.fromString(nombreIdioma));

            //Verifica si existen los libros por idioma
            if (buscarLibrosPorIdioma.isEmpty()) {
                //Si no existen dichos libros, muestra el siguiente mensaje
                System.out.println("No se encontraron los libros del idioma buscado.");
            } else {
                //Si existen dichos libros, los muestra en un forEach
                buscarLibrosPorIdioma.forEach(l -> System.out.print(l.toString()));
            }//Finaliza if
        } catch (Exception e) {
            System.out.println("Opción inválida. Favor de escribir un idioma abreviado del menú.");
        }// Finaliza try-catch

    }//Finaliza método listar libros por idioma

}