package com.caterpillar.mining.backend.shared.infrastructure.documentation.openapi.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    // Properties
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${documentation.application.description}")
    String applicationDescription;

    @Value("${documentation.application.version}")
    String applicationVersion;

    // Methods

    @Bean
    public OpenAPI caterpillarMiningOpenApi() {
        // General configuration
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .license(new License().name("Apache 2.0")
                                .url("https://springdoc.org")));

        // Add a security scheme

       /*final String securitySchemeName = "bearerAuth";

       openApi.addSecurityItem(new SecurityRequirement()
                       .addList(securitySchemeName))
               .components(new Components()
                       .addSecuritySchemes(securitySchemeName,
                               new SecurityScheme()
                                       .name(securitySchemeName)
                                       .type(SecurityScheme.Type.HTTP)
                                       .scheme("bearer")
                                       .bearerFormat("JWT")));*/

        // Return the OpenAPI configuration object with all the settings

        return openApi;
    }
}
