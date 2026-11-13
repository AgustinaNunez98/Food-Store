package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger HIBERNATE_LOGGER = Logger.getLogger("org.hibernate");
    public static void main(String[] args) {
        HIBERNATE_LOGGER.setLevel(Level.SEVERE);
        CategoriaRepository categoriaRepository = new CategoriaRepository();
        ProductoRepository productoRepository = new ProductoRepository();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al sistema de gestión de productos y categorías.");

        int opcion = 0;
        do {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Gestión de categorías");
            System.out.println("2. Gestión de productos");
            System.out.println("3. Reportes");
            System.out.println("0. Salir");
            opcion = leerEntero(scanner);
            switch (opcion) {
                case 1:
                    System.out.println("Gestión de categorias");
                    int opcion_categoria = 0;
                    do {
                        System.out.println("Seleccione una opción:");
                        System.out.println("1. Crear categoría");
                        System.out.println("2. Modificar categorías");
                        System.out.println("3. Baja lógica de categoría");
                        System.out.println("4. Listado de categorías");
                        System.out.println("0. Volver al menu principal");
                        opcion_categoria = leerEntero(scanner);
                        switch (opcion_categoria) {
                            case 1:
                                System.out.println("Ingrese el nombre de la categoría:");
                                    String nombreCategoria = scanner.nextLine();
                                    while (nombreCategoria.trim().isEmpty()){
                                        System.out.println("El nombre de la categoría no puede estar vacío. Por favor, ingrese un nombre válido.");
                                        nombreCategoria = scanner.nextLine();
                                    }
                                System.out.println("Ingrese la descripción de la categoría:");
                                Categoria categoriaNueva = new Categoria(nombreCategoria, scanner.nextLine());
                                Categoria categoriaGuardada = categoriaRepository.guardar(categoriaNueva);
                                System.out.println("Categoría creada con éxito: " + categoriaGuardada.getId());
                                break;
                            case 2:
                                    List<Categoria> categorias = categoriaRepository.listarActivos();
                                    System.out.println("Modificar categoría");
                                    if (categorias.isEmpty()) {
                                        System.out.println("No hay categorías activas para modificar.");
                                    } else {
                                        for (Categoria categoria : categorias) {
                                            System.out.println("ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre() + ", Descripción: " + categoria.getDescripcion());
                                        }
                                        System.out.println("Ingrese el ID de la categoría a modificar:");
                                        long idSeleccionado = leerLong(scanner);
                                        Categoria categoriaExistente = categoriaRepository.buscarPorId(idSeleccionado).orElse(null);
                                        if (categoriaExistente == null || categoriaExistente.isEliminado()) {
                                            System.out.println("La categoría seleccionada no existe o ha sido eliminada.");
                                        } else {
                                            System.out.println("Ingrese el nuevo nombre de la categoría (actual: " + categoriaExistente.getNombre() + "):");
                                            String nuevoNombre = scanner.nextLine();
                                            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                                                categoriaExistente.setNombre(nuevoNombre);
                                            }
                                            System.out.println("Ingrese la nueva descripción de la categoría (actual: " + categoriaExistente.getDescripcion() + "):");
                                            String nuevaDescripcion = scanner.nextLine();
                                            if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
                                                categoriaExistente.setDescripcion(nuevaDescripcion);
                                            }
                                            categoriaRepository.guardar(categoriaExistente);
                                            System.out.println("Categoría modificada con éxito.");
                                        }
                                    }break;
                            case 3:
                                System.out.println("Baja lógica de categoría");
                                List<Categoria> categoriasActivas = categoriaRepository.listarActivos();
                                if (categoriasActivas.isEmpty()) {
                                    System.out.println("No hay categorías activas para eliminar.");
                                } else {
                                    for (Categoria categoria : categoriasActivas) {
                                        System.out.println("ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre() + ", Descripción: " + categoria.getDescripcion());
                                    }
                                    System.out.println("Ingrese el ID de la categoría a eliminar:");
                                    long idEliminar = leerLong(scanner);
                                    Categoria categoriaEncontrada = categoriaRepository.buscarPorId(idEliminar).orElse(null);
                                    if (categoriaEncontrada == null || categoriaEncontrada.isEliminado()) {
                                        System.out.println("La categoría seleccionada no existe o ya ha sido eliminada.");
                                        break;
                                    }
                                    int cantidadProductos = categoriaRepository.buscarProductosPorCategoria(idEliminar).size();
                                    if (cantidadProductos > 0) {
                                        System.out.println("La categoría tiene " + cantidadProductos + " productos asociados y no puede ser eliminada.");
                                    } else {
                                        if (categoriaRepository.eliminarLogico(idEliminar)) {
                                            System.out.println("Categoría " + categoriaEncontrada.getNombre() + " eliminada con éxito.");
                                        } else {
                                            System.out.println("Error al eliminar la categoría.");
                                        }
                                    }
                                }
                                break;
                            case 4:
                                System.out.println("Listado de categorías");
                                List<Categoria> categoriasListadas = categoriaRepository.listarActivos();
                                if (categoriasListadas.isEmpty()) {
                                    System.out.println("No hay categorías activas para mostrar.");
                                } else {
                                    for (Categoria categoria : categoriasListadas) {
                                        System.out.println("ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre() + ", Descripción: " + categoria.getDescripcion());
                                    }
                                }
                                break;
                            case 0:
                                System.out.println("Saliendo de la gestión de categorías.");
                                break;
                        }
                    } while (opcion_categoria != 0);
                            break;
                case 2:
                    System.out.println("Gestión de productos");
                    int opcion_producto = 0;
                    do {
                        System.out.println("\nSeleccione una opción:");
                        System.out.println("1. Crear producto");
                        System.out.println("2. Modificar producto");
                        System.out.println("3. Baja lógica de producto");
                        System.out.println("4. Listado de productos");
                        System.out.println("0. Volver al menu principal");
                        opcion_producto = leerEntero(scanner);
                        switch (opcion_producto) {
                            case 1:
                                List<Categoria> categoriasActivas = categoriaRepository.listarActivos();
                                if (categoriasActivas.isEmpty()) {
                                    System.out.println("No hay categorías activas. Debe crear una categoría antes de agregar productos.");
                                    break;
                                } else {
                                    System.out.println("Categorías activas:");

                                    for (Categoria categoria : categoriasActivas) {
                                        System.out.println("ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre());
                                    }
                                }
                                System.out.println("Ingrese el ID de la categoría a la que desea agregar el producto:");
                                long idCategoria = leerLong(scanner);
                                if (categoriasActivas.stream()
                                        .noneMatch(c -> c.getId().equals(idCategoria))) {
                                    System.out.println("La categoría seleccionada no existe o ha sido eliminada.");
                                    break;
                                }
                                Producto productoNuevo = new Producto();
                                System.out.println("Ingrese el nombre del producto:");
                                String nombreProducto = scanner.nextLine();
                                while (nombreProducto == null || nombreProducto.trim().isEmpty()) {
                                    System.out.println("El nombre del producto no puede estar vacío.");
                                    nombreProducto = scanner.nextLine();
                                }
                                productoNuevo.setNombre(nombreProducto);
                                System.out.println("Ingrese la descripción del producto:");
                                String descripcionProducto = scanner.nextLine();
                                if (!descripcionProducto.trim().isEmpty()) {
                                    productoNuevo.setDescripcion(descripcionProducto);
                                }
                                System.out.println("Ingrese el precio del producto: ");
                                String precioIngresado = scanner.nextLine();
                                try{
                                    double precioProducto = Double.parseDouble(precioIngresado);
                                    if(precioProducto > 0){
                                        productoNuevo.setPrecio(precioProducto);
                                    } else {
                                        System.out.println("El precio del producto debe ser mayor a cero.");
                                        break;
                                    }
                                }catch (NumberFormatException e) {
                                    System.out.println("Precio inválido. Debe ingresar un número.");
                                    break;
                                }
                                System.out.println("Ingrese el stock del producto: ");
                                int stockProducto = leerEntero(scanner);
                                if (stockProducto < 0) {
                                    System.out.println("El stock del producto no puede ser negativo.");
                                    break;
                                } else {
                                    productoNuevo.setStock(stockProducto);
                                }
                                System.out.println("Ingrese la imagen del producto: ");
                                String imagenProducto = scanner.nextLine();
                                productoNuevo.setImagen(imagenProducto);
                                 System.out.println("Ingrese la disponibilidad del producto (s/n): ");
                                 String disponibilidadRecibida = scanner.nextLine().toLowerCase();
                                 if (disponibilidadRecibida.equals("s") || disponibilidadRecibida.trim().isEmpty()) { //&& disponibilidadRecibida.trim().isEmpty()){
                                     productoNuevo.setDisponible(true);
                                 } else if (disponibilidadRecibida.equals("n")) {
                                     productoNuevo.setDisponible(false);
                                 } else {
                                     System.out.println("Opción inválida para disponibilidad. Se establecerá como disponible por defecto 's'.");
                                     productoNuevo.setDisponible(true);
                                 }
                                 Categoria categoriaSeleccionada = categoriaRepository.buscarPorId(idCategoria).orElse(null);
                                 Producto productoGuardado = categoriaRepository.agregarProducto(idCategoria, productoNuevo);
                                 System.out.println("Producto creado con éxito ID: " + productoGuardado.getId() + "\nCategoría: " + categoriaSeleccionada.getNombre());
                                 break;
                            case 2:
                                List<Producto> productos = productoRepository.listarActivos();
                                System.out.println("\nModificar producto");
                                if (productos.isEmpty()) {
                                    System.out.println("No hay productos activos para modificar.");
                                    break;
                                } else {
                                    for (Producto producto : productos) {
                                        System.out.println("ID: " + producto.getId() +
                                                ", Nombre: " + producto.getNombre() +
                                                ", Descripción: " + producto.getDescripcion() +
                                                ", Precio: " + producto.getPrecio() +
                                                ", Stock: " + producto.getStock() +
                                                ", Imagen: " + producto.getImagen() +
                                                ", Disponible: " + (producto.isDisponible()));
                                    }
                                    System.out.println("Ingrese el ID del producto a modificar:");
                                    long idSeleccionado = leerLong(scanner);
                                    Producto productoExistente = productoRepository.buscarPorId(idSeleccionado).orElse(null);
                                    if (productoExistente == null || productoExistente.isEliminado()) {
                                        System.out.println("El producto seleccionado no existe o ha sido eliminado.");
                                    } else {
                                        System.out.println("Ingrese el nuevo nombre del producto (actual: " + productoExistente.getNombre() + "):");
                                        String nuevoNombre = scanner.nextLine();
                                        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                                            productoExistente.setNombre(nuevoNombre);
                                        }
                                        System.out.println("Ingrese el nuevo precio del producto o Enter para mantener el actual (actual: " + productoExistente.getPrecio() + "):");
                                        String nuevoPrecioIngresado = scanner.nextLine();
                                        if (!nuevoPrecioIngresado.isEmpty()) {
                                            try {
                                                double nuevoPrecio = Double.parseDouble(nuevoPrecioIngresado);
                                                if (nuevoPrecio > 0) {
                                                    productoExistente.setPrecio(nuevoPrecio);
                                                } else {
                                                    System.out.println("El precio debe ser mayor a cero. Se mantendrá el precio actual.");
                                                }
                                            } catch (NumberFormatException e) {
                                                System.out.println("Precio inválido. Se mantendrá el precio actual.");
                                            }
                                        }
                                        System.out.println("Ingrese el nuevo stock del producto (actual: " + productoExistente.getStock() + "):");
                                        String stockIngresado = scanner.nextLine();
                                        if (!stockIngresado.isEmpty()) {
                                            try {
                                                int nuevoStock = Integer.parseInt(stockIngresado);
                                                if (nuevoStock >= 0) {
                                                    productoExistente.setStock(nuevoStock);
                                                } else {
                                                    System.out.println("El stock debe ser igual o mayor a cero. Se mantendrá el stock actual.");
                                                }
                                            } catch (NumberFormatException e) {
                                                System.out.println("Stock inválido. Se mantendrá el stock actual " + productoExistente.getStock());
                                            }
                                        }
                                        productoRepository.guardar(productoExistente);
                                        System.out.println("Producto " + productoExistente.getNombre() + " modificado con éxito.");
                                    }
                                }
                                break;
                            case 3:
                                System.out.println("\nBaja lógica de producto");
                                List<Producto> productosActivos = productoRepository.listarActivos();
                                if (productosActivos.isEmpty()) {
                                    System.out.println("No hay productos activos para eliminar.");
                                } else {
                                    for (Producto producto : productosActivos) {
                                        System.out.println("ID: " + producto.getId() + ", Nombre: " + producto.getNombre() + ", Descripción: " + producto.getDescripcion());
                                    }
                                    System.out.println("Ingrese el ID del producto a eliminar:");
                                    long idEliminar = leerEntero(scanner);
                                    Producto productoEncontrado = productoRepository.buscarPorId(idEliminar).orElse(null);
                                    if (productoEncontrado == null || productoEncontrado.isEliminado()) {
                                        System.out.println("El producto seleccionado no existe o ya ha sido eliminado.");
                                        break;
                                    }
                                    if (productoRepository.eliminarLogico(idEliminar)) {
                                        System.out.println("Producto " + productoEncontrado.getNombre() + " eliminado con éxito.");
                                    } else {
                                        System.out.println("Error al eliminar el producto.");
                                    }
                                }
                                break;
                            case 4:
                                System.out.println("\nListado de productos");

                                List<Categoria> categoriasListadas =
                                        categoriaRepository.listarActivos();

                                if (categoriasListadas.isEmpty()) {
                                    System.out.println("No hay categorías activas.");
                                } else {
                                    for (Categoria categoria : categoriasListadas) {

                                        List<Producto> productosListados =
                                                categoriaRepository.buscarProductosPorCategoria(categoria.getId());

                                        System.out.println("Categoría: " + categoria.getNombre());

                                        if (productosListados.isEmpty()) {
                                            System.out.println("No hay productos activos en esta categoría.");
                                        } else {
                                            for (Producto producto : productosListados) {
                                                System.out.println(
                                                        "ID: " + producto.getId() +
                                                                "\nNombre: " + producto.getNombre() +
                                                                "\nPrecio: " + producto.getPrecio() +
                                                                "\nStock: " + producto.getStock() +
                                                                "\nDisponible: " + producto.isDisponible()
                                                );
                                            }
                                        }
                                    }
                                }
                                break;
                            case 0:
                                System.out.println("Saliendo de la gestión de productos.");
                                break;
                        }
                    } while (opcion_producto != 0) ;
                    break;
                case 3:
                    System.out.println("Reportes");
                    int opcion_reporte = 0;
                    System.out.println("Seleccione una opción:");
                    System.out.println("1. Listado de productos por categoría");
                    System.out.println("0. Volver al menu principal");
                    opcion_reporte = leerEntero(scanner);
                    if (opcion_reporte == 1) {
                        List<Categoria> categoriasActivas = categoriaRepository.listarActivos();
                        System.out.println("Listado de categorías activas:");
                        for (Categoria categoria : categoriasActivas) {
                            System.out.println("ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre());
                        }
                        if (categoriasActivas.isEmpty()) {
                            System.out.println("No hay categorías activas para mostrar productos.");
                            break;
                        } else {
                            System.out.println("Seleccione el ID de la categoría para ver sus productos:");
                            long idCategoria = leerLong(scanner);
                            Categoria idCategoriaSeleccionada = categoriaRepository.buscarPorId(idCategoria).orElse(null);
                            if (idCategoriaSeleccionada == null || idCategoriaSeleccionada.isEliminado()) {
                                System.out.println("La categoría seleccionada no existe o ha sido eliminada.");
                                break;
                            } else {
                                List<Producto> productos = categoriaRepository.buscarProductosPorCategoria(idCategoria);
                                if (productos.isEmpty()) {
                                    System.out.println("No hay productos en esta categoría.");
                                } else {
                                    for (Producto producto : productos) {
                                        System.out.println("ID: " + producto.getId() +
                                                "\nNombre: " + producto.getNombre() +
                                                "\nPrecio: " + producto.getPrecio() +
                                                "\nStock: " + producto.getStock());
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
                        break;
            }
        }while(opcion !=0);
    }
    public static long leerLong(Scanner scanner) {
        do {
            if (scanner.hasNextLong()) {
                long opcion = scanner.nextLong();
                scanner.nextLine();
                return opcion;
            } else {
                System.out.println("Opción inválida. Ingrese un número.");
                scanner.nextLine();
            }
        } while (true);
    }

    public static int leerEntero(Scanner scanner) {
        do {
            if (scanner.hasNextInt()) {
                int numero = scanner.nextInt();
                scanner.nextLine();
                return numero;
            } else {
                System.out.println("Ingrese un número entero válido.");
                scanner.nextLine();
            }
        } while (true);
    }

}
