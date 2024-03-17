package willie.Handler;

import willie.Enum.ConnectionMessageType;
import willie.impl.Client;
import willie.Enum.ClientStatus;

public class LoginAccountHandler{
	public static void clientLoginAccount(Client client, String username, String password){
		if(RegisterAccountHandler.accountExists(username)){
			if(RegisterAccountHandler.accounts.get(username).equals(password)){
				client.sendEncryptedMessage(ConnectionMessageType.LOGIN, "Login successful.");
				client.username = username;
				client.status = ClientStatus.LOGINED;
			}else{
				client.sendEncryptedMessage(ConnectionMessageType.LOGIN, "Incorrect password.");
			}
		}else{
			client.sendEncryptedMessage(ConnectionMessageType.LOGIN, "Account does not exist.");
		}
	}
}
