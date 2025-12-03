// src/main/java/com/microservicio/productos/domain/repository/ProductoRepository.java
package com.microservicio.productos.domain.repository;

import com.microservicio.productos.domain.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    Producto guardar(Producto producto);
    Optional<Producto> buscarPorId(Long id);
    List<Producto> listarTodos();
    void eliminar(Long id);
    boolean existePorCodigoProducto(String codigoProducto);
    Optional<Producto> buscarPorCodigoProducto(String codigoProducto);
}