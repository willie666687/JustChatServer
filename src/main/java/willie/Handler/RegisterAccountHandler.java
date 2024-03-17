package willie.Handler;

import willie.Enum.ClientStatus;
import willie.Enum.ConnectionMessageType;
import willie.impl.Client;

import java.util.HashMap;
import java.util.Map;

public class RegisterAccountHandler{
	public static final Map<String, String> accounts = new HashMap<>();
	public static void clientRegisterAccount(Client client, String username, String password){
		if(client.status != ClientStatus.KEYEXCHANGED){
			client.sendMessage(ConnectionMessageType.DEBUG, "You are not allowed to register an account at this time.");
			return;
		}
		if(accountExists(username)){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Account already exists.");
			return;
		}
		if(username.length() < 3 || username.length() > 16){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Username must be between 3 and 16 characters.");
			return;
		}
		if(username.contains(" ")){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Username cannot contain spaces.");
			return;
		}
		if(!username.matches("^[_0-9a-zA-Z\\s]+$")){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Username can only contain letters, numbers, and underscores.");
			return;
		}
		if(password.length() < 8 || password.length() > 32){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Password must be between 8 and 32 characters.");
			return;
		}
		if(password.contains(" ")){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Password cannot contain spaces.");
			return;
		}
		if(!password.matches("^[_0-9a-zA-Z\\s]+$")){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Password can only contain letters, numbers, and underscores.");
			return;
		}
		if(!(password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*"))){
			client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Password must contain at least one letter and one number.");
			return;
		}
		accounts.put(username, password);
		client.sendEncryptedMessage(ConnectionMessageType.REGISTER, "Account created successfully.");
	}
	public static Boolean accountExists(String username){
		return accounts.containsKey(username);
	}
}
