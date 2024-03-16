package willie.Handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import willie.Enum.ConnectionMessageType;
import willie.impl.ConnectionMessage;

import java.util.HashSet;
import java.util.Set;
@ChannelHandler.Sharable
public class ConnectionMessageHandler extends ChannelInboundHandlerAdapter{
	Set<ChannelHandlerContext> clients = new HashSet<>();
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		sendMessage(ctx, "Hello", "World");
		clients.add(ctx);
		System.out.println("Sent to: " + ctx.name() + ",Channel ID: " + ctx.channel().id().asLongText());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx){
		clients.remove(ctx);
		ctx.writeAndFlush("Goodbye").addListener(ChannelFutureListener.CLOSE);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(!(msg instanceof ConnectionMessage message)){
			return;
		}
		System.out.println("Received type: " +  message.type);
		for(String s : message.messages){
			System.out.println("Received message: " + s);
		}
	}
	public void sendMessage(ChannelHandlerContext ctx, String... messages){
		try{
			ctx.writeAndFlush(new ConnectionMessage(ConnectionMessageType.LOGIN, messages));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
