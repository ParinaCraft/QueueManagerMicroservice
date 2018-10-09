package fi.joniaromaa.queuemanagermicroservice.communication.udp;

import io.netty.buffer.ByteBuf;

public interface UdpOutgoingPacket
{
	public ByteBuf getBytes();
}
