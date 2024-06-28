package com.musinsa.product_app.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("무신사 프로젝트 API")
                        .description("무신사 백엔드 엔지니어 채용 과제 입니다.")
                        .version("1.0.0"));
    }
}