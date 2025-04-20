package com.example.fitpassserver.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
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

    @Bean
    public GroupedOpenApi all() {
        return GroupedOpenApi.builder()
                .group("전체")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi user() {
        return GroupedOpenApi.builder()
                .group("모든 회원 API(인증)")
                .pathsToMatch("/auth/**")
                .pathsToExclude("/auth/member/**", "/auth/owner/**")
                .build();
    }

    @Bean
    public GroupedOpenApi owner() {
        return GroupedOpenApi.builder()
                .group("시설회원 API")
                .pathsToMatch("/owner/**", "/auth/owner/**")
                .build();
    }

    @Bean
    public GroupedOpenApi admin() {
        return GroupedOpenApi.builder()
                .group("어드민 API")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi member() {
        return GroupedOpenApi.builder()
                .group("일반회원 API")
                .pathsToMatch("/auth/member/**", "/fitness/**", "/coin/pay/**", "/plan/pay/**", "/notice/**", "/pageView", "/management/**")
                .build();
    }
}
