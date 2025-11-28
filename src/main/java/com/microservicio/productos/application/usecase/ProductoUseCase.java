package com.microservicio.productos.application.usecase;

import com.microservicio.productos.application.dto.ProductoRequestDTO;
import com.microservicio.productos.application.dto.ProductoResponseDTO;
import com.microservicio.productos.domain.model.Producto;
import com.microservicio.productos.domain.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoUseCase {

    @Autowired
    private ProductoService productoService;

    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        Producto producto = convertirDTOaEntidad(requestDTO);
        Producto productoGuardado = productoService.crearProducto(producto);
        return convertirEntidadaDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO) {
        Producto producto = convertirDTOaEntidad(requestDTO);
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return convertirEntidadaDTO(productoActualizado);
    }

    public void eliminarProducto(Long id) {
        productoService.eliminarProducto(id);
    }

    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(this::convertirEntidadaDTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<ProductoResponseDTO> listarTodosLosProductos() {
        return productoService.listarTodosLosProductos()
                .stream()
                .map(this::convertirEntidadaDTO)
                .collect(Collectors.toList());
    }

    // Nuevo método con paginación
    public Page<ProductoResponseDTO> listarProductosPaginados(Pageable pageable) {
        return productoService.listarProductosPaginados(pageable)
                .map(this::convertirEntidadaDTO);
    }

    private Producto convertirDTOaEntidad(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());
        producto.setCodigoProducto(dto.getCodigoProducto());
        producto.setEstaActivo(dto.getEstaActivo());
        return producto;
    }

    private ProductoResponseDTO convertirEntidadaDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getCodigoProducto(),
                producto.getEstaActivo(),
                producto.getFechaCreacion(),
                producto.getFechaActualizacion()
        );
    }
}