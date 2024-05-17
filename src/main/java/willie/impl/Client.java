package willie.impl;

import io.netty.channel.ChannelHandlerContext;
import willie.Enum.ClientStatus;
import willie.Enum.ConnectionMessageType;
import willie.util.KeyUtils;

import java.security.PublicKey;
import java.util.Set;

public class Client{
	PublicKey publicKey;
	public ChannelHandlerContext ctx;

	public Client(ChannelHandlerContext ctx){
		this.ctx = ctx;
	}

	public void setPublicKey(PublicKey publicKey){
		this.publicKey = publicKey;
	}

	public PublicKey getPublicKey(){
		return publicKey;
	}

	public ClientStatus status;
	public Account account;

	public String encryptString(String s){
		try{
			return KeyUtils.encrypt(publicKey, s);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void setStatus(ClientStatus status){
		this.status = status;
	}

	public void sendEncryptedMessage(ConnectionMessageType type, String... messages){
		try{
			String[] encryptedMessages = new String[messages.length];
			for(int i = 0; i < messages.length; i++){
				encryptedMessages[i] = KeyUtils.encrypt(publicKey, messages[i]);
			}
			ctx.writeAndFlush(new ConnectionMessage(type, encryptedMessages));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendEncryptedMessage(ConnectionMessageType type, Set<String> messages){
		try{
			String[] encryptedMessages = new String[messages.size()];
			int i = 0;
			for(String s : messages){
				encryptedMessages[i] = KeyUtils.encrypt(publicKey, s);
				i++;
			}
			ctx.writeAndFlush(new ConnectionMessage(type, encryptedMessages));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendMessage(ConnectionMessageType type, String... messages){
		try{
			ctx.writeAndFlush(new ConnectionMessage(type, messages));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
