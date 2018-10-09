package fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpOutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GameRegisteredOutgoingPacket implements TcpOutgoingPacket
{
	private static final int PACKET_ID = 0;
	
	public GameRegisteredOutgoingPacket()
	{
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(GameRegisteredOutgoingPacket.PACKET_ID);
		return buf;
	}
}
