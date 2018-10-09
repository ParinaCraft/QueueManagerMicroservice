package fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing;

import java.util.UUID;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RequestSpectateOutgoingPacket implements TcpOutgoingPacket
{
	private static final int PACKET_ID = 3;
	
	private UUID unqiueId;
	
	public RequestSpectateOutgoingPacket(UUID unqiueId)
	{
		this.unqiueId = unqiueId;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(RequestSpectateOutgoingPacket.PACKET_ID);
		ByteBufUtils.writeUUID(buf, this.unqiueId);
		
		return buf;
	}
}
