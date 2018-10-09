package fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.net.TcpPacketDataHandler;
import fi.joniaromaa.queuemanagermicroservice.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class PlayerJoinIncomingPacket implements TcpIncomingPacket
{
	@Override
	public void handle(TcpPacketDataHandler handler, ByteBuf buf)
	{
		handler.getLinkedGameStatus().playerJoined(ByteBufUtils.readUUID(buf));
	}
}
