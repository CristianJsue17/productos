// src/test/java/com/microservicio/productos/application/dto/ProductoRequestDTOValidacionTest.java
package com.microservicio.productos.application.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tabla de Decisión: Validaciones de ProductoRequestDTO")
class ProductoRequestDTOValidacionTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * TABLA DE DECISIÓN: Validaciones de Campos
     * 
     * Condición                    | C1  | C2  | C3  | C4  | C5  | C6  |
     * -----------------------------|-----|-----|-----|-----|-----|-----|
     * Nombre válido                | V   | F   | V   | V   | V   | V   |
     * Precio válido (> 0)          | V   | V   | F   | V   | V   | V   |
     * Stock válido (>= 0)          | V   | V   | V   | F   | V   | V   |
     * Código válido                | V   | V   | V   | V   | F   | V   |
     * -----------------------------|-----|-----|-----|-----|-----|-----|
     * Acción                       |     |     |     |     |     |     |
     * -----------------------------|-----|-----|-----|-----|-----|-----|
     * Validación exitosa           | X   |     |     |     |     |     |
     * Error: nombre requerido      |     | X   |     |     |     |     |
     * Error: precio inválido       |     |     | X   |     |     |     |
     * Error: stock negativo        |     |     |     | X   |     |     |
     * Error: código requerido      |     |     |     |     | X   |     |
     */

    @Test
    @DisplayName("C1: Todos los campos válidos = Sin errores de validación")
    void testC1_TodosLosCamposValidos_SinErrores() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop HP",
                "Descripción válida",
                new BigDecimal("1500.00"),
                10,
                "Electrónica",
                "LAP-001",
                true
        );

        // Act
        Set<ConstraintViolation<ProductoRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
    }

    @Test
    @DisplayName("C2: Nombre vacío = Error de validación")
    void testC2_NombreVacio_ErrorValidacion() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "", // Nombre vacío
                "Descripción",
                new BigDecimal("1500.00"),
                10,
                "Electrónica",
                "LAP-001",
                true
        );

        // Act
        Set<ConstraintViolation<ProductoRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("nombre es obligatorio")));
    }

    @Test
    @DisplayName("C3: Precio cero o negativo = Error de validación")
    void testC3_PrecioCeroONegativo_ErrorValidacion() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop HP",
                "Descripción",
                new BigDecimal("0.00"), // Precio inválido
                10,
                "Electrónica",
                "LAP-001",
                true
        );

        // Act
        Set<ConstraintViolation<ProductoRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("precio debe ser mayor a 0")));
    }

    @Test
    @DisplayName("C4: Stock negativo = Error de validación")
    void testC4_StockNegativo_ErrorValidacion() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop HP",
                "Descripción",
                new BigDecimal("1500.00"),
                -5, // Stock negativo
                "Electrónica",
                "LAP-001",
                true
        );

        // Act
        Set<ConstraintViolation<ProductoRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("stock no puede ser negativo")));
    }

    @Test
    @DisplayName("C5: Código vacío = Error de validación")
    void testC5_CodigoVacio_ErrorValidacion() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Laptop HP",
                "Descripción",
                new BigDecimal("1500.00"),
                10,
                "Electrónica",
                "", // Código vacío
                true
        );

        // Act
        Set<ConstraintViolation<ProductoRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("código del producto es obligatorio")));
    }

    @Test
    @DisplayName("C6: Múltiples errores simultáneos")
    void testC6_MultiplesCamposInvalidos_MultipleserroresValidacion() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "", // Nombre vacío
                "Descripción",
                new BigDecimal("-100.00"), // Precio negativo
                -10, // Stock negativo
                "Electrónica",
                "", // Código vacío
                true
        );

        // Act
        Set<ConstraintViolation<ProductoRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.size() >= 3, 
                "Debería haber al menos 3 errores de validación");
    }
}