package com.dwes.empleadosapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
Configuración de CORS para poder conectarnos a esta API desde http://localhost:63342
 */
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permitir CORS en todos los endpoints
                        .allowedOrigins("*")  //.allowedOrigins("http://localhost:63342") Permitir cualquier origen o uno específico
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*"); // Permitir todos los encabezados
            }
        };

//              Si utilizamos credenciales (cookies, JWT, etc.) sería:
//              registry.addMapping("/**") // Aplica CORS a todos los endpoints
//                       .allowedOriginPatterns("*") // 🔥 Permitir cualquier origen CON credenciales
//                       .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
//                       .allowedHeaders("*") // Permitir cualquier encabezado (incluye Authorization para JWT)
//                       .allowCredentials(true); // 🔥 Permitir credenciales como JWT en Authorization header
//                  }
//              };


    }
}