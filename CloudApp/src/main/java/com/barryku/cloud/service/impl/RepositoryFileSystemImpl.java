package com.barryku.cloud.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.barryku.cloud.model.FileStream;
import com.barryku.cloud.service.RepositoryService;

public class RepositoryFileSystemImpl implements RepositoryService {

	@Override
	public void putAsset(String path, String assetName, InputStream asset) {
		createNewDir(path);
		try {
			ByteArrayInputStream assetStream = (ByteArrayInputStream) asset;			
			FileOutputStream fos = new FileOutputStream(path + "/" + assetName);
			byte[] buffer = new byte[1024];
			int n = -1;
			while ((n = assetStream.read(buffer)) != -1) {
				fos.write(buffer, 0, n);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void createNewDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	@Override
	public FileStream getAssetByName(String path, String name) throws FileNotFoundException{	
		File file = new File(path, name);
		if (!file.exists())
			throw new FileNotFoundException(path+"/" + name);
		return new FileStream(new FileInputStream(file), file.length());
	}

	@Override
	public List<String> getAssetList(String path) {
		List<String> assets = new ArrayList<String>();	
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file:files) {
				if (file.isFile())
					assets.add(file.getName());
			}
		}
		
		return assets;
	}

}
