package willie.impl;

public class ChatMessage{
	public String message;
	public Account sender;
	public int timestamp;
	public ChatMessage(String message, Account sender){
		this.message = message;
		this.sender = sender;
		timestamp = (int) (System.currentTimeMillis() / 1000);
	}
}
