package com.victor.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.victor.cursomc.services.ImageService;

@RestController
@RequestMapping("/images")
public class ImageResource {
	
	@Autowired
	private ImageService imageService;
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file)
	{
		imageService.uploadFile(file);
		return ResponseEntity.noContent().build();
	}
	
	
}
