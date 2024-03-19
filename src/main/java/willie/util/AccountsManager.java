package willie.util;

import willie.impl.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountsManager{
	public static Map<String, Account> accounts = new HashMap<>();
	public static void addAccount(String username, String password){
		Account account = new Account(username, password);
		accounts.put(username, account);
	}
	public static Account getAccount(String username){
		return accounts.get(username);
	}
	public static void removeAccount(String username){
		accounts.remove(username);
	}
}
