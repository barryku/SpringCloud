#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

public class UserDao {
	private String user;
	
	public UserDao(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return "VIP " + user;
	}
}
