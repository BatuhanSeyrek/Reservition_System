package com.batuhanseyrek.rezarvasyonSistemi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {"com.batuhanseyrek"})
@SpringBootApplication
public class RezarvasyonSistemiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezarvasyonSistemiApplication.class, args);
	}

}
