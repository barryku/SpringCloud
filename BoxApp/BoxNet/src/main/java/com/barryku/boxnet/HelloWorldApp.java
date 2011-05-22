package com.barryku.boxnet;

import com.barryku.boxnet.service.GreetingService;
import com.barryku.boxnet.view.TextView;

public class HelloWorldApp {
	private GreetingService service = new GreetingService();
	private TextView ui = new TextView();
	
	public static void main(String[] args) {
		HelloWorldApp app = new HelloWorldApp();
		app.sayHello();		
	}	
	
	public void sayHello(){
		ui.setMessage(service.getGreet());
		System.out.println(ui.display());
	}
}
