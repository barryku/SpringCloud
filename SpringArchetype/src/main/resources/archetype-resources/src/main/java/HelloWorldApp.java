#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.service.GreetingService;
import ${package}.ui.TextUi;
import ${package}.ui.HelloUi;

public class HelloWorldApp {
	private GreetingService service = new GreetingService();
	private HelloUi ui = new TextUi();
	
	public static void main(String[] args) {
		HelloWorldApp app = new HelloWorldApp();
		if (args.length > 0) {
			app.sayHelloTo(args[0]);
		} else {
			app.sayHelloTo("World");
		}
		
	}	
	
	public void sayHelloTo(String user){
		service.setUserName(user);
		ui.setMessage(service.getGreet());
		System.out.println(ui.display());
	}
}
