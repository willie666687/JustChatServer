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
	public void channelActive(final ChannelHandlerContext ctx){
		Client client = ClientsManager.addClient(ctx);
		client.setStatus(ClientStatus.CONNECTED);
		client.sendMessage(ConnectionMessageType.KEYEXCHANGE, Base64.getEncoder().encodeToString(KeyUtils.publicKey.getEncoded()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
//		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx){
		ClientsManager.removeClient(ctx);
		ctx.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		if(!(msg instanceof ConnectionMessage message)){
			return;
		}
		if(ClientsManager.getClient(ctx).status != ClientStatus.CONNECTED){
			DebugOutput.printArray(2, "Received " + message.type.toString() + " : ", KeyUtils.decryptMessages(message.messages));
		}
		switch(message.type){
			case KEYEXCHANGE -> {
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
			}
//			case DEBUGENCRYPTED -> {
//				DebugOutput.printArray("Decrypted message: ", KeyUtils.decryptMessages(message.messages));
//			}
			case REGISTER -> {
				RegisterAccountHandler.clientRegisterAccount(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]), KeyUtils.decrypt(message.messages[1]));
			}
			case LOGIN -> {
				LoginAccountHandler.clientLoginAccount(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]), KeyUtils.decrypt(message.messages[1]));
			}
			case ADDFRIEND -> {
				FriendMessageHandler.handleAddFriendMessage(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]));
			}
			case FRIENDLIST -> {
				FriendMessageHandler.handleFriendListMessage(ClientsManager.getClient(ctx));
			}
			case FRIENDREQUEST -> {
				FriendMessageHandler.handleFriendRequestMessage(ClientsManager.getClient(ctx));
			}
			case LOGOUT -> {
				Client client = ClientsManager.getClient(ctx);
				client.setStatus(ClientStatus.KEYEXCHANGED);
				client.account = null;
				client.sendEncryptedMessage(ConnectionMessageType.LOGOUT);
			}
			case ACCEPTFRIEND -> {
				FriendMessageHandler.handleAcceptFriendMessage(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]));
			}
			case CHATWITHFRIEND -> {
				FriendMessageHandler.handleChatWithFriendMessage(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]), KeyUtils.decrypt(message.messages[1]));
			}
			case FRIENDCHATHISTORY -> {
				FriendMessageHandler.handleFriendChatHistoryMessage(ClientsManager.getClient(ctx), KeyUtils.decrypt(message.messages[0]));
			}
		}
	}
}
