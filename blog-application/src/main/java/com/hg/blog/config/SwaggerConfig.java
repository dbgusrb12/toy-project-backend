package com.hg.blog.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String moduleName;
    @Value("${spring.application.desc}")
    private String description;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Authentication";
        final String apiTitle = String.format("%s API", moduleName);

        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(
                new Components().addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .info(new Info().title(apiTitle).version("v1").description(description));
    }
}
