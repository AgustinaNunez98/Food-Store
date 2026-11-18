package com.tp.jpa.repository;

import com.tp.jpa.model.DetallePedido;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.tp.jpa.util.JPAUtil.getEntityManager;

public class PedidoRepository extends BaseRepository<Pedido>{
    public PedidoRepository(){
        super(Pedido.class);
    }

    public List<Pedido> buscarPorEstado(Estado estado){
        // Consulta JPQL: retorna todos los pedidos activos con un estado específico.
        // Útil para filtrar PENDIENTE, CONFIRMADO, TERMINADO o CANCELADO.
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Pedido p WHERE p.estado = :estado AND p.eliminado = false";
            List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
                .setParameter("estado", estado)
                .getResultList();
                return pedidos;
        } finally {
            em.close();
        }
    }

    public Pedido crearPedido(long idUsuario, FormaPago formaPago,
                              Map<Long, Integer> cantidadesPorProducto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (cantidadesPorProducto.isEmpty()) {
                throw new IllegalArgumentException("No se proporcionaron productos para el pedido.");
            }
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario == null || usuario.isEliminado()) {
                throw new IllegalArgumentException("El usuario no existe o está dado de baja.");
            }
            Pedido pedido = new Pedido();
            pedido.setFecha(LocalDate.now());
            pedido.setEstado(Estado.PENDIENTE);
            pedido.setFormaPago(formaPago);

            for (Map.Entry<Long, Integer> entry : cantidadesPorProducto.entrySet()) {
                Long idProducto = entry.getKey();
                Integer cantidad = entry.getValue();
                Producto productoEncontrado = em.find(Producto.class, idProducto);
                if (cantidad <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
                } else if (productoEncontrado == null || productoEncontrado.isEliminado() || productoEncontrado.getStock() < cantidad || !productoEncontrado.isDisponible()) {
                    throw new IllegalArgumentException("El producto no existe, está dado de baja o no tiene suficiente stock.");
                }
                pedido.addDetallePedido(productoEncontrado, cantidad);
                productoEncontrado.setStock(productoEncontrado.getStock() - cantidad);
            }

            double total = pedido.calcularTotal();
            pedido.setTotal(total);
            em.persist(pedido);
            usuario.getPedidos().add(pedido);
            em.getTransaction().commit();
            return pedido;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
