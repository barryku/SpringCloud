package com.barryku.boxnet.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.barryku.boxnet.dao.FileStream;
import com.barryku.boxnet.model.Asset;
import com.barryku.boxnet.service.RepositoryService;
import com.barryku.boxnet.service.UnauthenticatedException;

import static com.barryku.boxnet.Constants.*;

@Controller
public class AssetDownloadController {
	private static Logger log = LoggerFactory.getLogger(AssetDownloadController.class);

	@Autowired
	private RepositoryService repositoryService;
	

	@RequestMapping("/assets/{assetId}/{assetName}")
	public String getAsset(@PathVariable("assetId") String assetId, HttpServletRequest request,
			@PathVariable("assetName") String assetName, HttpServletResponse response,
			HttpSession session, HttpServletRequest req) throws IOException, UnauthenticatedException {
		log.info("retrieve asset: " + assetName);
		repositoryService.setAuthToken((String) session.getAttribute(SESSION_AUTH_TOKEN)); 
		session.setAttribute(SESSION_ACTION, req.getPathInfo());
		if (repositoryService.isDirectDownload()) {
			String downloadUrl = null;
			try {
				downloadUrl = repositoryService.getDownloadUrl(assetId);
			} catch (UnauthenticatedException ae) {
				return "forward:" + getAuthUrl(ae);
			}
			return "redirect:" + downloadUrl;
		} else {
			String mimetype = request.getSession().getServletContext().getMimeType(assetId);
			FileStream file = repositoryService.getAssetById(assetId);
			response.setContentType(mimetype);
			response.setContentLength((int) file.getSize());
			response.setHeader("Content-Disposition","attachment; filename=\"" + assetName +"\"");
			FileCopyUtils.copy(file.getInputStream() , response.getOutputStream());
			return null;
		}		
	}
	
	private String getAuthUrl(UnauthenticatedException ue) {
		String queryString = null;
		try {
			queryString = "requestUrl=" + URLEncoder.encode(ue.getRequestUrl(), "UTF-8") + 
				"&authUrl=" + URLEncoder.encode(ue.getAuthUrl(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			queryString = "requestUrl=" + ue.getRequestUrl();
		}
		log.debug("redirect to auth: " + queryString);
		return "/" + MVC_CONTEXT + "/auth?" + queryString;
		
	}
	
	@RequestMapping("/folders/{folderId}")
	public String getAssetListWithFolderId(@PathVariable("folderId") String folderId, Model model, 
			@RequestHeader(value="output-format", defaultValue="none") String ouputFormat,
			 HttpSession session, HttpServletRequest req,
			 @RequestParam(value=PARAM_IS_UPLOAD, required=false) boolean isUpload) {
		session.setAttribute(SESSION_ACTION, req.getPathInfo());
		session.setAttribute(PARAM_IS_UPLOAD, Boolean.valueOf(isUpload));
		List<Asset> assets = null;
		repositoryService.setAuthToken((String) session.getAttribute(SESSION_AUTH_TOKEN)); 
		try {
			log.debug("get folder: " + folderId);
			assets = repositoryService.getAssetList(folderId);
		} catch (UnauthenticatedException ae) {
			return "forward:" + getAuthUrl(ae);
		}
		
		log.info("assets count: " + assets.size());
		model.addAttribute("homeUrl","/" + MVC_CONTEXT + "/assets");
		model.addAttribute("ctx", MVC_CONTEXT);
		model.addAttribute("assets", assets);
		model.addAttribute("pathId", folderId);
		if ("json".equals(ouputFormat)) {
			return "jsonView";
		} else {
			return "assetList";
		}
	}
	
	@RequestMapping(value={"/assets"})
	public String getAssetList(Model model, @RequestHeader(value="output-format", defaultValue="none") String ouputFormat,
			HttpSession session, HttpServletRequest req) {
		return getAssetListWithFolderId("0", model, ouputFormat, session, req, false);
	}	
}
