package com.barryku.gae.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.io.IOUtils;

import com.barryku.gae.model.FileStream;
import com.barryku.gae.model.GoogleAssetFile;
import com.barryku.gae.service.RepositoryService;
import com.google.appengine.api.datastore.Blob;

public class RepositoryGoogleAppImpl implements RepositoryService {
	private static final Logger log = Logger.getLogger(RepositoryGoogleAppImpl.class.getName());
	private static PersistenceManager pm;
	@Override
	public FileStream getAssetByName(String path, String name)
			throws FileNotFoundException {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
		//PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select from " + GoogleAssetFile.class.getName() +
		        " where name == nameParam " +
		        "parameters String nameParam");
	    List<GoogleAssetFile> results = (List<GoogleAssetFile>)query.execute(name);
	    log.info("asset:" + results.get(0).getName());
	    Blob asset = results.get(0).getAsset();
	    byte[] assetBytes = asset.getBytes();
	    pm.close();
		return new FileStream(new ByteArrayInputStream(assetBytes), assetBytes.length);
	}

	@Override
	public List<String> getAssetList(String path) {
		//PersistenceManager pm = PMF.get().getPersistenceManager();
		if (pm == null || pm.isClosed())
			pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select from " + GoogleAssetFile.class.getName());
	    List<GoogleAssetFile> results = (List<GoogleAssetFile>)query.execute();
	    List<String> assets = new ArrayList<String>();
	    for (GoogleAssetFile asset:results) {
	    	assets.add(asset.getName());
	    }
	    pm.close();
		return assets;
	}

	@Override
	public void putAsset(String path, String assetName, InputStream asset) {
		try {
			Blob assetBlob = new Blob(IOUtils.toByteArray(asset));
			GoogleAssetFile assetFile = new GoogleAssetFile(assetName, assetBlob);
			//PersistenceManager pm = PMF.get().getPersistenceManager();
			if (pm == null || pm.isClosed())
				pm = PMF.get().getPersistenceManager();
			pm.makePersistent(assetFile);
			pm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
