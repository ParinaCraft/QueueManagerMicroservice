package fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing;

import java.util.UUID;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RequestPlayerSlotsOutgoingPacket implements TcpOutgoingPacket
{
	private static final int PACKET_ID = 2;
	
	private UUID[] uuids;
	
	public RequestPlayerSlotsOutgoingPacket(UUID... uuids)
	{
		this.uuids = uuids;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(RequestPlayerSlotsOutgoingPacket.PACKET_ID);
		buf.writeInt(this.uuids.length);
		for(UUID uuid : this.uuids)
		{
			ByteBufUtils.writeUUID(buf, uuid);
		}
		
		return buf;
	}
}
