package fi.joniaromaa.queuemanagermicroservice.communication.tcp;

import io.netty.buffer.ByteBuf;

public interface TcpOutgoingPacket
{
	public ByteBuf getBytes();
}
