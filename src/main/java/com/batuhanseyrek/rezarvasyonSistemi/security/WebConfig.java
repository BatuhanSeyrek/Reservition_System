package com.batuhanseyrek.rezarvasyonSistemi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;


    @Autowired
    private UserInterceptor userInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/chair/**", "/admin/employee/**","/admin/update/**");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/store/create/**","/store/userReservationGet/**","/store/userReservationDelete/**","/store/reservationUpdate/**","/store/getAvailableSlots/**"); // İstediğin pathleri ekle

    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // React çalıştığı port
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
