package fi.joniaromaa.queuemanagermicroservice.game.status;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.channel.Channel;
import lombok.Getter;

public abstract class AbstractGameStatus
{
	@Getter private final int id;
	
	@Getter private final Channel channel;
	@Getter private final int serverId;
	@Getter private final int gameType;
	
	protected Map<UUID, Long> players;
	protected ReentrantReadWriteLock lock;
	
	public AbstractGameStatus(int id, Channel channel, int serverId, int gameType, UUID...uuids)
	{
		this.id = id;
		
		this.channel = channel;
		this.serverId = serverId;
		this.gameType = gameType;
		
		this.players = new HashMap<>();
		this.lock = new ReentrantReadWriteLock();
		
		for(UUID uuid : uuids)
		{
			this.players.put(uuid, null);
		}
	}
	
	public void playerJoined(UUID uuid)
	{
		this.lock.writeLock().lock();
		
		try
		{
			this.players.put(uuid, null);
		}
		finally
		{
			this.lock.writeLock().unlock();
		}
	}
	
	public void playerLeft(UUID uuid)
	{
		this.lock.writeLock().lock();
		
		try
		{
			this.players.remove(uuid);
		}
		finally
		{
			this.lock.writeLock().unlock();
		}
	}
	
	public boolean hasPlayer(UUID uniqueId)
	{
		return this.players.containsKey(uniqueId);
	}
}
