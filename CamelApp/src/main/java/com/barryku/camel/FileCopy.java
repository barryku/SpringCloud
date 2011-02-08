package com.barryku.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FileCopy {
	
	public static void main(String... args) throws Exception {
		ApplicationContext springContext = new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
		CamelContext context = (CamelContext) springContext.getBean("camelContext");
		context.addComponent("s3file", (Component) springContext.getBean("s3FileComponent"));
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("s3file://data/inbox?noop=false").beanRef("gsFileManager", "process");
				
			}
		});
		
		context.start();
		Thread.sleep(3000);
		context.stop();
	}
}
