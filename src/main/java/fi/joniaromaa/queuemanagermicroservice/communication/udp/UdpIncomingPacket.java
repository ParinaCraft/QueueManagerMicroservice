package fi.joniaromaa.queuemanagermicroservice.communication.udp;

import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

public interface UdpIncomingPacket
{
	public void handle(Channel channel, DatagramPacket msg);
}
