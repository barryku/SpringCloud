package com.barryku.gae.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import com.barryku.gae.controller.helper.TwilioSmsConfig;
import com.barryku.gae.model.SmsText;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;

@Controller
@RequestMapping("/sms")
@SessionAttributes("sms")
public class SmsController {
	private static Log log = LogFactory.getLog(SmsController.class);
	
	@Autowired
	private TwilioSmsConfig twilioConfig;
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model){
		SmsText sms = new SmsText();
		sms.setFrom(twilioConfig.getFromNumber());
		model.addAttribute("sms", sms);
		return "smsForm";
	}
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@RequestMapping(method = RequestMethod.POST)
	public String sendSms(@ModelAttribute("sms") SmsText sms) throws IOException  {
		URL url = new URL(twilioConfig.getRestUrl());
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
		URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();
		String userpass = twilioConfig.getSid() + ":" + twilioConfig.getAuthToken();
		String auth = Base64.encode(userpass.getBytes());
		
		request.setHeader(new HTTPHeader("Authorization", "Basic " + auth));
		String msg = "From=" + twilioConfig.getFromNumber() + "&" + 
			"To=" + sms.getSendTo() + "&Body=" + URLEncoder.encode(sms.getText(), "UTF-8");
		request.setPayload(msg.getBytes());
		HTTPResponse response = ufs.fetch(request);	
		log.info("response: " + new String(response.getContent()));		
		
		return "smsComplete";
	}	

}
