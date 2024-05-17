package willie.thread;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import willie.Handler.ConnectionMessageHandler;
import willie.util.MessageDecoder;
import willie.util.MessageEncoder;

public class ConnectionThread extends Thread{
	String host;
	int port;

	public ConnectionThread(String host, int port){
		this.host = host;
		this.port = port;
	}

	ConnectionMessageHandler serverHandler = new ConnectionMessageHandler();

	@Override
	public void run(){
		startServer();
	}

	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void startServer(){
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>(){
						@Override
						public void initChannel(SocketChannel ch){
							ch.pipeline().addLast(new MessageDecoder(), new MessageEncoder(), serverHandler);
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)          // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // (7)

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
