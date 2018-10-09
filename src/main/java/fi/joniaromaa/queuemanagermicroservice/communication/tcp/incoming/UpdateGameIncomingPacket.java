package fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing.UpdateGameOKOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.net.TcpPacketDataHandler;
import io.netty.buffer.ByteBuf;

public class UpdateGameIncomingPacket implements TcpIncomingPacket
{
	@Override
	public void handle(TcpPacketDataHandler handler, ByteBuf buf)
	{
		handler.sendPacket(new UpdateGameOKOutgoingPacket());
	}
}
