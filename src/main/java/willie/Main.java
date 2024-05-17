package willie;

import com.google.gson.internal.LinkedTreeMap;
import willie.impl.Account;
import willie.impl.ChatHistory;
import willie.impl.ChatMessage;
import willie.thread.ConnectionThread;
import willie.util.AccountsManager;
import willie.util.DataManager;
import willie.util.DebugOutput;
import willie.util.KeyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main{
	public static String host = "127.0.0.1";
	public static int port;

	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the host port: ");
		port = scanner.nextInt();
		System.out.println();
		DataManager.createFile(DataManager.UserData);
		Object data = DataManager.readData(DataManager.UserData, AccountsManager.accounts);
		if(data != null){
			if(data instanceof HashMap){
				Map<String, LinkedTreeMap> accountMap = (HashMap) data;
//                AccountsManager.accounts = accountMap;
				accountMap.forEach((key, value) -> {
					DebugOutput.print("Key: " + key + " Value: " + value.get("password"));
					Account account = AccountsManager.addAccount(key, (String) value.get("password"));
				});
				accountMap.forEach((key, value) -> {
					Account account = AccountsManager.getAccount(key);
					ArrayList<String> friends = (ArrayList) value.get("friendsUsername");
					if(friends != null){
						friends.forEach((friend) -> {
							Account friendAccount = AccountsManager.getAccount(friend);
							account.addFriend(friendAccount);
							DebugOutput.print("Friend: " + friend);
						});
					}
					ArrayList<String> friendRequests = (ArrayList) value.get("friendRequestsUsername");
					if(friendRequests != null){
						friendRequests.forEach((friendRequest) -> {
							Account friendRequestAccount = AccountsManager.getAccount(friendRequest);
							account.addFriendRequest(friendRequestAccount);
							DebugOutput.print("Friend Request: " + friendRequest);
						});
					}
					LinkedTreeMap chatHistories = (LinkedTreeMap) value.get("chatHistoriesUsername");
					if(chatHistories != null){
						chatHistories.forEach((friendUsername, v) -> {
							Account friendAccount = AccountsManager.getAccount((String) friendUsername);
							ChatHistory chatHistory = new ChatHistory();
							LinkedTreeMap context = (LinkedTreeMap) v;
							ArrayList chatMessagesList = (ArrayList) context.get("chatHistory");
							chatMessagesList.forEach(value2 -> {
								LinkedTreeMap chatMessageMap = (LinkedTreeMap) value2;
								ChatMessage chatMessage = new ChatMessage((String) chatMessageMap.get("message"), AccountsManager.getAccount((String) chatMessageMap.get("senderUsername")), (int) (double) chatMessageMap.get("timestamp"));
								chatHistory.addMessage(chatMessage);
								DebugOutput.print("Chat Message: " + chatMessage.message);
							});
							account.addChatHistory(friendAccount, chatHistory);
						});
					}
				});
				if(AccountsManager.accounts.isEmpty()){
					DebugOutput.print("AccountsManager is empty");
				}
			}else{
				DebugOutput.printError("Data is not an instance of HashMap");
			}
		}else{
			DebugOutput.printError("Data is null");
		}
//        Account willieAccount = AccountsManager.addAccount("willie", "qwer1234");
//        Account davidAccount = AccountsManager.addAccount("david", "qwer1234");
//        willieAccount.addFriend(AccountsManager.getAccount("david"));
//        davidAccount.addFriend(AccountsManager.getAccount("willie"));
//        willieAccount.addChatHistory(davidAccount);
//        willieAccount.addFriendChatMessage(davidAccount, willieAccount, "Hello David");
//        willieAccount.addFriendChatMessage(davidAccount, willieAccount, "eee");

//        DataManager.writeData(DataManager.UserData, AccountsManager.accounts);

		KeyUtils.generateKey();
		String encrypt;
		try{
			encrypt = KeyUtils.encrypt(KeyUtils.publicKey, "sussy");
//            System.out.println("PublicKey: " + KeyUtils.publicKey);
//            System.out.println("Encrypt: " + encrypt);
//            System.out.println("Decrypt: " + KeyUtils.decrypt(encrypt));
		}catch(Exception e){
			e.printStackTrace();
		}
		ConnectionThread connectionThread = new ConnectionThread(host, port);
		connectionThread.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			DebugOutput.print("Shutting down...");
			DataManager.writeData(DataManager.UserData, AccountsManager.accounts);
		}));
	}
}