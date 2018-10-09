package fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpOutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UpdateGameOKOutgoingPacket implements TcpOutgoingPacket
{
	private static final int PACKET_ID = 1;
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(UpdateGameOKOutgoingPacket.PACKET_ID);
		return buf;
	}
}
