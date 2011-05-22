package com.barryku.boxnet.controller;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.AbstractXPathTemplate;

import static com.barryku.boxnet.Constants.*;

@Controller
public class AuthenticationController {
	private static Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	@Autowired RestTemplate restTemplate;
	@Autowired AbstractXPathTemplate xpathTemplate; 
	
	@RequestMapping("/auth")
	public String doAuth(@RequestParam(value="requestUrl", required=false) String requestUrl,
			@RequestParam(value="authUrl", required=false) String authUrl) {
		log.debug("get request token via:" + requestUrl);
		Source response = restTemplate.getForObject(requestUrl, Source.class);
		String requestToken = xpathTemplate.evaluateAsString("//ticket", response);
		return "redirect:" + authUrl + requestToken; 
	}
	
	@RequestMapping("/boxnet")
	public String boxnetCallback(@RequestParam(value="auth_token") String authToken,
			HttpSession session) {
		session.setAttribute(SESSION_AUTH_TOKEN, authToken);
		String action = (String) session.getAttribute(SESSION_ACTION);
		log.info("after auth, forward to:" + action);
		return "forward:/" + MVC_CONTEXT + action; 
	}
}
