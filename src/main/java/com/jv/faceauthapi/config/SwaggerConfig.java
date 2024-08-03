package com.jv.faceauthapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Faceauth API",
                version = "1.0",
                description = "API de autenticação facial com Java, Spring e AWS - S3 e Rekognition",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "João Vitor Farias",
                        email = "joaovx.soares@gmail.com"
                )
        )
)
public class SwaggerConfig {
}
