package com.microservicio.productos.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtValidator {

    @Value("${jwt.secret}")
    private String secretKey;

    public Claims validarToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido: " + e.getMessage());
        }
    }

    public String obtenerNombreUsuario(String token) {
        Claims claims = validarToken(token);
        return claims.getSubject();
    }

    public Boolean esAdmin(String token) {
        Claims claims = validarToken(token);
        return claims.get("es_admin", Boolean.class);
    }
}