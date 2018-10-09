package fi.joniaromaa.queuemanagermicroservice.communication.tcp;

import java.util.HashMap;

import fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming.PlayerJoinIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming.PlayerQuitIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming.RegisterGameIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.incoming.UpdateGameIncomingPacket;
import fi.joniaromaa.queuemanagermicroservice.net.TcpPacketDataHandler;
import io.netty.buffer.ByteBuf;

public class TcpPacketManager
{
	private HashMap<Integer, TcpIncomingPacket> incomingPackets = new HashMap<>();
	
	public TcpPacketManager()
	{
		this.incomingPackets.put(0, new RegisterGameIncomingPacket());
		this.incomingPackets.put(1, new UpdateGameIncomingPacket());
		this.incomingPackets.put(2, new PlayerJoinIncomingPacket());
		this.incomingPackets.put(3, new PlayerQuitIncomingPacket());
	}
	
	public void handleIncoming(TcpPacketDataHandler handler, ByteBuf msg)
	{
		int packetId = msg.readShort();
		
		TcpIncomingPacket incoming = this.incomingPackets.get(packetId);
		if (incoming != null)
		{
			incoming.handle(handler, msg);
		}
		else
		{
			System.out.println("Unhandled packet: " + packetId);
		}
	}
}
