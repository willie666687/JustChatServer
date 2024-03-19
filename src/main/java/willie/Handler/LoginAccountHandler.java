package willie.Handler;

import willie.Enum.ConnectionMessageType;
import willie.impl.Client;
import willie.Enum.ClientStatus;
import willie.util.AccountsManager;

public class LoginAccountHandler{
	public static void clientLoginAccount(Client client, String username, String password){
		if(RegisterAccountHandler.accountExists(username)){
			if(AccountsManager.accounts.get(username).checkPassword(password)){
				client.sendEncryptedMessage(ConnectionMessageType.LOGIN, "Login successful.");
				client.account = AccountsManager.accounts.get(username);
				client.status = ClientStatus.LOGINED;
			}else{
				client.sendEncryptedMessage(ConnectionMessageType.LOGIN, "Incorrect password.");
			}
		}else{
			client.sendEncryptedMessage(ConnectionMessageType.LOGIN, "Account does not exist.");
		}
	}
}
