#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ui;

public class TextUi implements HelloUi{
	private String message;
	
	public String display() {
		return "*****  " + message + "  *****";
	}
	
	public void setMessage(String msg) {
		message = msg;
	}

}
