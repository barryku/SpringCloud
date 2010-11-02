#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HelloWorldAppTest {

	private HelloWorldApp app;
	
	@Before
	public void setUp() throws Exception {
		app = new HelloWorldApp(); 
	}

	@Test
	public void testXyz() {
	}

}
