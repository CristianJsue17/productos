// src/test/java/com/microservicio/productos/application/usecase/ProductoUseCaseActualizarProductoTest.java
package com.microservicio.productos.application.usecase;

import com.microservicio.productos.application.dto.ProductoRequestDTO;
import com.microservicio.productos.application.dto.ProductoResponseDTO;
import com.microservicio.productos.domain.model.Producto;
import com.microservicio.productos.domain.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tabla de Decisión: Actualización de Productos")
class ProductoUseCaseActualizarProductoTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    private ProductoRequestDTO requestActualizacion;
    private Producto productoActualizado;

    @BeforeEach
    void setUp() {
        requestActualizacion = new ProductoRequestDTO(
                "Laptop HP Actualizada",
                "Nueva descripción",
                new BigDecimal("1800.00"),
                15,
                "Tecnología",
                "LAP-001",
                true
        );

        productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Laptop HP Actualizada");
        productoActualizado.setDescripcion("Nueva descripción");
        productoActualizado.setPrecio(new BigDecimal("1800.00"));
        productoActualizado.setStock(15);
        productoActualizado.setCategoria("Tecnología");
        productoActualizado.setCodigoProducto("LAP-001");
        productoActualizado.setEstaActivo(true);
    }

    /**
     * TABLA DE DECISIÓN: Actualización de Productos
     * 
     * Condición                    | C1  | C2  | C3  | C4  |
     * -----------------------------|-----|-----|-----|-----|
     * Producto existe              | V   | V   | F   | F   |
     * Nuevo código único           | V   | F   | -   | -   |
     * -----------------------------|-----|-----|-----|-----|
     * Acción                       |     |     |     |     |
     * -----------------------------|-----|-----|-----|-----|
     * Actualizar exitosamente      | X   |     |     |     |
     * Excepción código duplicado   |     | X   |     |     |
     * Excepción producto no existe |     |     | X   | X   |
     */

    @Test
    @DisplayName("C1: Producto existe + Código único = Actualización exitosa")
    void testC1_ActualizarProducto_ProductoExisteCodigoUnico_Exitoso() {
        // Arrange
        when(productoService.actualizarProducto(eq(1L), any(Producto.class)))
                .thenReturn(productoActualizado);

        // Act
        ProductoResponseDTO resultado = productoUseCase.actualizarProducto(1L, requestActualizacion);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop HP Actualizada", resultado.getNombre());
        assertEquals(new BigDecimal("1800.00"), resultado.getPrecio());
        assertEquals(15, resultado.getStock());
        
        verify(productoService, times(1)).actualizarProducto(eq(1L), any(Producto.class));
    }

    @Test
    @DisplayName("C2: Producto existe + Código duplicado = Lanzar excepción")
    void testC2_ActualizarProducto_CodigoDuplicado_LanzaExcepcion() {
        // Arrange
        when(productoService.actualizarProducto(eq(1L), any(Producto.class)))
                .thenThrow(new RuntimeException("Ya existe un producto con el código: LAP-001"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoUseCase.actualizarProducto(1L, requestActualizacion);
        });

        assertTrue(exception.getMessage().contains("Ya existe un producto con el código"));
        verify(productoService, times(1)).actualizarProducto(eq(1L), any(Producto.class));
    }

    @Test
    @DisplayName("C3: Producto no existe = Lanzar excepción")
    void testC3_ActualizarProducto_ProductoNoExiste_LanzaExcepcion() {
        // Arrange
        when(productoService.actualizarProducto(eq(999L), any(Producto.class)))
                .thenThrow(new RuntimeException("Producto no encontrado con ID: 999"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoUseCase.actualizarProducto(999L, requestActualizacion);
        });

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoService, times(1)).actualizarProducto(eq(999L), any(Producto.class));
    }

    @Test
    @DisplayName("C4: Cambiar estado activo/inactivo")
    void testC4_ActualizarProducto_CambiarEstadoActivo_Exitoso() {
        // Arrange
        ProductoRequestDTO requestDesactivar = new ProductoRequestDTO(
                "Laptop HP",
                "Descripción",
                new BigDecimal("1500.00"),
                10,
                "Electrónica",
                "LAP-001",
                false // Desactivar producto
        );

        Producto productoDesactivado = new Producto();
        productoDesactivado.setId(1L);
        productoDesactivado.setEstaActivo(false);
        productoDesactivado.setNombre("Laptop HP");
        productoDesactivado.setPrecio(new BigDecimal("1500.00"));
        productoDesactivado.setStock(10);
        productoDesactivado.setCategoria("Electrónica");
        productoDesactivado.setCodigoProducto("LAP-001");

        when(productoService.actualizarProducto(eq(1L), any(Producto.class)))
                .thenReturn(productoDesactivado);

        // Act
        ProductoResponseDTO resultado = productoUseCase.actualizarProducto(1L, requestDesactivar);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getEstaActivo());
        verify(productoService, times(1)).actualizarProducto(eq(1L), any(Producto.class));
    }
}