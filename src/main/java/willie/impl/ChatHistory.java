package willie.impl;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory{
	public List<ChatMessage> chatHistory = new ArrayList<>();
	public void addChatMessage(String message, Account sender){
		chatHistory.add(new ChatMessage(message, sender));
	}
	public void addMessage(ChatMessage message){
		chatHistory.add(message);
	}
	public String[] getChatHistoryString(){
		String[] chatHistory = new String[this.chatHistory.size()];
		for(int i = 0; i < this.chatHistory.size(); i++){
			chatHistory[i] = this.chatHistory.get(i).sender.username + ": " + this.chatHistory.get(i).message;
		}
		return chatHistory;
	}
}
