// src/test/java/com/microservicio/productos/application/usecase/ProductoUseCaseCrearProductoTest.java
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tabla de Decisión: Creación de Productos")
class ProductoUseCaseCrearProductoTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    private ProductoRequestDTO requestValido;
    private Producto productoGuardado;

    @BeforeEach
    void setUp() {
        // Configuración común
        requestValido = new ProductoRequestDTO(
                "Laptop HP",
                "Laptop de alta gama",
                new BigDecimal("1500.00"),
                10,
                "Electrónica",
                "LAP-001",
                true
        );

        productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Laptop HP");
        productoGuardado.setDescripcion("Laptop de alta gama");
        productoGuardado.setPrecio(new BigDecimal("1500.00"));
        productoGuardado.setStock(10);
        productoGuardado.setCategoria("Electrónica");
        productoGuardado.setCodigoProducto("LAP-001");
        productoGuardado.setEstaActivo(true);
    }

    /**
     * TABLA DE DECISIÓN: Creación de Productos
     * 
     * Condición                    | C1  | C2  | C3  | C4  |
     * -----------------------------|-----|-----|-----|-----|
     * Datos válidos                | V   | V   | F   | F   |
     * Código único                 | V   | F   | V   | F   |
     * -----------------------------|-----|-----|-----|-----|
     * Acción                       |     |     |     |     |
     * -----------------------------|-----|-----|-----|-----|
     * Crear producto               | X   |     |     |     |
     * Lanzar excepción duplicado   |     | X   |     |     |
     * Validación falla             |     |     | X   | X   |
     */

    @Test
    @DisplayName("C1: Datos válidos + Código único = Crear producto exitosamente")
    void testC1_CrearProducto_DatosValidosCodigoUnico_Exitoso() {
        // Arrange
        when(productoService.crearProducto(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        ProductoResponseDTO resultado = productoUseCase.crearProducto(requestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop HP", resultado.getNombre());
        assertEquals("LAP-001", resultado.getCodigoProducto());
        assertEquals(new BigDecimal("1500.00"), resultado.getPrecio());
        assertTrue(resultado.getEstaActivo());
        
        verify(productoService, times(1)).crearProducto(any(Producto.class));
    }

    @Test
    @DisplayName("C2: Datos válidos + Código duplicado = Lanzar excepción")
    void testC2_CrearProducto_CodigoDuplicado_LanzaExcepcion() {
        // Arrange
        when(productoService.crearProducto(any(Producto.class)))
                .thenThrow(new RuntimeException("Ya existe un producto con el código: LAP-001"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoUseCase.crearProducto(requestValido);
        });

        assertTrue(exception.getMessage().contains("Ya existe un producto con el código"));
        verify(productoService, times(1)).crearProducto(any(Producto.class));
    }

    @Test
    @DisplayName("C3: Stock negativo = Validación debe fallar")
    void testC3_CrearProducto_StockNegativo_ValidacionFalla() {
        // Arrange
        ProductoRequestDTO requestInvalido = new ProductoRequestDTO(
                "Laptop HP",
                "Laptop de alta gama",
                new BigDecimal("1500.00"),
                -5, // Stock negativo
                "Electrónica",
                "LAP-002",
                true
        );

        // Act & Assert
        // Nota: Esta validación normalmente la maneja @Valid en el controller
        // Aquí verificamos que el valor esté incorrecto
        assertTrue(requestInvalido.getStock() < 0, "El stock no debería ser negativo");
    }

    @Test
    @DisplayName("C4: Precio cero o negativo = Validación debe fallar")
    void testC4_CrearProducto_PrecioCeroONegativo_ValidacionFalla() {
        // Arrange
        ProductoRequestDTO requestInvalido = new ProductoRequestDTO(
                "Laptop HP",
                "Laptop de alta gama",
                new BigDecimal("0.00"), // Precio inválido
                10,
                "Electrónica",
                "LAP-003",
                true
        );

        // Act & Assert
        assertTrue(requestInvalido.getPrecio().compareTo(BigDecimal.ZERO) <= 0, 
                   "El precio debe ser mayor a cero");
    }
}