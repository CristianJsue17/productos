package com.microservicio.productos.domain.service;

import com.microservicio.productos.domain.model.Producto;
import com.microservicio.productos.infrastructure.persistence.ProductoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepositoryImpl productoRepository;

    public Producto crearProducto(Producto producto) {
        if (productoRepository.existePorCodigoProducto(producto.getCodigoProducto())) {
            throw new RuntimeException("Ya existe un producto con el código: " + producto.getCodigoProducto());
        }
        return productoRepository.guardar(producto);
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (!productoExistente.getCodigoProducto().equals(producto.getCodigoProducto()) &&
                productoRepository.existePorCodigoProducto(producto.getCodigoProducto())) {
            throw new RuntimeException("Ya existe un producto con el código: " + producto.getCodigoProducto());
        }

        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setCategoria(producto.getCategoria());
        productoExistente.setCodigoProducto(producto.getCodigoProducto());
        productoExistente.setEstaActivo(producto.getEstaActivo());

        return productoRepository.guardar(productoExistente);
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.buscarPorId(id).isPresent()) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.eliminar(id);
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.buscarPorId(id);
    }

    public List<Producto> listarTodosLosProductos() {
        return productoRepository.listarTodos();
    }

    // Nuevo método con paginación
    public Page<Producto> listarProductosPaginados(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }
}