package com.example.fitpassserver.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swagger() {
        Info info = new Info().title("Fit Pass").description("Fit pass API 명세서").version("0.0.1");

        String securityScheme = "JWT TOKEN";
        SecurityRequirement requirement = new SecurityRequirement().addList(securityScheme);

        Components components = new Components().addSecuritySchemes(securityScheme, new SecurityScheme()
                .name(securityScheme)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT"));

        return new OpenAPI().info(info)
                .addServersItem(new Server().url("/"))
                .addSecurityItem(requirement)
                .components(components);
    }
}
