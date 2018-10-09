package fi.joniaromaa.queuemanagermicroservice.net;

import fi.joniaromaa.queuemanagermicroservice.QueueManagerMicroservice;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpPacketDataHandler extends SimpleChannelInboundHandler<DatagramPacket> 
{
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception
	{
		QueueManagerMicroservice.getUdpNetworkManager().getUdpPacketManager().handleIncoming(ctx.channel(), msg);
	}
}
