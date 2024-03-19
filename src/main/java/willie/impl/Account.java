package willie.impl;

import java.util.HashSet;
import java.util.Set;

public class Account{
	public String username;
	public Set<Account> friends = new HashSet<>();
	public Set<Account> friendRequests = new HashSet<>();
	private String password;
	public Account(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public boolean checkPassword(String password){
		return this.password.equals(password);
	}
	public void addFriendRequest(Account friend){
		friendRequests.add(friend);
	}
	public void removeFriendRequest(Account friend){
		friendRequests.remove(friend);
	}
	public void addFriend(Account friend){
		friends.add(friend);
	}
	public void removeFriend(Account friend){
		friends.remove(friend);
	}
}
