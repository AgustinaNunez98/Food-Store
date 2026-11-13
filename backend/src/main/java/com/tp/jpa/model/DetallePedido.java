package com.tp.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class DetallePedido extends Base{
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetallePedido(int cantidad, double subtotal, Producto producto) {
        setCantidad(cantidad);
        setSubtotal(subtotal);
        setProducto(producto);
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        else {
            this.cantidad = cantidad;
        }
    }
}
