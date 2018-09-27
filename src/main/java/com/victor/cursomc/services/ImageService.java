package com.victor.cursomc.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.victor.cursomc.services.exceptions.FileException;


@Service
public class ImageService {
	
	@Value("${pathDBImages}")
	private String dbImagesPath;
	
	private Logger LOG = LoggerFactory.getLogger(ImageService.class);
	
	public ImageService() 
	{
	}
	
	public String uploadFile(MultipartFile multipartFile) 
	{
		try {
			String filename = multipartFile.getOriginalFilename();
			InputStream is = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();
			
			return uploadFile(is, filename, contentType);
			
		} catch (IOException e) {
			throw new FileException("Erro IO: " + e.getMessage());
		}
	}
	
	public String uploadFile(InputStream is, String filename, String contentType) 
	{
		
		try {
			LOG.info("Iniciando o upload ...");
			
			//cria o diretório caso não exista
			new File(dbImagesPath).mkdirs();	
			
			String newFilename = dbImagesPath + Paths.get(filename).getFileName().toString();
			
			FileOutputStream out = new FileOutputStream(newFilename);
			byte [] bytes = StreamUtils.copyToByteArray(is);
			
			out.write(bytes);
			out.close();
			
			LOG.info("Upload finalizado");
			
			return newFilename;
			
		} catch (IOException e) {
			throw new RuntimeException("Erro ao realizar o upload!");
		}
	}
	
}
