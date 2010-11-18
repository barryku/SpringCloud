package com.barryku.cloud.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.barryku.cloud.model.FileStream;
import com.barryku.cloud.service.RepositoryService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class RepositoryAmazonS3Impl implements RepositoryService {
	
private static final String FOLDER_SUFFIX = "/";
	
	private AmazonS3 s3;
	private String bucket;	

	public void setBucket(String bucket){
		this.bucket = bucket;
	}
	
	public RepositoryAmazonS3Impl(AmazonS3 client) {
		s3 = client;
	}

	@Override
	public FileStream getAssetByName(String path, String name)
			throws FileNotFoundException {		
		S3Object obj = s3.getObject(new GetObjectRequest(bucket, getS3Path(path) + name));
		FileStream result = new FileStream(obj.getObjectContent(), obj.getObjectMetadata().getContentLength());
		return result;
	}
	
	@Override
	public List<String> getAssetList(String path) {
		List<String> result = new ArrayList<String>();		
		ObjectListing objList = s3.listObjects(bucket, getS3Path(path));
		for (S3ObjectSummary summary:objList.getObjectSummaries()) {
			//ignore folders
			if(! summary.getKey().endsWith(FOLDER_SUFFIX)){
				result.add(summary.getKey().substring(path.length()));
			}
		}
		
		return result;
	}
	
	@Override
	public void putAsset(String path, String assetName, InputStream asset) {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(((ByteArrayInputStream) asset).available());
		s3.putObject(new PutObjectRequest(bucket, getS3Path(path) + assetName, asset, meta));
	}
	
	private String getS3Path(String path) {
		//remove root path: /
		if (path.startsWith(FOLDER_SUFFIX)) {
			path = path.substring(1);
		}	
		
		return path + FOLDER_SUFFIX;
	}
}
