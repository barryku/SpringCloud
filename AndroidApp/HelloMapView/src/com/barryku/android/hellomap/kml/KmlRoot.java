package com.barryku.android.hellomap.kml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="kml")
public class KmlRoot {
	@Element(name="Document")
	private KmlDocument document;

	public void setDocument(KmlDocument document) {
		this.document = document;
	}

	public KmlDocument getDocument() {
		return document;
	}
	
}
