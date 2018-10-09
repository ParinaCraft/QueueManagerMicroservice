package fi.joniaromaa.queuemanagermicroservice.communication.udp.outgoing;

import fi.joniaromaa.queuemanagermicroservice.communication.udp.UdpOutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SendPlayerOutgoingPacket implements UdpOutgoingPacket
{
	private static final int PACKET_ID = 0;
	
	private int queueId;
	private int serverId;
	
	public SendPlayerOutgoingPacket(int queueId, int serverId)
	{
		this.queueId = queueId;
		this.serverId = serverId;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte(SendPlayerOutgoingPacket.PACKET_ID);
		buf.writeInt(this.queueId);
		buf.writeInt(this.serverId);
		return buf;
	}
}
