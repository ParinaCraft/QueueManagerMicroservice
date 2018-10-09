package fi.joniaromaa.queuemanagermicroservice.queue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import fi.joniaromaa.queuemanagermicroservice.game.status.PregameStatus;
import io.netty.channel.Channel;

public class QueueManager
{
	private ConcurrentMap<Integer, ConcurrentSkipListSet<PregameStatus>> avaibleGameServers;
	
	private AtomicInteger nextId;
	
	public QueueManager()
	{
		this.avaibleGameServers = new ConcurrentHashMap<>();
		
		this.nextId = new AtomicInteger(1);
	}
	
	public int getNextId()
	{
		return this.nextId.getAndIncrement();
	}
	
	public PregameStatus registerGameStatus(Channel channel, int serverId, int gameType, int maxPlayers, UUID... uuids)
	{
		PregameStatus gameStatus = new PregameStatus(this.getNextId(), channel, serverId, gameType, maxPlayers, uuids);
		
		ConcurrentSkipListSet<PregameStatus> gameQueue = this.avaibleGameServers.computeIfAbsent(gameType, (k) ->
		{
			return new ConcurrentSkipListSet<>(new Comparator<PregameStatus>()
			{
				@Override
				public int compare(PregameStatus gameStatus1, PregameStatus gameStatus2)
				{
					int spotsLeft = Integer.compare(gameStatus1.getSpotsLeft(), gameStatus2.getSpotsLeft());
					if (spotsLeft == 0)
					{
						return Integer.compare(gameStatus1.getId(), gameStatus2.getId());
					}
					else
					{
						return spotsLeft;
					}
				}
			});
		});
		
		gameQueue.add(gameStatus);
		return gameStatus;
	}
	
	public void unregisterGameStatus(PregameStatus gameStatus)
	{
		ConcurrentSkipListSet<PregameStatus> gameQueue = this.avaibleGameServers.get(gameStatus.getGameType());
		if (gameQueue != null)
		{
			gameQueue.remove(gameStatus);
		}
	}

	public PregameStatus findGameToJoin(int gameType, UUID... uuids)
	{
		ConcurrentSkipListSet<PregameStatus> gameQueue = this.avaibleGameServers.get(gameType);
		if (gameQueue != null)
		{
			for(PregameStatus gameStatus : gameQueue)
			{
				if (gameStatus.tryJoin(uuids))
				{
					return gameStatus;
				}
			}
		}
		
		return null;
	}
	
	public HashMap<Integer, Integer> getAvaibleGameServersCount()
	{
		HashMap<Integer, Integer> total = new HashMap<>();
		for(Entry<Integer, ConcurrentSkipListSet<PregameStatus>> game : this.avaibleGameServers.entrySet())
		{
			total.put(game.getKey(), game.getValue().size());
		}
		
		return total;
	}
}
