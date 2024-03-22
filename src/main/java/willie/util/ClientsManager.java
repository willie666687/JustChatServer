package willie.util;

import io.netty.channel.ChannelHandlerContext;
import willie.impl.Account;
import willie.impl.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientsManager{
	public static Map<ChannelHandlerContext, Client> clients = new HashMap<>();
	public static Client addClient(ChannelHandlerContext ctx){
		Client client = new Client(ctx);
		clients.put(ctx, client);
		return client;
	}
	public static void removeClient(Client client){
		for(Map.Entry<ChannelHandlerContext, Client> entry : clients.entrySet()){
			if(entry.getValue() == client){
				clients.remove(entry.getKey());
				return;
			}
		}
	}
	public static void removeClient(ChannelHandlerContext ctx){
		clients.remove(ctx);
	}
	public static Client getClient(ChannelHandlerContext ctx){
		return clients.get(ctx);
	}
	public static Client getClient(Account account){
		for(Client client : clients.values()){
			if(client.account == account){
				return client;
			}
		}
		return null;
	}
}
