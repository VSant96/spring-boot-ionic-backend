package com.victor.cursomc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.victor.cursomc.services.ImageService;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired
	ImageService imageService;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception { 
		//imageService.uploadFile("C:\\Users\\Victor Santos\\Pictures\\Perfil de usuário\\maria.jpg");
	}
}