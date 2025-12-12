package com.akbank.spring_app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


// OpenAPI ve Swagger yapılandırması için sınıf
// Token işlemlerin token header'da gönderilmesi için güvenlik şeması tanımlanıyor.

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Spring Boot Rest API Application",
                version = "1.0",
                description = "This is a Spring Boot REST API application with JWT authentication and OpenAPI documentation."
        ),
        security = @SecurityRequirement(name="bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class OpenAPIConfig {
}