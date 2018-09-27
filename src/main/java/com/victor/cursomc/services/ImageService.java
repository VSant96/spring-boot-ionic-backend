package com.victor.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
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
			
			String newFilename = dbImagesPath + filename;
			
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
	
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) 
	{
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if(!"png".equals(ext) && !"jpg".equals(ext)) 
		{
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if("png".equals(ext)) 
			{
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}
	
	public InputStream getInputStream(BufferedImage image, String ext) 
	{
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, ext, os);
			return new ByteArrayInputStream(os.toByteArray());
			
		} catch (IOException e) {
			throw new FileException("Erro ao ler o arquivo");
		}
	}
	
}
