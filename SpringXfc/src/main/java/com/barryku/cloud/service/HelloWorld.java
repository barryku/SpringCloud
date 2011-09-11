package com.barryku.cloud.service;

import javax.jws.WebService;

@WebService
public interface HelloWorld {
	String sayHi(String msg);
}
