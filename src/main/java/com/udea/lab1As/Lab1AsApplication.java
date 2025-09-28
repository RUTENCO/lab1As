package com.udea.lab1As;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Lab1AsApplication {

	public static void main(String[] args) {
		// Cargar variables del archivo .env antes de iniciar Spring Boot
		Dotenv dotenv = Dotenv.configure()
				.directory("./")  // Busca el .env en la raíz del proyecto
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();
		
		// Establecer las variables como propiedades del sistema
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});
		
		SpringApplication.run(Lab1AsApplication.class, args);
	}

}
