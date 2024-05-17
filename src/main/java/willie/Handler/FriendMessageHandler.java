package willie.Handler;

import willie.Enum.ConnectionMessageType;
import willie.impl.Account;
import willie.impl.Client;
import willie.util.AccountsManager;
import willie.util.ClientsManager;

import java.util.Set;

public class FriendMessageHandler{
	public static void handleAddFriendMessage(Client client, String argument){
		Account friend = AccountsManager.getAccount(argument);
		Account account = client.account;
		if(friend != null){
			if(account.friends.contains(friend)){
				client.sendEncryptedMessage(ConnectionMessageType.ADDFRIEND, "You are already friends with " + argument + ".");
			}else if(friend.friendRequests.contains(account)){
				client.sendEncryptedMessage(ConnectionMessageType.ADDFRIEND, "You already sent a friend request to " + argument + ".");
			}else if(friend == account){
				client.sendEncryptedMessage(ConnectionMessageType.ADDFRIEND, "You cannot add yourself as a friend.");
			}else{
				friend.addFriendRequest(account);
				client.sendEncryptedMessage(ConnectionMessageType.ADDFRIEND, "Friend request sent to " + argument + ".");
			}
		}else{
			client.sendEncryptedMessage(ConnectionMessageType.ADDFRIEND, "User does not exist.");
		}
	}

	public static void handleFriendListMessage(Client client){
		Set<Account> friends = client.account.friends;
		if(friends.isEmpty()){
			client.sendEncryptedMessage(ConnectionMessageType.FRIENDLIST, "You have no friends.");
		}else{
			String[] friendNames = new String[friends.size()];
			int i = 0;
			for(Account account : friends){
				friendNames[i] = account.username;
				i++;
			}
			client.sendEncryptedMessage(ConnectionMessageType.FRIENDLIST, friendNames);
		}
	}

	public static void handleFriendRequestMessage(Client client){
		Set<Account> friendRequests = client.account.friendRequests;
		if(friendRequests.isEmpty()){
			client.sendEncryptedMessage(ConnectionMessageType.FRIENDREQUEST);
		}else{
			String[] friendRequestNames = new String[friendRequests.size()];
			int i = 0;
			for(Account account : friendRequests){
				friendRequestNames[i] = account.username;
				i++;
			}
			client.sendEncryptedMessage(ConnectionMessageType.FRIENDREQUEST, friendRequestNames);
		}
	}

	public static void handleAcceptFriendMessage(Client client, String argument){
		Account friend = AccountsManager.getAccount(argument);
		Account account = client.account;
		if(friend != null){
			if(account.friendRequests.contains(friend)){
				account.addFriend(friend);
				friend.addFriend(account);
				account.removeFriendRequest(friend);
				client.sendEncryptedMessage(ConnectionMessageType.ACCEPTFRIEND, "Friend request accepted.");
			}else{
				client.sendEncryptedMessage(ConnectionMessageType.ACCEPTFRIEND, "No friend request from " + argument + ".");
			}
		}else{
			client.sendEncryptedMessage(ConnectionMessageType.ACCEPTFRIEND, "User does not exist.");
		}
	}

	public static void handleChatWithFriendMessage(Client client, String targetName, String message){
		Account friend = AccountsManager.getAccount(targetName);
		Account account = client.account;
		if(friend != null){
			if(account.friends.contains(friend)){
				client.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIEND, account.username, message);
				Client friendClient = ClientsManager.getClient(friend);
				if(friendClient != null){
					friendClient.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIEND, account.username, message);
				}
//				friend.addFriendChatMessage(account, account, message);
				account.addFriendChatMessage(friend, account, message);
			}else{
				client.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIENDDEBUG, "You are not friends with " + targetName + ".");
			}
		}else{
			client.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIENDDEBUG, "User does not exist.");
		}
	}

	public static void handleFriendChatHistoryMessage(Client client, String friend){
		Account account = client.account;
		Account friendAccount = AccountsManager.getAccount(friend);
		if(account == null){
			client.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIENDDEBUG, "You are not logged in.");
		}else if(friendAccount == null){
			client.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIENDDEBUG, "User does not exist.");
		}else if(!account.friends.contains(friendAccount)){
			client.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIENDDEBUG, "You are not friends with " + friend + ".");
		}else if(account.getChatHistory(friendAccount) == null){
			client.sendEncryptedMessage(ConnectionMessageType.FRIENDCHATHISTORY);
		}else{
			client.sendEncryptedMessage(ConnectionMessageType.FRIENDCHATHISTORY, account.getChatHistory(friendAccount).getChatHistoryString());
		}
	}
}
