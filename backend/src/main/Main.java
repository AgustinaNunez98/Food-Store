package main;

import main.java.com.tp.jpa.model.Pedido;
import main.java.com.tp.jpa.model.Producto;
import main.java.com.tp.jpa.model.enums.Estado;
import main.java.com.tp.jpa.model.enums.FormaPago;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Producto p1 = new Producto("Hamburguesa",
                500,
                "Hamburguesa completa",
                10,
                "hamburguesa.jpg",
                true);

        Producto p2 = new Producto("Gaseosa",
                200,
                "Gaseosa de 500ml",
                3,
                "gaseosa.jpg",
                true);

        Pedido pedido = new Pedido(
                LocalDate.now(),
                Estado.PENDIENTE,
                0,
                FormaPago.EFECTIVO
        );

        pedido.addDetallePedido(p1, 2);
        pedido.addDetallePedido(p2, 1);

        System.out.println("Total: " + pedido.calcularTotal());

        List<Producto> productos = List.of(p1, p2);

        productos.stream()
                .filter(Producto::isDisponible)
                .forEach(producto
                        -> System.out.println("Producto disponible: " + producto.getNombre()));

        int cantidadItems = pedido.calcularCantidadItems();

        System.out.println("Cantidad de items en el pedido: " + cantidadItems);

        productos.stream()
                .filter(producto -> producto.getStock() < 5)
                .forEach(producto
                        -> System.out.println("Producto con stock bajo: " + producto.getNombre()));
    }
}
