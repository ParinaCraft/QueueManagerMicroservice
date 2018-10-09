package fi.joniaromaa.queuemanagermicroservice.net;

import java.util.concurrent.TimeUnit;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpPacketManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;

public class TcpNetworkManager
{
	@Getter private TcpPacketManager tcpPacketManager;
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	public TcpNetworkManager()
	{
		this.tcpPacketManager = new TcpPacketManager();
		
		this.bossGroup = new NioEventLoopGroup(1);
		this.workerGroup = new NioEventLoopGroup();
	}
	
	public void start() throws InterruptedException
	{
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(this.bossGroup, this.workerGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100)
		.childOption(ChannelOption.TCP_NODELAY, true)
		.childHandler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception
			{
				//Incoming
				socketChannel.pipeline().addLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
				socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Short.MAX_VALUE, 0, 2, 0, 2));
				socketChannel.pipeline().addLast(new TcpPacketDataHandler());
				
				//Outgoing
				socketChannel.pipeline().addLast(new LengthFieldPrepender(2));
				socketChannel.pipeline().addLast(new TcpOutgoingPackerEncoder());
			}
		});

		serverBootstrap.bind("127.0.0.1", 7649).sync();
	}
	
	public void stop()
	{
		this.bossGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
	}
}
