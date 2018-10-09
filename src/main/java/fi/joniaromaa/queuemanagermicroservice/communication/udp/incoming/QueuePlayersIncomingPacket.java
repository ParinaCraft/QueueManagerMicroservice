package fi.joniaromaa.queuemanagermicroservice.communication.udp.incoming;

import java.util.UUID;

import fi.joniaromaa.queuemanagermicroservice.QueueManagerMicroservice;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.UdpIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.outgoing.NoServersFoundOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.outgoing.SendPlayerOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.game.status.PregameStatus;
import fi.joniaromaa.queuemanagermicroservice.utils.ByteBufUtils;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

public class QueuePlayersIncomingPacket implements UdpIncomingPacket
{
	@Override
	public void handle(Channel channel, DatagramPacket msg)
	{
		int queueId = msg.content().readInt();
		int gameType = msg.content().readByte();
		
		UUID[] players = new UUID[msg.content().readByte()];
		for(int i = 0; i < players.length; i++)
		{
			players[i]  = ByteBufUtils.readUUID(msg.content());
		}
		
		PregameStatus gameStatus = QueueManagerMicroservice.getQueueManager().findGameToJoin(gameType, players);
		if (gameStatus != null)
		{
			channel.writeAndFlush(new DatagramPacket(new SendPlayerOutgoingPacket(queueId, gameStatus.getServerId()).getBytes(), msg.sender()));
		}
		else
		{
			channel.writeAndFlush(new DatagramPacket(new NoServersFoundOutgoingPacket(queueId).getBytes(), msg.sender()));
		}
	}
}
