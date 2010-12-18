package com.barryku.gae.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class GoogleAssetFile {
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey
    private Long id;

    @Persistent
    private String name;

    @Persistent
    Blob asset;

    public GoogleAssetFile() { }
    public GoogleAssetFile(String name, Blob asset) {
        this.name = name; 
        this.asset = asset;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Blob getAsset()              { return asset; }
    public void setAsset(Blob asset)    { this.asset = asset; }

}

