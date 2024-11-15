package com.employees.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permetti CORS per tutte le rotte
                        .allowedOrigins("http://my-app.local") // Permetti solo le origini specificate
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Permetti i metodi
                        .allowedHeaders("Content-Type", "Authorization", "Accept", "Cache-Control", "Access-Control-Allow-Origin") // Specifica le intestazioni consentite
                        .allowCredentials(true); // Se necessario
            }
        };
    }
}
