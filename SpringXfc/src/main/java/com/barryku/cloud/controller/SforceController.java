package com.barryku.cloud.controller;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Element;

import com.barryku.cloud.model.SpringProperties;
import com.barryku.cloud.sforce.partner.QueryResult;
import com.barryku.cloud.sforce.partner.SessionHeader;
import com.barryku.cloud.sforce.partner.SforceService;
import com.barryku.cloud.sforce.partner.Soap;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectorConfig;


@Controller
public class SforceController {
	@Autowired
	private SpringProperties springProperties;
	
	@RequestMapping("/sforcetest")
	public void createAccount() throws Exception {
		ConnectorConfig config = new ConnectorConfig();
        config.setUsername(springProperties.getUserName());
        config.setPassword(springProperties.getPassword() + springProperties.getSecurityToken());

        PartnerConnection connection = Connector.newConnection(config);
        SObject account = new SObject();
        account.setType("Book__c");
        account.setField("Name", "It works with cutom objects");        
        connection.create(new SObject[]{account});
	}
	
	@RequestMapping("/sforcetest2")
	public void createAccount2() throws Exception {
		SforceService service = new SforceService();
		Soap port = service.getSoap();

		com.barryku.cloud.sforce.partner.LoginResult login = port.login(springProperties.getUserName(), 
				springProperties.getPassword() + springProperties.getSecurityToken());
		String url = login.getServerUrl();
		String sessionId = login.getSessionId();
		
		Soap soapPort = setServerUrlAndSessionId(port, url, sessionId);
		QueryResult result = soapPort.queryAll("SELECT Id, Name, Type__c FROM Book__c");
		System.out.println("url:" + url);
		System.out.println("sessionID:" + sessionId);
		com.barryku.cloud.sforce.partner.SObject newSobj = null;
		for (com.barryku.cloud.sforce.partner.SObject sobj:result.getRecords()) {
			
			for (Object o : sobj.getAny()) {
				Element element = (Element) o;
				System.out.println(element.getLocalName() + "-----" + 
						(element.getFirstChild()== null ? "nothing here" : element.getFirstChild().getNodeValue()));
			}
			newSobj = sobj;
			
		}
		
		//set ID to null, so it can be created as new object
		newSobj.setId(null);
		newSobj.setType("Book__c");
		for (Object o:newSobj.getAny()) {
			
			Element element = (Element) o;
			if ("Name".equals(element.getLocalName())) {
				System.out.println("...." + o);
				element.getFirstChild().setNodeValue("test book " + Math.random());
			} else if ("Type__c".equals(element.getLocalName())) {
				System.out.println("...." + o);
				element.getFirstChild().setNodeValue("Novel");
			}
		}
		List<com.barryku.cloud.sforce.partner.SObject> objs = new ArrayList<com.barryku.cloud.sforce.partner.SObject>();
		objs.add(newSobj);
		port.create(objs);
		
	}
	
	private Soap setServerUrlAndSessionId(Soap port, String url, String sessionId) throws JAXBException {
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		SessionHeader header = new SessionHeader();
		header.setSessionId(sessionId);
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header(new QName("urn:partner.soap.sforce.com", "SessionHeader"), header, new JAXBDataBinding(SessionHeader.class)));
		bp.getRequestContext().put(Header.HEADER_LIST, headers);
		return (Soap) bp;
	}
}
