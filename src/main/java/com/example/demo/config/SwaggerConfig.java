package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supplier Diversity Tracker")
                        .version("1.0")
                        .description("API documentation for AmyPO test cases")
                )
                .servers(List.of(
                        new Server().url("https://9444.pro604cr.amypo.ai/")
                ));
    }
}