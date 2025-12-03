// src/main/java/com/microservicio/productos/application/dto/ProductoResponseDTO.java
package com.microservicio.productos.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String categoria;
    private String codigoProducto;
    private Boolean estaActivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}