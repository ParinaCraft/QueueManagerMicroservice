package fi.joniaromaa.queuemanagermicroservice;

import java.util.Timer;
import java.util.TimerTask;

import fi.joniaromaa.queuemanagermicroservice.game.GameManager;
import fi.joniaromaa.queuemanagermicroservice.net.TcpNetworkManager;
import fi.joniaromaa.queuemanagermicroservice.net.UdpNetworkManager;
import fi.joniaromaa.queuemanagermicroservice.queue.QueueManager;
import lombok.Getter;

public class QueueManagerMicroservice
{
	@Getter private static QueueManager queueManager;
	@Getter private static GameManager gameManager;
	
	@Getter private static TcpNetworkManager tcpNetworkManager;
	@Getter private static UdpNetworkManager udpNetworkManager;
	
	public static void main(String[] args) throws InterruptedException
	{
		QueueManagerMicroservice.queueManager = new QueueManager();
		QueueManagerMicroservice.gameManager = new GameManager();
		
		QueueManagerMicroservice.tcpNetworkManager = new TcpNetworkManager();
		QueueManagerMicroservice.tcpNetworkManager.start();
		
		QueueManagerMicroservice.udpNetworkManager = new UdpNetworkManager();
		QueueManagerMicroservice.udpNetworkManager.start();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				System.out.println("Queue: " + QueueManagerMicroservice.queueManager.getAvaibleGameServersCount());
			}
		}, 1000, 1000);
		
		System.out.println("READY!");
	}
}
