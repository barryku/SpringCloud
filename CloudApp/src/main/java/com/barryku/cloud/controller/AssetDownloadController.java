package com.barryku.cloud.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.barryku.cloud.model.FileStream;
import com.barryku.cloud.service.RepositoryService;

@Controller
public class AssetDownloadController {
	private static Log log = LogFactory.getLog(AssetDownloadController.class);
	private String ASSET_PATH = "/asset_tests";
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping("/assets/{assetName}")
	public void getAsset(@PathVariable("assetName") String assetName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String mimetype = request.getSession().getServletContext().getMimeType(assetName);
		FileStream file = repositoryService.getAssetByName(ASSET_PATH, assetName);
		response.setContentType(mimetype);
		response.setContentLength((int) file.getSize());
		response.setHeader("Content-Disposition","attachment; filename=\"" + assetName +"\"");
		FileCopyUtils.copy(file.getInputStream() , response.getOutputStream());

	}
	
	@RequestMapping("/assets")
	public String getAssetList(Model model) {
		List<String> assets = repositoryService.getAssetList(ASSET_PATH);
		log.info("assets count: " + assets.size());
		model.addAttribute("assets", assets);
		return "assetList";
	}	
}
