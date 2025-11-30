// src/main/java/com/microservicio/productos/infrastructure/security/JwtAuthenticationFilter.java
package com.microservicio.productos.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // ← CAMBIO: Buscar en X-Auth-Token en lugar de Authorization
        String authHeader = request.getHeader("X-Auth-Token");
        
        // ← FALLBACK: Si no existe X-Auth-Token, buscar en Authorization (para compatibilidad local)
        if (authHeader == null || authHeader.isEmpty()) {
            authHeader = request.getHeader("Authorization");
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                String nombreUsuario = jwtValidator.obtenerNombreUsuario(token);
                Boolean esAdmin = jwtValidator.esAdmin(token);

                if (nombreUsuario != null && esAdmin != null && esAdmin) {
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            nombreUsuario, 
                            null, 
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}