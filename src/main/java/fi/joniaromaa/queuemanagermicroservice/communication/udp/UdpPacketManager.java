package fi.joniaromaa.queuemanagermicroservice.communication.udp;

import java.util.HashMap;

import fi.joniaromaa.queuemanagermicroservice.communication.udp.incoming.QueuePlayerIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.incoming.QueuePlayersIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.udp.incoming.SpectatePlayerIncomingPackett;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

public class UdpPacketManager
{
	private HashMap<Integer, UdpIncomingPacket> incomingPackets = new HashMap<>();
	
	public UdpPacketManager()
	{
		this.incomingPackets.put(0, new QueuePlayerIncomingPacket());
		this.incomingPackets.put(1, new QueuePlayersIncomingPacket());
		this.incomingPackets.put(2, new SpectatePlayerIncomingPackett());
	}
	
	public void handleIncoming(Channel channel, DatagramPacket msg)
	{
		int packetId = msg.content().readByte();
		
		UdpIncomingPacket incoming = this.incomingPackets.get(packetId);
		if (incoming != null)
		{
			incoming.handle(channel, msg);
		}
		else
		{
			System.out.println("Unhandled packet: " + packetId);
		}
	}
}
