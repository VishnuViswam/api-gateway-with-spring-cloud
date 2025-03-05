package com.server.gateway.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Value("${build.version}")
    private String buildVersion;
    @Value("${build.name}")
    private String buildName;
    @Value("${build.desc}")
    private String buildDesc;


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(buildName)
                        .version(buildVersion)
                        .description(buildDesc)
                        .license(new License().name("Open License"))
                );
    }
}
