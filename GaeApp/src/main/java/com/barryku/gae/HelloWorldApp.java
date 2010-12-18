package com.barryku.gae;

import com.barryku.gae.service.GreetingService;
import com.barryku.gae.view.TextView;

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
