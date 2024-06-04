package com.project.workaholic.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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

        return new OpenAPI()
                .info(info);
    }
}
