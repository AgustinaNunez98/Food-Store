package com.tp.jpa.repository;

import com.tp.jpa.model.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository <T extends Base>{
    private Class<T> entityClass;
    private EntityManagerFactory entityManagerFactory;

    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.entityManagerFactory = JPAUtil.getEntityManagerFactory();
    }

    public T guardar(T entity){
        EntityManager em = entityManagerFactory.createEntityManager();
        T entidadGestionada = entity;

        try {
            em.getTransaction().begin();
            if (entidadGestionada.getId() == null) {
                em.persist(entidadGestionada);
            } else {
                entidadGestionada = em.merge(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.err.println("Error al guardar la entidad: " + e.getMessage());
            }
            throw new RuntimeException(e);
        }
        finally {
            em.close();
        }
        return entidadGestionada;
    }

    public Optional<T> buscarPorId(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            T entidad = em.find(entityClass, id);
            return Optional.ofNullable(entidad);
        }
        finally {
            em.close();
        }
    }


    public List<T> listarActivos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            String nombreEntidad = entityClass.getSimpleName();
            return em.createQuery(
                    "SELECT e FROM " + nombreEntidad + " e WHERE e.eliminado = false",
                    entityClass
            ).getResultList();
        }
        finally {
            em.close();
        }
    }

    public boolean eliminarLogico(Long id){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            T entidad = em.find(entityClass, id);
            if (entidad == null || entidad.isEliminado()){
                return false;
            }
            else {
                em.getTransaction().begin();
                entidad.setEliminado(true);
                em.merge(entidad);
                em.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.err.println("Error al eliminar la entidad: " + e.getMessage());
            }
            throw new RuntimeException(e);
        }
        finally {
            em.close();
        }
    }
}