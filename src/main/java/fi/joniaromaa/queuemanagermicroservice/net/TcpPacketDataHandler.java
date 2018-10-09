package fi.joniaromaa.queuemanagermicroservice.net;

import java.io.IOException;
import java.util.UUID;

import fi.joniaromaa.queuemanagermicroservice.QueueManagerMicroservice;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.TcpOutgoingPacket;
import fi.joniaromaa.queuemanagermicroservice.game.status.AbstractGameStatus;
import fi.joniaromaa.queuemanagermicroservice.game.status.GameStatus;
import fi.joniaromaa.queuemanagermicroservice.game.status.PregameStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.timeout.TimeoutException;
import lombok.Getter;

public class TcpPacketDataHandler extends SimpleChannelInboundHandler<ByteBuf>
{
	@Getter private Channel channel;
	
	@Getter private AbstractGameStatus linkedGameStatus;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		this.channel = ctx.channel();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception
	{
		QueueManagerMicroservice.getTcpNetworkManager().getTcpPacketManager().handleIncoming(this, msg);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
		if (this.linkedGameStatus instanceof PregameStatus)
		{
			QueueManagerMicroservice.getQueueManager().unregisterGameStatus((PregameStatus)this.linkedGameStatus);
		}
		else if (this.linkedGameStatus instanceof GameStatus)
		{
			QueueManagerMicroservice.getGameManager().unregisterGameStatus((GameStatus)this.linkedGameStatus);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		try
		{
			if (cause instanceof IOException)
			{
				if (cause.getMessage().startsWith("An existing connection was forcibly closed by the remote host"))
				{
					System.out.println("An existing connection was forcibly closed by the remote host, disconnecting");
					
					return;
				}
				else if (cause.getMessage().startsWith("Connection reset by peer"))
				{
					System.out.println("Connection reset by peer, disconnecting");
					
					return;
				}
			}
			else if (cause instanceof TimeoutException)
			{
				System.out.println("Timeout, disconnecting");
				
				return;
			}
			else if (cause instanceof TooLongFrameException)
			{
				System.out.println("Received too long frame, disconnecting");
				
				return;
			}
			
			cause.printStackTrace();
		}
		finally
		{
			ctx.close();
		}
	}
	
	public void sendPacket(TcpOutgoingPacket packet)
	{
		this.channel.writeAndFlush(packet);
	}
	
	public void createPregameStatus(int serverId, int gameType, int maxPlayers, UUID... uuids)
	{
		if (this.linkedGameStatus != null)
		{
			throw new UnsupportedOperationException("You may not redefine game status, create new session");
		}
		
		this.linkedGameStatus = QueueManagerMicroservice.getQueueManager().registerGameStatus(this.channel, serverId, gameType, maxPlayers, uuids);
	}
	
	public void createGameStatus(int serverId, int gameType, UUID... uuids)
	{
		if (this.linkedGameStatus != null)
		{
			throw new UnsupportedOperationException("You may not redefine game status, create new session");
		}
		
		this.linkedGameStatus = QueueManagerMicroservice.getGameManager().registerGameStatus(this.channel, serverId, gameType, uuids);
	}
}
