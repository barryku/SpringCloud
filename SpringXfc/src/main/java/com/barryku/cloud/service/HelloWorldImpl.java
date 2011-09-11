package com.barryku.cloud.service;

import javax.jws.WebService;

@WebService(endpointInterface = "com.barryku.cloud.service.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

	public String sayHi(String msg) {
		return "hello " + msg;
	}

}
