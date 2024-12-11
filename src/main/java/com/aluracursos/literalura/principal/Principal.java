package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.DatosLibros;
import com.aluracursos.literalura.model.DatosResultados;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    //Atributos
    public static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {

            try {
                String menu = """
                ------------
                Elija la opción a través de su número:
                1- buscar libro por título
                2- listar libros registrados
                3- listar autores registrados
                4- listar autores vivos en un determinado año
                5- listar libros por idioma
                0- salir""";

                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {

                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        System.out.println("Mensaje 2");
                        break;
                    case 3:
                        System.out.println("Mensaje 3");
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
        //System.out.println(json);
        System.out.println();
        var datos = conversor.obtenerDatos(json, DatosResultados.class);
        return datos;
    }

    //Metodo para buscar información de un libro por el nombre
    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el título del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        var datosLibros = obtenerDatosResultados(tituloLibro);
        Optional<DatosLibros> libroEncontrado = datosLibros.listaLibros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                .findFirst();

        if (libroEncontrado.isEmpty()){
            System.out.println("Libro no encontrado.");
        } else {
            System.out.println("Libro encontrado.");
            System.out.println(libroEncontrado.get());
        }
    }

}
