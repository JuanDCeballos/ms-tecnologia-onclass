package co.onclass.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Microservicio de tecnologias",
        description = "Microservicio para la gestión de las tecnologías de los bootcamps"
))
public class SwaggerConfig {
}
