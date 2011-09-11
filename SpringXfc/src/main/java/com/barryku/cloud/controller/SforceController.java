package com.barryku.cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.*;
import com.sforce.ws.*;


@Controller
public class SforceController {
	@RequestMapping("/sforcetest")
	public void createAccount() throws Exception {
		ConnectorConfig config = new ConnectorConfig();
        config.setUsername("mypixo@yahoo.com");
        config.setPassword("MwP8lbh1NsGUYqdbZJH4" + "9KgF9GYiDr0dQcdo7pwlckG8");

        PartnerConnection connection = Connector.newConnection(config);
        SObject account = new SObject();
        account.setType("Book__c");
        account.setField("Name", "It works with cutom objects");
        connection.create(new SObject[]{account});
	}
}
