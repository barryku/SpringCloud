package com.barryku.cloud.service.helper;

import java.io.InputStream;

import org.apache.http.entity.mime.content.InputStreamBody;

public class InputStreamBodyWithSize extends InputStreamBody {
	private int length;
	public InputStreamBodyWithSize(	final InputStream in, final String filename, final int length) {
		super(in, filename);
		this.length = length;
	}

	@Override
	public long getContentLength() {
		return this.length;
	}

}
