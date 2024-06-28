package com.project.workaholic.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm");
        Date now = new Date();

        Info info = new Info()
                .version(simpleDateFormat.format(now))
                .title("Workaholic 명세서")
                .description("Workaholic API Description");

        String jwtSchemeName = "JWT AccessToken";
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public GroupedOpenApi getVSCApi() {
        String[] paths = {
                "/account/**",
                "/vcs/**",
                "/github/**",
                "/gitlab/**"
        };

        return GroupedOpenApi.builder()
                .group("Version control system API")
                .pathsToMatch(paths)
                .build();
    }
}
