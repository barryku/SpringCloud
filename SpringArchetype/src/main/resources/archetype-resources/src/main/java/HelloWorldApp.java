#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.service.GreetingService;
import ${package}.view.TextView;

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
