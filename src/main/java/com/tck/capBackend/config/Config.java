package com.tck.capBackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // Map the URL Path to the external registry
        registry
                .addResourceHandler("/uploads/images")
                .addResourceLocations("file:/uploads/images");

    }
}
