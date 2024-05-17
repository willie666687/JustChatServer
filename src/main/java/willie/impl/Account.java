package willie.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Account{
	public String username;
	public transient Set<Account> friends = new HashSet<>();
	public Set<String> friendsUsername = new HashSet<>();
	public transient Set<Account> friendRequests = new HashSet<>();
	public Set<String> friendRequestsUsername = new HashSet<>();
	private String password;
	public transient Map<Account, ChatHistory> chatHistories = new HashMap<>();
	public Map<String, ChatHistory> chatHistoriesUsername = new HashMap<>();

	public Account(String username, String password){
		this.username = username;
		this.password = password;
	}

	public boolean checkPassword(String password){
		return this.password.equals(password);
	}

	public void addFriendRequest(Account friend){
		friendRequests.add(friend);
		friendRequestsUsername.add(friend.username);
	}

	public void removeFriendRequest(Account friend){
		friendRequests.remove(friend);
		friendRequestsUsername.remove(friend.username);
	}

	public void addFriend(Account friend){
		friends.add(friend);
		friendsUsername.add(friend.username);
	}

	public void removeFriend(Account friend){
		friends.remove(friend);
		friendsUsername.remove(friend.username);
	}

	public void addChatHistory(Account friend){
		if(chatHistories.containsKey(friend)){
			return;
		}
		ChatHistory chatHistory = new ChatHistory();
		chatHistories.put(friend, chatHistory);
		chatHistoriesUsername.put(friend.username, chatHistory);
		friend.addChatHistory(this, chatHistory);
	}

	public void addChatHistory(Account friend, ChatHistory chatHistory){
		if(chatHistories.containsKey(friend)){
			return;
		}
		chatHistories.put(friend, chatHistory);
		chatHistoriesUsername.put(friend.username, chatHistory);
		friend.addChatHistory(this, chatHistory);
	}

	public ChatHistory getChatHistory(Account friend){
		return chatHistories.get(friend);
	}

	public void addFriendChatMessage(Account friend, Account sender, String message){
		if(!chatHistories.containsKey(friend)){
			addChatHistory(friend);
		}
		chatHistories.get(friend).addChatMessage(message, sender);
	}

	public void addFriendChatMessage(Account friend, Account sender, String message, int timestamp){
		if(!chatHistories.containsKey(friend)){
			addChatHistory(friend);
		}
		chatHistories.get(friend).addChatMessage(message, sender, timestamp);
	}
}
