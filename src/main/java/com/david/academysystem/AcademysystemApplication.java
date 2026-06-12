package com.david.academysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AcademysystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademysystemApplication.class, args);
	}

}
