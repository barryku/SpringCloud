#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ui;

public class HtmlUi implements HelloUi{
	private String message;
	
	public String display() {
		String display = "<html>\n<body>\n<h3>" +
			message + "</h3>\n</body>\n</html>";
		return display;
	}
	
	public void setMessage(String msg) {
		message = msg;
	}

}
