package lk.ashan.routenetlkserverapllication.shared.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "RouteNetLK API Documentation",
                version = "1.0",
                description = "API documentation for the RouteNetLK system backend, including transport management, crew allocation, and route tracking.",
                contact = @Contact(
                        name = "Ashan",
                        email = "support@routenetlk.lk"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .path("/login", new PathItem()
                        .post(new Operation()
                                .addTagsItem("Authentication")
                                .summary("Login to receive JWT token")
                                .description("Authenticate with username and password to receive a JWT token in the response header.")
                                .requestBody(new io.swagger.v3.oas.models.parameters.RequestBody()
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema<>()
                                                                .type("object")
                                                                .addProperty("username", new StringSchema().example("admin"))
                                                                .addProperty("password", new StringSchema().example("admin123"))))))
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse()
                                                .description("Login successful. Token is returned in the 'Authorization' header as 'Bearer <token>'.")
                                                .content(new Content().addMediaType("application/json", new MediaType().schema(new Schema<>().type("object")))))
                                        .addApiResponse("401", new ApiResponse().description("Invalid credentials")))));
    }
}
