package fi.joniaromaa.queuemanagermicroservice.communication.udp.incoming;

import java.util.UUID;

import fi.joniaromaa.queuemanagermicroservice.QueueManagerMicroservice;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing.RequestSpectateOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.UdpIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.outgoing.NoServersFoundOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.outgoing.SendPlayerOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.game.status.GameStatus;
import fi.joniaromaa.queuemanagermicroservice.utils.ByteBufUtils;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

public class SpectatePlayerIncomingPackett implements UdpIncomingPacket
{
	@Override
	public void handle(Channel channel, DatagramPacket msg)
	{
		int queueId = msg.content().readInt();
		UUID whoUniqueId = ByteBufUtils.readUUID(msg.content());
		UUID targetUniqueId = ByteBufUtils.readUUID(msg.content());
		
		GameStatus gameStatus = QueueManagerMicroservice.getGameManager().findPlayerGame(targetUniqueId);
		if (gameStatus != null)
		{	
			gameStatus.getChannel().writeAndFlush(new RequestSpectateOutgoingPacket(whoUniqueId));
			
			channel.writeAndFlush(new DatagramPacket(new SendPlayerOutgoingPacket(queueId, gameStatus.getServerId()).getBytes(), msg.sender()));
		}
		else
		{
			channel.writeAndFlush(new DatagramPacket(new NoServersFoundOutgoingPacket(queueId).getBytes(), msg.sender()));
		}
	}
}
