package com.barryku.cloud.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;

import org.apache.http.HttpResponse;

import com.barryku.cloud.model.FileStream;
import com.barryku.cloud.service.RepositoryService;
import com.barryku.cloud.service.helper.DropboxClient;

public class RepositoryDropboxImpl implements RepositoryService {

	private DropboxClient client;
	public void setClient(DropboxClient client) {
		this.client = client;
	}
	
	@Override
	public void putAsset(String path, String assetName, InputStream asset) {
		client.putBytes(path, assetName, (ByteArrayInputStream) asset);
	}

	@Override
	public FileStream getAssetByName(String path, String fileName) {
		FileStream result = null;
		try {
			HttpResponse response = client.getFile(path, fileName);	
			InputStream is = response.getEntity().getContent();
			result = new FileStream(is, response.getEntity().getContentLength());        
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	@Override
	public List<String> getAssetList(String path) {	
		List<String> result = null;
		try {
			result = client.getFileList(path);
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
