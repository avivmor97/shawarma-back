package com.Shawarma.Shawarma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // כתובת ה-Frontend שלך
                .allowedMethods("GET", "POST", "PUT", "DELETE") // סוגי הבקשות המותרות
                .allowedHeaders("*") // כל הכותרות
                .allowCredentials(true); // אם אתה שולח cookies (כמו JWT)
    }
}
