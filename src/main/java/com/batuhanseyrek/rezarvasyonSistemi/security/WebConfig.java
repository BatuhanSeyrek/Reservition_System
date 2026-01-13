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
                .addPathPatterns("/admin/chair/**", "/admin/employee/**","/admin/update/**","/admin/myAdmin","/admin/chair/**","/admin/getRezervationForMyAdmin","/store/getMyReservations","/store/exportReservations");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/store/create/**","/store/userReservationGet/**","/store/userReservationDelete/**","/store/reservationUpdate/**","/store/getAvailableSlots/**","/user/myUser","/user/update","/api/favorites/toggle/**", "/api/favorites/my-favorites"); // İstediğin pathleri ekle

    }

}
