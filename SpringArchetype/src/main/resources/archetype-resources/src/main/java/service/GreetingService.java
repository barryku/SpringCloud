#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import ${package}.dao.UserDao;

public class GreetingService {
	private UserDao dao;
	private String userName;
	
	public String getGreet() {
		dao = dao == null ? new UserDao(userName) : dao;
		return "Hello " + dao.getUser();
	}
	
	public void setDao(UserDao dao) {
		this.dao = dao;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
