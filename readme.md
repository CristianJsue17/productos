## CONSTRUIR PROYECTO
docker-compose up --build

SI YA ESTA CONTUIDO, SOLO LEVANTAS CON:docker-compose up
    
DETENER CON: docker-compose down

## Endpoints disponibles (requieren token JWT):
POST http://localhost:8080/api/productos - Crear producto
GET http://localhost:8080/api/productos - Listar productos
GET http://localhost:8080/api/productos/{id} - Obtener producto
PUT http://localhost:8080/api/productos/{id} - Actualizar producto
DELETE http://localhost:8080/api/productos/{id} - Eliminar producto

## Tecnologias
- Java 21 
- Springboot
- Postgresql 15 alpine
- docker y docker-compose

## DOCUMENTACION ENDPOINTS

Swagger UI: http://localhost:8080/api/swagger-ui.html
OpenAPI JSON: http://localhost:8080/api/api-docs


## ARQUITECTURA LIMPIA

productos/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── microservicio/
│       │           └── productos/
│       │               ├── ProductosApplication.java
│       │               ├── config/
│       │               │   ├── SecurityConfig.java
│       │               │   └── CorsConfig.java
│       │               │   └── OpenAPIConfig.java
│       │               ├── domain/
│       │               │   ├── model/
│       │               │   │   └── Producto.java
│       │               │   ├── repository/
│       │               │   │   └── ProductoRepository.java
│       │               │   └── service/
│       │               │       └── ProductoService.java
│       │               │       
│       │               ├── application/
│       │               │   ├── dto/
│       │               │   │   ├── ProductoRequestDTO.java
│       │               │   │   └── ProductoResponseDTO.java
│       │               │   └── usecase/
│       │               │       └── ProductoUseCase.java
│       │               └── infrastructure/
│       │                   ├── controller/
│       │                   │   └── ProductoController.java
│       │                   │   └── HealthController.java       
│       │                   ├── security/
│       │                   │   ├── JwtAuthenticationFilter.java
│       │                   │   └── JwtValidator.java
│       │                   └── persistence/
│       │                       └── ProductoRepositoryImpl.java
│       └── resources/
│           └── application.yml
├── pom.xml
├── Dockerfile
└── docker-compose.yml