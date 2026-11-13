package com.tp.jpa.model;

import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private double total;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido(LocalDate fecha,
                  Estado estado,
                  double total,
                  FormaPago formaPago) {
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        this.detalles = new ArrayList<>();
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void addDetallePedido(Producto producto, int cantidad){
        double subtotal = producto.getPrecio() * cantidad;
        DetallePedido nuevoDetalle = new DetallePedido(
                cantidad,
                subtotal,
                producto);
        detalles.add(nuevoDetalle);
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto){
        return detalles.stream()
                .filter(detalle -> detalle.getProducto().equals(producto))
                .findFirst()
                .orElse(null);
    }

    public void deleteDetallePedidoByProducto(Producto producto){
        detalles.removeIf(detalle -> detalle.getProducto().equals(producto));
    }

    public double calcularTotal(){
        return detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }

    public int calcularCantidadItems() {
        return detalles.stream()
                .mapToInt(DetallePedido::getCantidad)
                .sum();
    }
}