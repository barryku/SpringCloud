package com.barryku.boxnet.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;
import org.springframework.xml.xpath.AbstractXPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.barryku.boxnet.model.Asset;
import com.barryku.boxnet.model.FileItem;
import com.barryku.boxnet.model.FolderItem;
import com.barryku.boxnet.dao.FileStream;
import com.barryku.boxnet.service.RepositoryService;
import com.barryku.boxnet.service.UnauthenticatedException;

public class RepositoryBoxNetImpl implements RepositoryService {
	private static final String URI_GET_ACCOUNT_TREE = "{resetUrl}&action=get_account_tree&folder_id={folderId}&params[]=onelevel&params[]=nozip&auth_token={authToken}";
	
	private String authToken;
	private String downloadUrl; 
	private AbstractXPathTemplate xpathTemplate;
	private RestTemplate restTemplate;
	private String authUrl;
	private String requestUrl;
	private String restUrl;
	private String uploadUrl;

	private static Logger log = LoggerFactory.getLogger(RepositoryBoxNetImpl.class);
	
	@Override
	public FileStream getAssetByName(String path, String name)
			throws FileNotFoundException, UnauthenticatedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asset> getAssetList(String path)
			throws UnauthenticatedException {		
		checkAuthentication();
		
		List<Asset> assets = new ArrayList<Asset>();
		
		//path will replace the vars in the restRequest URI template, i.e. {restUrl} and {folderId}
		Source response = restTemplate.getForObject(URI_GET_ACCOUNT_TREE, Source.class, restUrl, path, authToken); 
		List<FileItem> files = xpathTemplate.evaluate("//files/file", response, new NodeMapper<FileItem>() {
			@Override
			public FileItem mapNode(Node node, int nodeNum) throws DOMException {
				Element element = (Element) node;
				FileItem bfile = new FileItem();
				bfile.setId(element.getAttribute("id"));
				bfile.setFileName(element.getAttribute("file_name"));
				return bfile;
			}			
		});		
		
		response = restTemplate.getForObject(URI_GET_ACCOUNT_TREE, Source.class, restUrl, path, authToken);
		List<FolderItem> folders = xpathTemplate.evaluate("//folders/folder", response, new NodeMapper<FolderItem>() {
			@Override
			public FolderItem mapNode(Node node, int nodeNum) throws DOMException {
				Element element = (Element) node;
				FolderItem bfile = new FolderItem();
				bfile.setId(element.getAttribute("id"));
				bfile.setName(element.getAttribute("name"));
				return bfile;
			}			
		});
		
		log.info("size: " + files.size());
		for (Asset f : folders) {
			assets.add(f);
		}
		
		for (Asset f : files) {
			assets.add(f);
		}
		return assets;
	}

	@Override
	public void putAsset(String path, String assetName, InputStream asset)
			throws UnauthenticatedException {
		log.info("uploading asset: " + asset);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		InputStreamResource file = new InputStreamResource(asset);
		parts.add("file", asset);

		try {
			log.debug("uploading");
		String response = restTemplate.postForObject(uploadUrl, parts, String.class, authToken, path);
		log.debug(response);
		} catch (Exception e) {
			log.error("upload failed", e);
		}

	}
	
	@Override
	public String putAsset(String path, String assetName, MultipartFile asset)
			throws UnauthenticatedException {
		log.info("uploading asset: " + assetName);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		//restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		
		String tempDir = "/tmp/boxnet";
		String tempFileName = tempDir + "/" + assetName;
		String status = null;
		try {
			File dir = new File(tempDir);
			dir.mkdir();
			FileOutputStream fo = new FileOutputStream(tempFileName);
			fo.write(asset.getBytes());
			fo.close();
			
			parts.add(assetName, asset.getBytes());
			parts.add("file", new FileSystemResource(tempFileName));
			
			/**
			Source response = restTemplate.postForObject(uploadUrl, parts, Source.class, authToken, path);
			status = xpathTemplate.evaluateAsString("//status", response);
			**/
			String response = restTemplate.postForObject(uploadUrl, parts, String.class, authToken, path);
			status = response.substring(response.indexOf("status>") + "status>".length());
			status = status.substring(0, status.indexOf("</status"));
			
			File f = new File(tempFileName);
			f.delete();
		} catch (Exception e) {
			log.error("upload failed", e);
		}
		
		return status;
		
	}
	

	@Override
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	@Override
	public FileStream getAssetById(String id) throws FileNotFoundException,
			UnauthenticatedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkAuthentication() throws UnauthenticatedException {
		if (authToken == null) {
			UnauthenticatedException ue = new UnauthenticatedException();
			ue.setAuthUrl(authUrl);
			ue.setRequestUrl(requestUrl);
			throw ue;
		}
	}
	
	public void setDownloadUrl(String url) {
		downloadUrl = url;
	}
	
	public void setUploadUrl(String url) {
		uploadUrl = url;
	}

	public String getDownloadUrl(Object... params) throws UnauthenticatedException {
		checkAuthentication();
		UriTemplate ut = new UriTemplate(downloadUrl);
		URI uri = ut.expand(authToken, params[0]);
		return uri.toString();
	}
	
	public void setXpathTemplate(AbstractXPathTemplate xpathTemplate) {
		this.xpathTemplate = xpathTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	@Override
	public boolean isDirectDownload() {
		return true;
	}
	
}
