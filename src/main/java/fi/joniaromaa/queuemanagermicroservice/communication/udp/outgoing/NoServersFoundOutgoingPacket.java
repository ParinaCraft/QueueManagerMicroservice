package fi.joniaromaa.queuemanagermicroservice.communication.udp.outgoing;

import fi.joniaromaa.queuemanagermicroservice.communication.udp.UdpOutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NoServersFoundOutgoingPacket implements UdpOutgoingPacket
{
	private static final int PACKET_ID = 1;
	
	private int queueId;
	
	public NoServersFoundOutgoingPacket(int queueId)
	{
		this.queueId = queueId;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte(NoServersFoundOutgoingPacket.PACKET_ID);
		buf.writeInt(this.queueId);
		return buf;
	}
}
