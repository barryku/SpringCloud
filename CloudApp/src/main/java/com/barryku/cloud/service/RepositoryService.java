package com.barryku.cloud.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.barryku.cloud.model.FileStream;

public interface RepositoryService {
	public void putAsset(String path, String assetName, InputStream asset);
	public List<String> getAssetList(String path);
	public FileStream getAssetByName(String path, String name) throws FileNotFoundException;
}
