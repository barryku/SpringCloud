package com.barryku.gae.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.barryku.gae.service.RepositoryService;

@Controller
@RequestMapping("/asset")
public class AssetUploadController {
	private static final String ASSET_PATH = "/asset_tests";
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model){
		return "uploadForm";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String handleUpload(
			@RequestParam("assetFile") MultipartFile assetFile) throws IOException{
		if (assetFile != null) {			
			repositoryService.putAsset(ASSET_PATH, assetFile.getOriginalFilename(), new ByteArrayInputStream(assetFile.getBytes()));
		}
		return "uploadComplete";
	}
}
