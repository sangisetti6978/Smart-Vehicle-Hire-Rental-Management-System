package com.vehiclerental.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolve to absolute path and ensure it ends with /
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
        if (!absolutePath.endsWith("/")) {
            absolutePath += "/";
        }
        registry.addResourceHandler("/uploads/vehicles/**")
                .addResourceLocations(absolutePath)
                .setCachePeriod(3600);

        // Serve the frontend directory so the UI is accessible on the same origin
        Path frontendDir = Paths.get(System.getProperty("user.dir")).resolve("../frontend").normalize();
        String frontendPath = frontendDir.toUri().toString();
        if (!frontendPath.endsWith("/")) {
            frontendPath += "/";
        }
        registry.addResourceHandler("/**")
                .addResourceLocations(frontendPath)
                .setCachePeriod(0);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
