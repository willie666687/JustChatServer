package willie.impl;

public class ChatMessage{
	public String message;
	public transient Account sender;
	public String senderUsername;
	public int timestamp;

	public ChatMessage(String message, Account sender){
		this.message = message;
		this.sender = sender;
		this.senderUsername = sender.username;
		timestamp = (int) (System.currentTimeMillis() / 1000);
	}

	public ChatMessage(String message, Account sender, int timestamp){
		this.message = message;
		this.sender = sender;
		this.senderUsername = sender.username;
		this.timestamp = timestamp;
	}
}
