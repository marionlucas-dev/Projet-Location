package com.accenture.configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger pour l'application de gestion des locations de véhicules.
 * Cette classe permet de configurer Swagger pour générer automatiquement la documentation de l'API de l'application. Elle définit les métadonnées de base
 * pour la documentation de l'API, telles que le titre, la description et la licence.
 */


@Configuration
public class SwaggerConfiguration {
    @Bean
    OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestion de Locations de Véhicules")
                        .description("Interface de gestion du parc locatif "));
    }
}




