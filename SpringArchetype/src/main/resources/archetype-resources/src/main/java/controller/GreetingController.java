#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ${package}.service.GreetingService;

@Controller
public class GreetingController {
	
	@Autowired
	private GreetingService service;
	
	@RequestMapping("/hello")
	public String getAssetList(Model model) {
		model.addAttribute("message", service.getGreetForUser(2));
		return "htmlView";
	}	
}
