package fi.joniaromaa.queuemanagermicroservice.game.status;

import java.util.HashSet;
import java.util.Map.Entry;

import fi.joniaromaa.queuemanagermicroservice.QueueManagerMicroservice;
import fi.joniaromaa.queuemanagermicroservice.communication.tcp.outgoing.RequestPlayerSlotsOutgoingPacket;

import java.util.UUID;

import io.netty.channel.Channel;
import lombok.Getter;

public class PregameStatus extends AbstractGameStatus
{
	@Getter private final int maxPlayers;
	
	public PregameStatus(int id, Channel channel, int serverId, int gameType, int maxPlayers, UUID...uuids)
	{
		super(id, channel, serverId, gameType, uuids);
		
		this.maxPlayers = maxPlayers;
	}
	
	public boolean tryJoin(UUID... uuids)
	{
		if (!this.getChannel().isActive())
		{
			QueueManagerMicroservice.getQueueManager().unregisterGameStatus(this);
			return false;
		}
		
		this.removeTimedoutUsers();

		if (this.getSpotsLeft() >= uuids.length)
		{
			this.lock.writeLock().lock();
			
			try
			{
				if (this.getSpotsLeft() >= uuids.length)
				{
					long now = System.nanoTime();
					
					for(UUID uuid : uuids)
					{
						this.players.put(uuid, now);
					}
					
					try
					{
						this.getChannel().writeAndFlush(new RequestPlayerSlotsOutgoingPacket(uuids));
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
					
					return true;
				}
			}
			finally
			{
				this.lock.writeLock().unlock();
			}
		}
		
		return false;
	}
	
	private void removeTimedoutUsers()
	{
		HashSet<UUID> toRemove = new HashSet<>();
		
		this.lock.readLock().lock();
		
		try
		{
			long now = System.nanoTime();
			for(Entry<UUID, Long> entry : this.players.entrySet())
			{
				if (entry.getValue() != null && now - entry.getValue() >= 1000000000L * 5L) //After 5s remove them
				{
					toRemove.add(entry.getKey());
				}
			}
		}
		finally
		{
			this.lock.readLock().unlock();
		}
		
		if (toRemove.size() > 0)
		{
			this.lock.writeLock().lock();
			
			try
			{
				for(UUID uuid : toRemove)
				{
					this.players.remove(uuid);
				}
			}
			finally
			{
				this.lock.writeLock().unlock();
			}
		}
	}
	
	public int getSpotsLeft()
	{
		this.lock.readLock().lock();
		
		try
		{
			return this.maxPlayers - this.players.size();
		}
		finally
		{
			this.lock.readLock().unlock();
		}
	}
}
