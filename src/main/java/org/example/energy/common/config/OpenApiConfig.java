package org.example.energy.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;

import java.security.Principal;


@Configuration
public class OpenApiConfig {

    static {
        org.springdoc.core.utils.SpringDocUtils.getConfig()
                .addRequestWrapperToIgnore(org.springframework.security.core.Authentication.class)
                .addRequestWrapperToIgnore(Principal.class)
                .addRequestWrapperToIgnore(SecurityContext.class)
                .addRequestWrapperToIgnore(org.springframework.security.core.userdetails.UserDetails.class);
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Energy Management System API")
                        .version("1.0.0")
                        .description("API RESTful para gestión energética.")
                        .contact(new Contact()
                                .name("Soporte Backend")
                                .email("backend@energy.example.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce el token JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}