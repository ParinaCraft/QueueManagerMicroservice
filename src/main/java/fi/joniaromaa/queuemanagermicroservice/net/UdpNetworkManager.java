package fi.joniaromaa.queuemanagermicroservice.net;

import fi.joniaromaa.queuemanagermicroservice.communication.udp.UdpPacketManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Getter;

public class UdpNetworkManager
{
	@Getter private UdpPacketManager udpPacketManager;
	
	private EventLoopGroup bossGroup;
	//private Channel channel;
	
	public UdpNetworkManager()
	{
		this.udpPacketManager = new UdpPacketManager();
		
		this.bossGroup = new NioEventLoopGroup(1);
	}
	
	public void start() throws InterruptedException
	{
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(this.bossGroup)
		.channel(NioDatagramChannel.class)
		.option(ChannelOption.SO_BROADCAST, true)
		.handler(new UdpPacketDataHandler());
		
		/*this.channel = */bootstrap.bind(7650).sync().channel();
	}
}
