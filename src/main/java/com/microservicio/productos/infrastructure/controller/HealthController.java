// src/main/java/com/microservicio/productos/infrastructure/controller/HealthController.java
package com.microservicio.productos.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Root", description = "Endpoints básicos del servicio")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Información del servicio", description = "Obtiene información básica del microservicio")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Microservicio de Productos");
        response.put("version", "1.0.0");
        response.put("estado", "activo");
        
        Map<String, String> documentacion = new HashMap<>();
        documentacion.put("swagger", "http://localhost:8080/api/swagger-ui.html");
        documentacion.put("openapi", "http://localhost:8080/api/api-docs");
        response.put("documentacion", documentacion);
        
        return response;
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Verifica el estado de salud del servicio")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "saludable");
        return response;
    }
}