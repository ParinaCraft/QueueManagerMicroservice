package fi.joniaromaa.queuemanagermicroservice.game.status;

import java.util.UUID;

import io.netty.channel.Channel;

public class GameStatus extends AbstractGameStatus
{
	public GameStatus(int id, Channel channel, int serverId, int gameType, UUID[] uuids)
	{
		super(id, channel, serverId, gameType, uuids);
	}
}
