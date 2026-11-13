package com.tp.jpa.repository;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoriaRepository extends BaseRepository<Categoria>{
    public CategoriaRepository() {
        super(Categoria.class);
    }

    public List<Producto> buscarProductosPorCategoria(Long categoriaId){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            //La consulta lista los productos activos de una categoría específica. Categoria se representa con c y
            //Producto con p. Mediante JOIN se navega desde la categoría hacia su colección de productos. Se filtra por el ID
            //de la categoría recibido mediante el parámetro catId y se incluyen solo los productos cuyo campo eliminado sea false.
            String jpql = "SELECT p FROM Categoria c JOIN c.productos p WHERE c.id = :catId AND p.eliminado = false";
            return em.createQuery(jpql, Producto.class)
                    .setParameter("catId", categoriaId)
                    .getResultList();

        } catch (Exception e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
            throw new RuntimeException(e);
        }
        finally {
            em.close();
        }
    }

    public Producto agregarProducto(Long categoriaId, Producto producto){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Categoria categoria = em.find(Categoria.class, categoriaId);
            if (categoria == null || categoria.isEliminado()) {
                throw new IllegalArgumentException("La categoría no existe o ha sido eliminada.");
            }
            em.persist(producto);
            categoria.addProducto(producto);
            em.getTransaction().commit();
            return producto;
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.err.println("Error al agregar el producto: " + e.getMessage());
            }
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        finally {
            em.close();
        }
    }
}
