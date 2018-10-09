package fi.joniaromaa.queuemanagermicroservice.communication.tcp;

import fi.joniaromaa.queuemanagermicroservice.net.TcpPacketDataHandler;
import io.netty.buffer.ByteBuf;

public interface TcpIncomingPacket
{
	public void handle(TcpPacketDataHandler handler, ByteBuf buf);
}
