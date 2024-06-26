package willie.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import willie.impl.ConnectionMessage;

public class MessageEncoder extends MessageToByteEncoder<ConnectionMessage>{
	@Override
	protected void encode(ChannelHandlerContext ctx, ConnectionMessage msg, ByteBuf out){
		int totalLength = msg.totalLength + 8;
		out.writeInt(totalLength);

		int typeLength = msg.type.toString().getBytes().length;
		out.writeInt(typeLength);

		String type = msg.type.toString();
		out.writeBytes(type.getBytes());

		int messageAmount = msg.messageAmount;
		out.writeInt(messageAmount);

		String[] messages = msg.messages;
		for(String s : messages){
			out.writeInt(s.getBytes().length);
			out.writeBytes(s.getBytes());
		}

//		System.out.println("Sent: " + msg.type + " " + Arrays.toString(msg.messages));
		DebugOutput.printArray(2, "Sent: ", msg.messages);
	}
}
