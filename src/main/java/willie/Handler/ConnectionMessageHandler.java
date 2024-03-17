package willie.Handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import willie.Enum.ClientStatus;
import willie.Enum.ConnectionMessageType;
import willie.impl.Client;
import willie.impl.ConnectionMessage;
import willie.util.ClientsManager;
import willie.util.DebugOutput;
import willie.util.KeyUtils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ChannelHandler.Sharable
public class ConnectionMessageHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		Client client = ClientsManager.addClient(ctx);
		client.setStatus(ClientStatus.CONNECTED);
		client.sendMessage(ConnectionMessageType.KEYEXCHANGE, Base64.getEncoder().encodeToString(KeyUtils.publicKey.getEncoded()));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx){
		ClientsManager.removeClient(ctx);
		ctx.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(!(msg instanceof ConnectionMessage message)){
			return;
		}
		DebugOutput.printArray(2, "Received: ", message.messages);
		if(message.type == ConnectionMessageType.KEYEXCHANGE){
			byte[] publicBytes = Base64.getDecoder().decode(message.messages[0]);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			try{
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				PublicKey pubKey = keyFactory.generatePublic(keySpec);
				Client client = ClientsManager.getClient(ctx);
				client.setPublicKey(pubKey);
				ClientsManager.getClient(ctx).setStatus(ClientStatus.KEYEXCHANGED);
			}catch(NoSuchAlgorithmException | InvalidKeySpecException e){
				DebugOutput.printError("Error while setting public key: " + e.getMessage());
			}
		}else if(message.type == ConnectionMessageType.DEBUGENCRYPTED){
			DebugOutput.printArray(2, "Received encrypted message: ", message.messages);
			DebugOutput.printArray("Decrypted message: ", decryptMessages(message.messages));
		}else if(message.type == ConnectionMessageType.REGISTER){
			RegisterAccountHandler.clientRegisterAccount(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]), KeyUtils.decrypt(message.messages[1]));
			DebugOutput.printArray(1, "Received register message: ", decryptMessages(message.messages));
		}else if(message.type == ConnectionMessageType.LOGIN){
			LoginAccountHandler.clientLoginAccount(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]), KeyUtils.decrypt(message.messages[1]));
			DebugOutput.printArray(1, "Received login message: ", decryptMessages(message.messages));
			
		}
	}
	public String[] decryptMessages(String[] messages){
		String[] decryptedMessages = new String[messages.length];
		for(int i = 0; i < messages.length; i++){
			try{
				decryptedMessages[i] = KeyUtils.decrypt(messages[i]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return decryptedMessages;
	}
}
