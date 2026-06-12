package com.david.academysystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                        .info(new Info()
                        .title("Gestão de Acadêmia")
                        .description("API responsável pela gestão de uma acadêmia")
                        .version("1"))
                        .schemaRequirement("jwt_auth", createSecurityscheme());
    }

    private SecurityScheme createSecurityscheme(){
        return new SecurityScheme()
                .name("jwt_auth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
