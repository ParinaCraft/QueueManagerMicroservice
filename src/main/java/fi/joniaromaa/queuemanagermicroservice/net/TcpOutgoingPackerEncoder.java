package fi.joniaromaa.queuemanagermicroservice.net;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpOutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TcpOutgoingPackerEncoder extends MessageToByteEncoder<TcpOutgoingPacket>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, TcpOutgoingPacket msg, ByteBuf out) throws Exception
	{
		out.writeBytes(msg.getBytes());
	}
}
