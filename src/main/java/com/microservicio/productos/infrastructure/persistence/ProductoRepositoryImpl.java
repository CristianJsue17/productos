package com.microservicio.productos.infrastructure.persistence;

import com.microservicio.productos.domain.model.Producto;
import com.microservicio.productos.domain.repository.ProductoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepositoryImpl extends JpaRepository<Producto, Long>, ProductoRepository {
    
    @Override
    default Producto guardar(Producto producto) {
        return save(producto);
    }

    @Override
    default Optional<Producto> buscarPorId(Long id) {
        return findById(id);
    }

    @Override
    default java.util.List<Producto> listarTodos() {
        return findAll();
    }

    @Override
    default void eliminar(Long id) {
        deleteById(id);
    }

    // Métodos de Spring Data JPA (SIN @Override)
    boolean existsByCodigoProducto(String codigoProducto);

    @Override
    default boolean existePorCodigoProducto(String codigoProducto) {
        return existsByCodigoProducto(codigoProducto);
    }

    // Métodos de Spring Data JPA (SIN @Override)
    Optional<Producto> findByCodigoProducto(String codigoProducto);

    @Override
    default Optional<Producto> buscarPorCodigoProducto(String codigoProducto) {
        return findByCodigoProducto(codigoProducto);
    }
}