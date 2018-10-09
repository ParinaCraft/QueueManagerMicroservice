package fi.joniaromaa.queuemanagermicroservice.game;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import fi.joniaromaa.queuemanagermicroservice.game.status.GameStatus;
import io.netty.channel.Channel;

public class GameManager
{
	private ConcurrentMap<Integer, ConcurrentMap<Integer, GameStatus>> gamesByType;
	private ConcurrentMap<Integer, GameStatus> gamesById;
 	
	private AtomicInteger nextId;
	
	public GameManager()
	{
		this.gamesByType = new ConcurrentHashMap<>();
		this.gamesById = new ConcurrentHashMap<>();
		
		this.nextId = new AtomicInteger(1);
	}
	
	public int getNextId()
	{
		return this.nextId.getAndIncrement();
	}
	
	public GameStatus registerGameStatus(Channel channel, int serverId, int gameType, UUID... uuids)
	{
		GameStatus gameStatus = new GameStatus(this.getNextId(), channel, serverId, gameType, uuids);

		this.gamesByType.computeIfAbsent(gameType, (k) -> new ConcurrentHashMap<>()).put(gameStatus.getId(), gameStatus);
		this.gamesById.put(gameStatus.getId(), gameStatus);
		
		return gameStatus;
	}
	
	public void unregisterGameStatus(GameStatus gameStatus)
	{
		ConcurrentMap<Integer, GameStatus> games = this.gamesByType.get(gameStatus.getGameType());
		if (games != null)
		{
			games.remove(gameStatus.getId());
		}
		
		this.gamesById.remove(gameStatus.getId());
	}

	public GameStatus findPlayerGame(UUID uniqueId)
	{
		for(GameStatus gameStatus : this.gamesById.values())
		{
			if (gameStatus.hasPlayer(uniqueId))
			{
				return gameStatus;
			}
		}
		
		return null;
	}
}
