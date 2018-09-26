package com.victor.cursomc.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ImageService {
	
	public ImageService() 
	{
	}
	
	public void uploadFile(String filename) 
	{
		File originalFile = new File(filename);	
		Path DBPath =Paths.get("C://temp//workspace//spring-ionic-backend//images//");
		
		File file = new File(
				"C://temp//workspace//spring-ionic-backend//images//", 
				Paths.get(filename).getFileName().toString());
		
		try {
			//cria o diretório caso não exista
			new File(DBPath.getFileName().toString()).mkdirs();
			
			// cria uma imagem nova a partir da imagem original
			BufferedImage image = ImageIO.read(originalFile);
			ImageIO.write(image, "jpg", file);
			file.createNewFile();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	
	}
	
}
