package com.barryku.boxnet.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.barryku.boxnet.service.RepositoryService;
import com.barryku.boxnet.service.UnauthenticatedException;
import static com.barryku.boxnet.Constants.*;

@Controller
@RequestMapping("/asset")
public class AssetUploadController implements HandlerExceptionResolver {
	private static Logger log = LoggerFactory.getLogger(AssetUploadController.class);
			
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(method = {RequestMethod.POST})
	public String handleUpload (
			@RequestParam(value="assetFile", required=false) MultipartFile assetFile, HttpSession session,
			@RequestParam(value="pathId", required=false) String pathId) throws IOException, UnauthenticatedException{
		log.debug("uploading: " + pathId);
		pathId = pathId == null ? "0" : pathId;
		
		if (assetFile != null) {	
			String status = repositoryService.putAsset(pathId, assetFile.getOriginalFilename(), assetFile);
			session.setAttribute("uploadStatus", status);
		}
		return "redirect:folders/" + pathId + "?" + PARAM_IS_UPLOAD + "=true";
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest req,
			HttpServletResponse resp, Object arg2, Exception e) {
		req.getSession().setAttribute("uploadStatus", e.getMessage());
		log.warn("upload exception caught: " + req.getQueryString()); //pathId is lost at this point
		return new ModelAndView("redirect:folders/0?" + PARAM_IS_UPLOAD + "=true");
	}
}
