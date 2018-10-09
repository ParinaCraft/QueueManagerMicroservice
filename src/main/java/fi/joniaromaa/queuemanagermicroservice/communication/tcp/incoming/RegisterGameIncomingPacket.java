package fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming;

import java.util.UUID;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing.GameRegisteredOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.net.TcpPacketDataHandler;
import fi.joniaromaa.queuemanagermicroservice.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class RegisterGameIncomingPacket implements TcpIncomingPacket
{
	@Override
	public void handle(TcpPacketDataHandler handler, ByteBuf buf)
	{
		boolean ongoing = buf.readBoolean();
		
		if (!ongoing)
		{
			int serverId = buf.readInt();
			int gameType = buf.readInt();
			int maxPlayers = buf.readInt();
			
			int playersCount = buf.readInt();
			UUID[] uuids = new UUID[playersCount];
			
			for(int i = 0; i < playersCount; i++)
			{
				uuids[i] = ByteBufUtils.readUUID(buf);
			}
			
			handler.createPregameStatus(serverId, gameType, maxPlayers, uuids);
		}
		else
		{
			int serverId = buf.readInt();
			int gameType = buf.readInt();
			
			int playersCount = buf.readInt();
			UUID[] uuids = new UUID[playersCount];
			
			for(int i = 0; i < playersCount; i++)
			{
				uuids[i] = ByteBufUtils.readUUID(buf);
			}
			
			handler.createGameStatus(serverId, gameType, uuids);
		}
		
		handler.sendPacket(new GameRegisteredOutgoingPacket());
	}
}
