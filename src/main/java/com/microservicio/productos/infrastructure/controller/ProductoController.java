package com.microservicio.productos.infrastructure.controller;

import com.microservicio.productos.application.dto.ProductoRequestDTO;
import com.microservicio.productos.application.dto.ProductoResponseDTO;
import com.microservicio.productos.application.usecase.ProductoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión de productos")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductoController {

    @Autowired
    private ProductoUseCase productoUseCase;

    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Map<String, Object>> crearProducto(
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO productoCreado = productoUseCase.crearProducto(requestDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Producto creado exitosamente");
        response.put("producto", productoCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar productos con paginación", description = "Obtiene la lista paginada de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Map<String, Object>> listarProductos(
            @Parameter(description = "Número de página (inicia en 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") 
            @RequestParam(defaultValue = "15") int size,
            @Parameter(description = "Campo para ordenar") 
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (asc o desc)") 
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<ProductoResponseDTO> productosPage = productoUseCase.listarProductosPaginados(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("productos", productosPage.getContent());
        response.put("paginaActual", productosPage.getNumber());
        response.put("totalElementos", productosPage.getTotalElements());
        response.put("totalPaginas", productosPage.getTotalPages());
        response.put("tamanoPagina", productosPage.getSize());
        response.put("esUltimaPagina", productosPage.isLast());
        response.put("esPrimeraPagina", productosPage.isFirst());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto", description = "Obtiene un producto específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<ProductoResponseDTO> obtenerProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        ProductoResponseDTO producto = productoUseCase.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Map<String, Object>> actualizarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO productoActualizado = productoUseCase.actualizarProducto(id, requestDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Producto actualizado exitosamente");
        response.put("producto", productoActualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Map<String, String>> eliminarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        productoUseCase.eliminarProducto(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Producto eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
}