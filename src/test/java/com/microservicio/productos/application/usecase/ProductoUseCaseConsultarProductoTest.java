// src/test/java/com/microservicio/productos/application/usecase/ProductoUseCaseConsultarProductoTest.java
package com.microservicio.productos.application.usecase;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tabla de Decisión: Consulta de Productos")
class ProductoUseCaseConsultarProductoTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop HP");
        producto.setDescripcion("Laptop de alta gama");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setStock(10);
        producto.setCategoria("Electrónica");
        producto.setCodigoProducto("LAP-001");
        producto.setEstaActivo(true);
    }

    /**
     * TABLA DE DECISIÓN: Consulta de Productos
     * 
     * Condición                    | C1  | C2  | C3  |
     * -----------------------------|-----|-----|-----|
     * Producto existe              | V   | V   | F   |
     * Producto activo              | V   | F   | -   |
     * -----------------------------|-----|-----|-----|
     * Acción                       |     |     |     |
     * -----------------------------|-----|-----|-----|
     * Devolver producto activo     | X   |     |     |
     * Devolver producto inactivo   |     | X   |     |
     * Lanzar excepción             |     |     | X   |
     */

    @Test
    @DisplayName("C1: Producto existe + Activo = Devolver producto")
    void testC1_ObtenerProducto_ExisteYActivo_Exitoso() {
        // Arrange
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // Act
        ProductoResponseDTO resultado = productoUseCase.obtenerProductoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop HP", resultado.getNombre());
        assertTrue(resultado.getEstaActivo());
        assertEquals(new BigDecimal("1500.00"), resultado.getPrecio());
        
        verify(productoService, times(1)).obtenerProductoPorId(1L);
    }

    @Test
    @DisplayName("C2: Producto existe + Inactivo = Devolver producto inactivo")
    void testC2_ObtenerProducto_ExisteYInactivo_Exitoso() {
        // Arrange
        producto.setEstaActivo(false);
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // Act
        ProductoResponseDTO resultado = productoUseCase.obtenerProductoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertFalse(resultado.getEstaActivo());
        
        verify(productoService, times(1)).obtenerProductoPorId(1L);
    }

    @Test
    @DisplayName("C3: Producto no existe = Lanzar excepción")
    void testC3_ObtenerProducto_NoExiste_LanzaExcepcion() {
        // Arrange
        when(productoService.obtenerProductoPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoUseCase.obtenerProductoPorId(999L);
        });

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoService, times(1)).obtenerProductoPorId(999L);
    }
}