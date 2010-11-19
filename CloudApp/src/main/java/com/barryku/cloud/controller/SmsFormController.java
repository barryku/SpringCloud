package com.barryku.cloud.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.barryku.cloud.model.SmsText;
import com.barryku.cloud.controller.helper.TwilioSmsConfig;

@Controller
@RequestMapping("/sms")
@SessionAttributes("sms")
public class SmsFormController {
	private static Log log = LogFactory.getLog(SmsFormController.class);
	
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
	@Autowired
	protected UsernamePasswordCredentials credentials;
	
	@RequestMapping(method = RequestMethod.POST)
	public String sendSms(@ModelAttribute("sms") SmsText sms, BindingResult result, SessionStatus status) {	
		CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate.getRequestFactory();
		HttpClient client = factory.getHttpClient();
		client.getState().setCredentials(AuthScope.ANY, credentials);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("From", twilioConfig.getFromNumber());
		form.add("To", sms.getSendTo());
		form.add("Body", sms.getText());
		try {
			String response = restTemplate.postForObject(twilioConfig.getRestUrl(), form, String.class);
			log.debug(response);
		} catch (HttpClientErrorException re) {			
			log.info(re.getStatusText() + ":   " + re.getMessage());
		}

		return "smsComplete";
	}
}
