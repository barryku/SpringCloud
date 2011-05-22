package com.barryku.boxnet.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.barryku.boxnet.dao.FileStream;
import com.barryku.boxnet.model.Asset;

public interface RepositoryService {
	public void putAsset(String path, String assetName, InputStream asset) throws UnauthenticatedException;
	public String putAsset(String path, String assetName, MultipartFile asset) throws UnauthenticatedException;
	public List<Asset> getAssetList(String path) throws UnauthenticatedException;
	public FileStream getAssetByName(String path, String name) throws FileNotFoundException, UnauthenticatedException;	
	public FileStream getAssetById(String id) throws FileNotFoundException, UnauthenticatedException;	
	public void setAuthToken(String authToken);
	public boolean isDirectDownload();
	public String getDownloadUrl(Object... params) throws UnauthenticatedException;; //to be used with UriTemplate.expand(params);
}
