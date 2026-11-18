package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    private static final Logger HIBERNATE_LOGGER = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        HIBERNATE_LOGGER.setLevel(Level.SEVERE);
        CategoriaRepository categoriaRepository = new CategoriaRepository();
        ProductoRepository productoRepository = new ProductoRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al sistema de gestión de productos y categorías.");

        int opcion = 0;
        do {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Gestión de Usuarios");
            System.out.println("4.Gestión de Pedidos");
            System.out.println("5. Reportes");
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
                                while (nombreCategoria.trim().isEmpty()) {
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
                                }
                                break;
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
                                try {
                                    double precioProducto = Double.parseDouble(precioIngresado);
                                    if (precioProducto > 0) {
                                        productoNuevo.setPrecio(precioProducto);
                                    } else {
                                        System.out.println("El precio del producto debe ser mayor a cero.");
                                        break;
                                    }
                                } catch (NumberFormatException e) {
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
                    } while (opcion_producto != 0);
                    break;
                case 3:
                    System.out.println("Gestión de usuarios");
                    int opcion_usuario = 0;
                    do {
                        System.out.println("Seleccione una opción:");
                        System.out.println("1. Crear usuario");
                        System.out.println("2. Modificar usuario");
                        System.out.println("3. Baja lógica de usuario");
                        System.out.println("4. Listado de usuarios");
                        System.out.println("5. Buscar usuario por mail");
                        System.out.println("0. Volver al menu principal");
                        opcion_usuario = leerEntero(scanner);
                        switch (opcion_usuario){
                            case 1:
                                System.out.println("Crear usuario");
                                System.out.println("Ingrese el nombre del usuario:");
                                String nombreUsuario = scanner.nextLine().trim();
                                while (nombreUsuario.trim().isEmpty()) {
                                    System.out.println("El nombre del usuario no puede estar vacío. Por favor, ingrese un nombre válido.");
                                    nombreUsuario = scanner.nextLine().trim();
                                }

                                System.out.println("Ingrese el apellido del usuario:");
                                String apellidoUsuario = scanner.nextLine();
                                while (apellidoUsuario.trim().isEmpty()) {
                                    System.out.println("El apellido del usuario no puede estar vacío. Por favor, ingrese un apellido válido.");
                                    apellidoUsuario = scanner.nextLine().trim();
                                }

                                System.out.println("Ingrese el mail del usuario:");
                                String mailUsuario = scanner.nextLine().trim();
                                while (mailUsuario.trim().isEmpty() || usuarioRepository.buscarPorMail(mailUsuario).isPresent()) {
                                    if(mailUsuario.isEmpty()) {
                                        System.out.println("El mail del usuario no puede estar vacío. Por favor, ingrese un mail válido: ");
                                    }else {
                                        System.out.println("Ese mail ya está registrado. Ingrese otro: ");
                                    }
                                    mailUsuario = scanner.nextLine().trim();
                                }

                                System.out.println("Ingrese el celular del usuario:");
                                String celularUsuario = scanner.nextLine();

                                System.out.println("Ingrese la contraseña del usuario:");
                                String contrasenaUsuario = scanner.nextLine();
                                while (contrasenaUsuario.trim().isEmpty()) {
                                    System.out.println("La contraseña del usuario no puede estar vacía. Por favor, ingrese una contraseña válida.");
                                    contrasenaUsuario = scanner.nextLine().trim();
                                }
                                System.out.println("Ingrese el rol del usuario (ADMIN/USUARIO):");
                                String rolUsuario = scanner.nextLine().trim();
                                while (!rolUsuario.equalsIgnoreCase("ADMIN") && !rolUsuario.equalsIgnoreCase("USUARIO")) {
                                    System.out.println("Rol inválido. Por favor, ingrese 'ADMIN' o 'USUARIO'.");
                                    rolUsuario = scanner.nextLine().trim();
                                }
                                Rol rol = Rol.valueOf(rolUsuario.toUpperCase(Locale.ROOT));
                                Usuario usuarioNuevo = new Usuario(nombreUsuario, apellidoUsuario, mailUsuario, celularUsuario, contrasenaUsuario, rol);
                                Usuario usuarioGuardado = usuarioRepository.guardar(usuarioNuevo);
                                System.out.println("Usuario creado con ID: " + usuarioGuardado.getId());
                                break;
                            case 2:
                                System.out.println("Modificar usuario");
                                List<Usuario> usuarios = usuarioRepository.listarActivos();
                                if (usuarios.isEmpty()) {
                                    System.out.println("No hay usuarios activos para modificar.");
                                } else {
                                    for (Usuario usuario : usuarios) {
                                        System.out.println("ID: " + usuario.getId() +
                                                ", Nombre: " + usuario.getNombre() +
                                                ", Apellido: " + usuario.getApellido() +
                                                ", Mail: " + usuario.getMail() +
                                                ", Celular: " + usuario.getCelular() +
                                                ", Rol: " + usuario.getRol());
                                    }
                                    System.out.println("Ingrese el ID del usuario a modificar:");
                                    long idSeleccionado = leerLong(scanner);
                                    Usuario usuarioExistente = usuarioRepository.buscarPorId(idSeleccionado).orElse(null);
                                    if (usuarioExistente == null || usuarioExistente.isEliminado()) {
                                        System.out.println("El usuario seleccionado no existe o ha sido eliminado.");
                                    } else {
                                        System.out.println("Ingrese el nuevo nombre del usuario (actual: " + usuarioExistente.getNombre() + "):");
                                        String nuevoNombre = scanner.nextLine();
                                        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                                            usuarioExistente.setNombre(nuevoNombre);
                                        }

                                        System.out.println("Ingrese el nuevo apellido del usuario (actual: " + usuarioExistente.getApellido() + "):");
                                        String nuevoApellido = scanner.nextLine();
                                        if (nuevoApellido != null && !nuevoApellido.trim().isEmpty()) {
                                            usuarioExistente.setApellido(nuevoApellido);
                                        }

                                        System.out.println("Ingrese el nuevo mail del usuario (actual: " + usuarioExistente.getMail() + "):");
                                        String nuevoMail = scanner.nextLine().trim();
                                        if (!nuevoMail.isEmpty()) {
                                            Optional<Usuario> encontrado = usuarioRepository.buscarPorMail(nuevoMail);

                                            if (encontrado.isPresent()
                                                    && !encontrado.get().getId().equals(usuarioExistente.getId())) {
                                                System.out.println("Ese mail pertenece a otro usuario. Se conserva el actual.");
                                            } else {
                                                usuarioExistente.setMail(nuevoMail);
                                            }
                                        }

                                        System.out.println("Ingrese el nuevo celular del usuario (actual: " + usuarioExistente.getCelular() + "):");
                                        String nuevoCelular = scanner.nextLine();
                                        if (nuevoCelular != null && !nuevoCelular.trim().isEmpty()) {
                                            usuarioExistente.setCelular(nuevoCelular);
                                        }

                                        System.out.println("Ingrese la nueva contraseña o presione Enter para conservarla: ");
                                        String nuevaContrasena = scanner.nextLine();
                                        if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                                            usuarioExistente.setContrasena(nuevaContrasena);
                                        }

                                        usuarioRepository.guardar(usuarioExistente);
                                        System.out.println("Usuario modificado con éxito.");
                                    }
                                }
                                break;
                            case 3:
                                System.out.println("Baja lógica de usuario");
                                List<Usuario> usuariosActivos = usuarioRepository.listarActivos();
                                if (usuariosActivos.isEmpty()){
                                    System.out.println("No hay usuarios activos.");
                                } else {
                                    for (Usuario usuario : usuariosActivos) {
                                        System.out.println("ID: " + usuario.getId() +
                                                "\nNombre completo: " + usuario.getNombre() + " " + usuario.getApellido() +
                                                "\nMail: " + usuario.getMail());
                                    }
                                    System.out.println("Ingrese el ID del usuario a eliminar:");
                                    long idEliminar = leerLong(scanner);
                                    Usuario usuarioEncontrado = usuarioRepository.buscarPorId(idEliminar).orElse(null);
                                    if (usuarioRepository.eliminarLogico(idEliminar)) {
                                        System.out.println("Usuario " + usuarioEncontrado.getNombre() + " "
                                                + usuarioEncontrado.getApellido() + " dado de baja con éxito.");
                                    } else {
                                        System.out.println("El usuario ya no está activo.");
                                    }
                                }
                                break;
                            case 4:
                                System.out.println("Listado de usuarios");
                                List<Usuario> usuariosListados = usuarioRepository.listarActivos();
                                if (usuariosListados.isEmpty()) {
                                    System.out.println("No hay usuarios activos para mostrar.");
                                } else {
                                    for (Usuario usuario : usuariosListados) {
                                        System.out.println("ID: " + usuario.getId() +
                                                "\nNombre completo: " + usuario.getNombre() + " " + usuario.getApellido() +
                                                "\nMail: " + usuario.getMail() +
                                                "\nRol: " + usuario.getRol());
                                    }
                                }
                                break;
                            case 5:
                                System.out.println("Buscar usuario por mail");
                                System.out.println("Ingrese el mail del usuario a buscar: ");
                                String mailBuscar = scanner.nextLine().trim();
                                Optional<Usuario> usuarioEncontrado = usuarioRepository.buscarPorMail(mailBuscar);
                                if (usuarioEncontrado.isPresent()) {
                                    Usuario usuario = usuarioEncontrado.get();
                                    System.out.println("\nUsuario encontrado: " +
                                            "\nID: " + usuario.getId() +
                                            "\nNombre completo: "+ usuario.getNombre() + " " + usuario.getApellido() +
                                            "\nMail: " + usuario.getMail() +
                                            "\nCelular: " + usuario.getCelular() +
                                            "\nRol: " + usuario.getRol());
                                } else {
                                    System.out.println("Usuario no encontrado.");
                                }
                                break;
                            case 0:
                                System.out.println("Saliendo de la gestión de usuarios.");
                                break;
                        }
                    } while (opcion_usuario != 0);
                        break;
                        case 4:
                            System.out.println("\nGestión de pedidos");
                            int opcion_pedido = 0;
                            do {
                                System.out.println("\nSeleccione una opción:");
                                System.out.println("1. Alta de pedidos");
                                System.out.println("2. Modificar estado de pedido");
                                System.out.println("3. Baja lógica de pedido");
                                System.out.println("4. Listado de pedidos");
                                System.out.println("5. Listado de pedidos por usuario");
                                System.out.println("6. Listado de pedidos por estado");
                                System.out.println("0. Volver al menu principal");
                                opcion_pedido = leerEntero(scanner);
                                switch (opcion_pedido) {
                                    case 1:
                                        System.out.println("Alta de pedidos");
                                        try {
                                            List<Usuario> usuariosActivos = usuarioRepository.listarActivos();
                                            if (usuariosActivos.isEmpty()) {
                                                System.out.println("No hay usuarios activos.");
                                            } else {
                                                for (Usuario usuario : usuariosActivos) {
                                                    System.out.println("ID: " + usuario.getId() +
                                                            "\nNombre completo: " + usuario.getNombre() + " " + usuario.getApellido());
                                                }
                                                System.out.println("Ingrese el ID del usuario: ");
                                                long idEliminar = leerLong(scanner);
                                                Usuario usuarioEncontrado = usuarioRepository.buscarPorId(idEliminar).orElse(null);
                                                if (usuarioEncontrado == null || usuarioEncontrado.isEliminado()) {
                                                    System.out.println("El usuario seleccionado no existe o ha sido eliminado.");
                                                } else {
                                                    System.out.println("Usuario encontrado: " + usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellido());
                                                    FormaPago pago = null;
                                                    while (pago == null) {
                                                        System.out.println("Ingrese la forma de pago (EFECTIVO, TARJETA, TRANSFERENCIA):");
                                                        String entrada = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

                                                        if (entrada.length() >= 3) {
                                                            if ("EFECTIVO".startsWith(entrada)) {
                                                                pago = FormaPago.EFECTIVO;
                                                            } else if ("TARJETA".startsWith(entrada)) {
                                                                pago = FormaPago.TARJETA;
                                                            } else if ("TRANSFERENCIA".startsWith(entrada)) {
                                                                pago = FormaPago.TRANSFERENCIA;
                                                            }
                                                        }

                                                        if (pago == null) {
                                                            System.out.println("Forma de pago inválida. Ingrese al menos tres letras del comienzo de una opción.");
                                                        }
                                                    }
                                                    System.out.println("Listado de productos disponibles: ");
                                                    List<Producto> productosDisponibles = productoRepository.listarActivos();
                                                    if (productosDisponibles.isEmpty()) {
                                                        System.out.println("No hay productos disponibles.");
                                                    } else {
                                                        for (Producto producto : productosDisponibles) {
                                                            System.out.println("ID: " + producto.getId() +
                                                                    "\nNombre: " + producto.getNombre() +
                                                                    "\nPrecio: " + producto.getPrecio() +
                                                                    "\nStock: " + producto.getStock());
                                                        }
                                                    }
                                                    System.out.println("Desea agregar productos al pedido? (s/n): ");
                                                    String respuesta = scanner.nextLine().trim().toLowerCase();
                                                    while (!respuesta.equals("s") && !respuesta.equals("n")){
                                                        System.out.println("Respuesta inválida. Ingrese 's' para sí o 'n' para no: ");
                                                        respuesta = scanner.nextLine().trim().toLowerCase();
                                                    }
                                                    Map<Long, Integer> cantidadesPorProducto = new LinkedHashMap<>();
                                                    while (respuesta.equals("s")) {
                                                        System.out.println("Ingrese el ID del producto a agregar al pedido: ");
                                                        long idProducto = leerLong(scanner);
                                                        Producto productoEncontrado = productoRepository.buscarPorId(idProducto).orElse(null);
                                                        if (productoEncontrado == null || productoEncontrado.isEliminado()) {
                                                            System.out.println("El producto seleccionado no existe o ha sido eliminado.");
                                                        } else if (!productoEncontrado.isDisponible()) {
                                                            System.out.println("El producto seleccionado no está disponible.");
                                                        } else {
                                                            System.out.println("Producto encontrado: " + productoEncontrado.getNombre());
                                                            System.out.println("Ingrese la cantidad del producto: ");
                                                            int cantidad = leerEntero(scanner);
                                                            int cantidadYaElegida = cantidadesPorProducto.getOrDefault(idProducto, 0);
                                                            int stockRestante = productoEncontrado.getStock() - cantidadYaElegida;
                                                            if (cantidad <= 0) {
                                                                System.out.println("La cantidad debe ser mayor a cero.");
                                                            } else if (cantidad > stockRestante) {
                                                                System.out.println("Podés agregar hasta " + stockRestante + " unidades más.");
                                                            } else {
                                                                cantidadesPorProducto.put(idProducto, cantidadYaElegida + cantidad);
                                                            }
                                                        }
                                                        System.out.println("Desea agregar productos al pedido? (s/n): ");
                                                        respuesta = scanner.nextLine().trim().toLowerCase();
                                                        while (!respuesta.equals("s") && !respuesta.equals("n")){
                                                            System.out.println("Respuesta inválida. Ingrese 's' para sí o 'n' para no: ");
                                                            respuesta = scanner.nextLine().trim().toLowerCase();
                                                        }
                                                    }

                                                    if (cantidadesPorProducto.isEmpty()) {
                                                        System.out.println("El pedido debe tener al menos un producto. Se canceló el alta.");
                                                        break;
                                                    }
                                                    System.out.println("Creando pedido...");
                                                    Pedido pedidoNuevo = pedidoRepository.crearPedido(usuarioEncontrado.getId(), pago, cantidadesPorProducto);
                                                    System.out.println("Pedido creado con éxito." +
                                                            "\nID del pedido: " + pedidoNuevo.getId() +
                                                            "\nFecha: " + pedidoNuevo.getFecha() +
                                                            "\nUsuario: " + usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellido() +
                                                            "\nForma de pago: " + pedidoNuevo.getFormaPago() +
                                                            "\nListado de productos: \n" + pedidoNuevo.getDetalles().stream()
                                                            .map(detalle -> detalle.getProducto().getNombre() +
                                                                    "\nCantidad: " + detalle.getCantidad() +
                                                                    "\nSubtotal: " + detalle.getSubtotal())
                                                            .collect(Collectors.joining("\n")) +
                                                            "\nTotal: " + pedidoNuevo.getTotal());
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Error al dar de alta el pedido: " + e.getMessage());
                                        }
                                        break;
                                    case 2:
                                        System.out.println("Modificar estado de pedido");
                                        List<Pedido> pedidosActivos = pedidoRepository.listarActivos();
                                        if (pedidosActivos.isEmpty()) {
                                            System.out.println("No hay pedidos activos para modificar.");
                                        } else {
                                            for (Pedido pedido : pedidosActivos) {
                                                System.out.println("ID: " + pedido.getId() +
                                                        "\nEstado: " + pedido.getEstado());
                                            }
                                        }
                                        System.out.println("Ingrese el ID del pedido a modificar: ");
                                        long idPedido = leerLong(scanner);
                                        Pedido pedidoExistente = pedidoRepository.buscarPorId(idPedido).orElse(null);
                                        if (pedidoExistente == null || pedidoExistente.isEliminado()) {
                                            System.out.println("El pedido seleccionado no existe o ha sido dado de baja.");
                                        } else {
                                            System.out.println("Pedido encontrado con ID: " + pedidoExistente.getId() +
                                                    "\nEstado actual: " + pedidoExistente.getEstado());
                                            Estado estado = null;
                                            while (estado == null) {
                                                System.out.println("Ingrese el nuevo estado del pedido (PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO): ");
                                                String nuevoEstado = scanner.nextLine().trim().toUpperCase();

                                                if (nuevoEstado.length() >= 3) {
                                                    if ("PENDIENTE".startsWith(nuevoEstado)) {
                                                        estado = Estado.PENDIENTE;
                                                    } else if ("CONFIRMADO".startsWith(nuevoEstado)) {
                                                        estado = Estado.CONFIRMADO;
                                                    } else if ("TERMINADO".startsWith(nuevoEstado)) {
                                                        estado = Estado.TERMINADO;
                                                    } else if ("CANCELADO".startsWith(nuevoEstado)) {
                                                        estado = Estado.CANCELADO;
                                                    }
                                                }

                                                if (estado == null) {
                                                    System.out.println("Estado inválido. Ingrese al menos tres letras del comienzo de una opción.");
                                                }
                                            }

                                            pedidoExistente.setEstado(estado);
                                            pedidoRepository.guardar(pedidoExistente);
                                            System.out.println("Estado del pedido modificado con éxito: " +
                                                    "\nID: "  + pedidoExistente.getId() +
                                                    "\nNuevo estado: " + pedidoExistente.getEstado());

                                        }
                                        break;
                                    case 3:
                                        System.out.println("\nBaja lógica de pedido\n");
                                        List<Pedido> pedidosActivosID = pedidoRepository.listarActivos();
                                        if (pedidosActivosID.isEmpty()) {
                                            System.out.println("No hay pedidos activos para dar de baja.");
                                        } else {
                                            for (Pedido pedido : pedidosActivosID) {
                                                System.out.println("ID: " + pedido.getId() +
                                                        "\nEstado: " + pedido.getEstado());
                                            }
                                            System.out.println("Ingrese el ID del pedido a dar de baja: ");
                                            long idPedidoBaja = leerLong(scanner);
                                            Pedido pedidoExistenteBaja = pedidoRepository.buscarPorId(idPedidoBaja).orElse(null);
                                            if (pedidoExistenteBaja == null || pedidoExistenteBaja.isEliminado()) {
                                                System.out.println("El pedido seleccionado no existe o ya ha sido dado de baja.");
                                            } else {
                                                if (pedidoRepository.eliminarLogico(idPedidoBaja)) {
                                                    System.out.println("Pedido con ID: " + pedidoExistenteBaja.getId() + " dado de baja con éxito."+
                                                            "\nTotal del pedido: " + pedidoExistenteBaja.getTotal());
                                                } else {
                                                    System.out.println("Error al dar de baja el pedido.");
                                                }
                                            }
                                        }
                                        break;
                                    case 4:
                                        System.out.println("\nListado de pedidos\n");
                                        List<Pedido> pedidosListados = pedidoRepository.listarActivos();
                                        if (pedidosListados.isEmpty()) {
                                            System.out.println("No hay pedidos activos para mostrar.");
                                        } else {
                                            for (Pedido pedido : pedidosListados) {
                                                System.out.println("\nID: " + pedido.getId() +
                                                        "\nFecha: " + pedido.getFecha() +
                                                        "\nEstado: " + pedido.getEstado() +
                                                        "\nForma de pago: " + pedido.getFormaPago() +
                                                        "\nTotal: " + pedido.getTotal());
                                            }
                                        }
                                        break;
                                    case 5:
                                        System.out.println("\nListado de pedidos por usuario\n");
                                        List<Usuario> usuariosActivos = usuarioRepository.listarActivos();
                                        if (usuariosActivos.isEmpty()) {
                                            System.out.println("No hay usuarios activos para mostrar.");
                                        } else {
                                            System.out.println("Listado de usuarios activos:");
                                            for (Usuario usuario : usuariosActivos) {
                                                System.out.println("ID: " + usuario.getId() +
                                                        "\nNombre completo: " + usuario.getNombre() + " " + usuario.getApellido());
                                            }
                                            System.out.println("Ingrese el ID del usuario para ver sus pedidos:");
                                            long idUsuario = leerLong(scanner);
                                            Usuario usuarioSeleccionado = usuarioRepository.buscarPorId(idUsuario).orElse(null);
                                            if (usuarioSeleccionado == null || usuarioSeleccionado.isEliminado()) {
                                                System.out.println("El usuario seleccionado no existe o ha sido dado de baja.");
                                            } else {
                                                List<Pedido> pedidosPorUsuario = usuarioRepository.buscarPedidosPorUsuario(idUsuario);
                                                if (pedidosPorUsuario.isEmpty()) {
                                                    System.out.println("El usuario no tiene pedidos activos.");
                                                } else {
                                                    System.out.println("Pedidos del usuario " + usuarioSeleccionado.getNombre() + " "+ usuarioSeleccionado.getApellido() + ":");
                                                    for (Pedido pedido : pedidosPorUsuario) {
                                                    System.out.println("\nID: " + pedido.getId() +
                                                            "\nFecha: " + pedido.getFecha() +
                                                            "\nEstado: " + pedido.getEstado() +
                                                            "\nTotal: " + pedido.getTotal() +
                                                            "\nForma de pago: " + pedido.getFormaPago() );
                                                    }

                                                }
                                            }
                                        }
                                        break;
                                    case 6:
                                        System.out.println("\nListado de pedidos por estado\n");
                                        System.out.println("Ingrese el estado de los pedidos a listar (PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO): ");
                                        Estado estadoPedido = null;
                                        while (estadoPedido == null) {
                                            String entradaPedido = scanner.nextLine().trim().toUpperCase();
                                            if (entradaPedido.length() >= 3) {
                                                if ("PENDIENTE".startsWith(entradaPedido)) {
                                                    estadoPedido = Estado.PENDIENTE;
                                                } else if ("CONFIRMADO".startsWith(entradaPedido)) {
                                                    estadoPedido = Estado.CONFIRMADO;
                                                } else if ("TERMINADO".startsWith(entradaPedido)) {
                                                    estadoPedido = Estado.TERMINADO;
                                                } else if ("CANCELADO".startsWith(entradaPedido)) {
                                                    estadoPedido = Estado.CANCELADO;
                                                }
                                            }
                                            if (estadoPedido == null) {
                                                System.out.println("\nEstado inválido. Ingrese al menos tres letras del comienzo de una opción.\n");
                                            }
                                        }

                                        List<Pedido> pedidosPorEstado =
                                                pedidoRepository.buscarPorEstado(estadoPedido);

                                        if (pedidosPorEstado.isEmpty()) {
                                            System.out.println("\nNo hay pedidos activos con estado " + estadoPedido + ".");
                                        } else {
                                            System.out.println("\nPedidos con estado " + estadoPedido + ":");

                                            for (Pedido pedido : pedidosPorEstado) {
                                                System.out.println("\nID: " + pedido.getId()
                                                        + "\nFecha: " + pedido.getFecha()
                                                        + "\nForma de pago: " + pedido.getFormaPago()
                                                        + "\nTotal: " + pedido.getTotal());
                                            }
                                        }
                                        break;
                                    case 0:
                                        System.out.println("\nSaliendo de la gestión de pedidos.\n");
                                        break;
                                }
                            } while (opcion_pedido != 0);
                            break;
                        case 5:
                            System.out.println("\nReportes");
                            int opcion_reporte = 0;
                            System.out.println("\nSeleccione una opción:");
                            System.out.println("1. Listado de productos por categoría");
                            System.out.println("2. Listado de pedidos por usuario");
                            System.out.println("3. Listado de pedidos por estado");
                            System.out.println("4. Total facturado");
                            System.out.println("0. Volver al menu principal");
                            opcion_reporte = leerEntero(scanner);
                            switch (opcion_reporte) {
                                case 1:
                                    System.out.println("Listado de productos por categoría");
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
                                    break;
                                case 2:
                                    System.out.println("\nPedidos por usuario\n");
                                    List<Usuario> usuariosActivos = usuarioRepository.listarActivos();
                                    if (usuariosActivos.isEmpty()) {
                                        System.out.println("No hay usuarios activos para mostrar.");
                                    } else {
                                        System.out.println("Listado de usuarios activos:");
                                        for (Usuario usuario : usuariosActivos) {
                                            System.out.println("ID: " + usuario.getId() +
                                                    "\nNombre completo: " + usuario.getNombre() + " " + usuario.getApellido());
                                        }
                                        System.out.println("Ingrese el ID del usuario para ver sus pedidos:");
                                        long idUsuario = leerLong(scanner);
                                        Usuario usuarioSeleccionado = usuarioRepository.buscarPorId(idUsuario).orElse(null);
                                        if (usuarioSeleccionado == null || usuarioSeleccionado.isEliminado()) {
                                            System.out.println("El usuario seleccionado no existe o ha sido dado de baja.");
                                        } else {
                                            List<Pedido> pedidosPorUsuario = usuarioRepository.buscarPedidosPorUsuario(idUsuario);
                                            if (pedidosPorUsuario.isEmpty()) {
                                                System.out.println("El usuario no tiene pedidos activos.");
                                            } else {
                                                System.out.println("Pedidos del usuario " + usuarioSeleccionado.getNombre() + " "+ usuarioSeleccionado.getApellido() + ":");
                                                for (Pedido pedido : pedidosPorUsuario) {
                                                    System.out.println("\nID: " + pedido.getId() +
                                                            "\nFecha: " + pedido.getFecha() +
                                                            "\nEstado: " + pedido.getEstado() +
                                                            "\nTotal: " + pedido.getTotal() +
                                                            "\nForma de pago: " + pedido.getFormaPago() );
                                                }

                                            }
                                        }
                                    }
                                    break;
                                case 3:
                                    System.out.println("\nPedidos por estado\n");
                                    System.out.println("Ingrese el estado de los pedidos a listar (PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO): ");
                                    Estado estadoPedido = null;
                                    while (estadoPedido == null) {
                                        String entradaPedido = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
                                        if (entradaPedido.length() >= 3) {
                                            if ("PENDIENTE".startsWith(entradaPedido)) {
                                                estadoPedido = Estado.PENDIENTE;
                                            } else if ("CONFIRMADO".startsWith(entradaPedido)) {
                                                estadoPedido = Estado.CONFIRMADO;
                                            } else if ("TERMINADO".startsWith(entradaPedido)) {
                                                estadoPedido = Estado.TERMINADO;
                                            } else if ("CANCELADO".startsWith(entradaPedido)) {
                                                estadoPedido = Estado.CANCELADO;
                                            }
                                        }
                                        if (estadoPedido == null) {
                                            System.out.println("\nEstado inválido. Ingrese al menos tres letras del comienzo de una opción.\n");
                                        }
                                    }

                                    List<Pedido> pedidosPorEstado =
                                            pedidoRepository.buscarPorEstado(estadoPedido);

                                    if (pedidosPorEstado.isEmpty()) {
                                        System.out.println("\nNo hay pedidos activos con estado " + estadoPedido + ".");
                                    } else {
                                        System.out.println("\nPedidos con estado " + estadoPedido + ":");

                                        for (Pedido pedido : pedidosPorEstado) {
                                            System.out.println("\nID: " + pedido.getId()
                                                    + "\nFecha: " + pedido.getFecha()
                                                    + "\nForma de pago: " + pedido.getFormaPago()
                                                    + "\nTotal: " + pedido.getTotal());
                                        }
                                    }
                                    break;
                                case 4:
                                    System.out.println("Total facturado");
                                    List<Pedido> estadoPedidos = pedidoRepository.buscarPorEstado(Estado.TERMINADO);
                                    double totalFacturado = estadoPedidos.stream()
                                            .mapToDouble(Pedido::getTotal)
                                            .sum();
                                    System.out.println("El total facturado por pedidos TERMINADOS es: " + String.format(Locale.US, "$%.2f", totalFacturado));
                                    break;
                                case 0:
                                    System.out.println("Volviendo al menú principal...");
                                    break;
                            }
                            break;
                        case 0:
                            System.out.println("Saliendo del sistema. ¡Hasta luego!");
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
                            break;
                    }
            } while (opcion != 0) ;
        JPAUtil.close();
        }
        public static long leerLong (Scanner scanner){
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

        public static int leerEntero (Scanner scanner){
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