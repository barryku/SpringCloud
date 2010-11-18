package com.barryku.cloud.dao;

import com.barryku.cloud.model.User;

public class UserDao {
	private static User[] users = new User[3]; 
	static {
		users[0] = new User("Doe", "John");
		users[1] = new User("Simpson", "Bart");
		users[2] = new User("Kawasaki", "Ken");
	}
	
	public User getUser() {
		return users[0];
	}
	
	public User getUser(int i) {
		User user = null;
		if (i > -1 && i < 3) {
			user = users[i];
		}
		return user;
	}
}
