package com.derrick.blogger;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@OpenAPIDefinition(
        info =
                @Info(
                        title = "Blogger",
                        description = "This application serves as the backend of a typical blog app",
                        version = "v1.0"),
        externalDocs =
                @ExternalDocumentation(description = "Blogger Repo", url = "https://github.com/Kachielite/blogger"))
public class BloggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloggerApplication.class, args);
    }
}
