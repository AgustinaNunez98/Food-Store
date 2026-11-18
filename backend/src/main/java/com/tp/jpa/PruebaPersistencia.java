package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PruebaPersistencia {
    private static final Logger HIBERNATE_LOGGER = Logger.getLogger("org.hibernate");
    public static void main(String[] args) {
        HIBERNATE_LOGGER.setLevel(Level.SEVERE);
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Usuario u1 = new Usuario(
                    "Agustina",
                    "Nunez",
                    "agus@example.com",
                    "123456789",
                    "password1",
                    Rol.USUARIO);
            em.persist(u1);

            Usuario u2 = new Usuario(
                    "Antonella",
                    "Nuñez",
                    "anto@example.com",
                    "987654321",
                    "pasword2",
                    Rol.ADMIN);
            em.persist(u2);

            Categoria c1 = new Categoria("Zapatos", "Calzado de todo tipo");
            Categoria c2 = new Categoria("Ropa", "Prendas de vestir");
            Categoria c3 = new Categoria("Accesorios", "Complementos y accesorios de moda");
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);

            Producto p1 = new Producto(
                    "Zapato Deportivo",
                    100,
                    "Zapato cómodo para correr",
                    15,
                    "zapato.jpg",
                    true);
            Producto p2 = new Producto(
                    "Camisa Casual",
                    50,
                    "Camisa de algodón para uso diario",
                    30,
                    "camisa.jpg",
                    true);
            Producto p3 = new Producto(
                    "Pantalón Jeans",
                    80,
                    "Pantalón de mezclilla para uso diario",
                    20,
                    "pantalon.jpg",
                    true);
            Producto p4 = new Producto(
                    "Bolso de Cuero",
                    120,
                    "Bolso elegante de cuero genuino",
                    10,
                    "bolso.jpg",
                    true);
            Producto p5 = new Producto(
                    "Reloj de Pulsera",
                    200,
                    "Reloj de pulsera con correa de acero inoxidable",
                    5,
                    "reloj.jpg",
                    true);
            Producto p6 = new Producto(
                    "Sandalias de Playa",
                    40,
                    "Sandalias cómodas para la playa",
                    25,
                    "sandalias.jpg",
                    true);
            Producto p7 = new Producto(
                    "Chaqueta de Invierno",
                    150,
                    "Chaqueta acolchada para clima frío",
                    12,
                    "chaqueta.jpg",
                    true);
            Producto p8 = new Producto(
                    "Gorra de Béisbol",
                    25,
                    "Gorra ajustable con visera curva",
                    40,
                    "gorra.jpg",
                    true);
            Producto p9 = new Producto(
                    "Bufanda de Lana",
                    30,
                    "Bufanda cálida de lana para invierno",
                    18,
                    "bufanda.jpg",
                    true);
            Producto p10 = new Producto(
                    "Cinturón de Cuero",
                    35,
                    "Cinturón elegante de cuero genuino",
                    22,
                    "cinturon.jpg",
                    true);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);
            em.persist(p6);
            em.persist(p7);
            em.persist(p8);
            em.persist(p9);
            em.persist(p10);

            Categoria categoriaEncontrada = em.find(Categoria.class, c1.getId());
            categoriaEncontrada.addProducto(p1);
            categoriaEncontrada.addProducto(p6);

            Categoria categoriaEncontrada2 = em.find(Categoria.class, c2.getId());
            categoriaEncontrada2.addProducto(p2);
            categoriaEncontrada2.addProducto(p3);
            categoriaEncontrada2.addProducto(p7);

            Categoria categoriaEncontrada3 = em.find(Categoria.class, c3.getId());
            categoriaEncontrada3.addProducto(p4);
            categoriaEncontrada3.addProducto(p5);
            categoriaEncontrada3.addProducto(p8);
            categoriaEncontrada3.addProducto(p9);
            categoriaEncontrada3.addProducto(p10);

            Pedido pedido1 = new Pedido(
                    LocalDate.now(),
                    Estado.PENDIENTE,
                    0,
                    FormaPago.TARJETA);

            Pedido pedido2 = new Pedido(
                    LocalDate.now(),
                    Estado.PENDIENTE,
                    0,
                    FormaPago.EFECTIVO);

            Pedido pedido3 = new Pedido(
                    LocalDate.now(),
                    Estado.PENDIENTE,
                    0,
                    FormaPago.TARJETA);

            pedido1.addDetallePedido(p1, 2);
            pedido1.addDetallePedido(p2, 1);
            pedido1.setTotal(pedido1.calcularTotal());
            em.persist(pedido1);
            System.out.println("Total del pedido 1: " + pedido1.getTotal());

            pedido2.addDetallePedido(p3, 1);
            pedido2.addDetallePedido(p5, 2);
            pedido2.setTotal(pedido2.calcularTotal());
            em.persist(pedido2);
            System.out.println("Total del pedido 2: " + pedido2.getTotal());

            pedido3.addDetallePedido(p6, 3);
            pedido3.addDetallePedido(p7, 1);
            pedido3.setTotal(pedido3.calcularTotal());
            em.persist(pedido3);
            System.out.println("Total del pedido 3: " + pedido3.getTotal());

            p1.setStock(0);
            System.out.println("Stock actualizado del producto: " + p1.getNombre() + " - Nuevo stock: " + p1.getStock());
            p3.setPrecio(100);
            System.out.println("Precio actualizado del producto: " + p3.getNombre() + " - Nuevo precio: " + p3.getPrecio());

            Usuario usuarioEncontradoId = em.find(Usuario.class, u1.getId());
            if (usuarioEncontradoId != null) {
                System.out.println("Usuario encontrado por ID: " + usuarioEncontradoId.getId());
            } else {
                System.out.println("Usuario no encontrado por ID.");
            }
            Usuario usuarioEncontradoMail = em.createQuery("SELECT u FROM Usuario u WHERE u.mail = :mail", Usuario.class)
                    .setParameter("mail", u1.getMail())
                    .getSingleResult();

            System.out.println("Usuario encontrado por mail: " + usuarioEncontradoMail);

            Producto productoEliminado = em.find(Producto.class, p10.getId());
            em.remove(productoEliminado);
            System.out.println("Producto eliminado: " + productoEliminado.getNombre() + " (ID: " + productoEliminado.getId() + ")");

            em.getTransaction().commit();

        }
        catch (RuntimeException e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }
        finally {
            em.close();
            JPAUtil.close();
        }
    }
}