package badugi;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;

public class Game
{
	private int round;
	private int bank, money, port;
	private ClientWorker players[];
	private Card suit[];
	public Random random = new Random();
	
	/*
	 * initializing card suite
	 */
	Game(ClientWorker players[], int money)
	{
		//this.playersNumber = playersNumber; // we have length of workers-1
		this.players = players;
		this.money = money;
		
		suit = new Card[52];
		
		for(int i=1; i<=13; i++)
		{
			for(int j=1; j<=4; j++)
			{
				suit[i*j-1] = new Card(i,j);
				//suit[i*j-1].setCardFigure(i);
				//suit[i*j-1].setCardColor(j);
			}
		}
		
		for(int i=0; i < players.length; i++)
		{
			System.out.println("player " + i);
			players[i].sayToClient("Game starts!");
			
			
		}
		
		
	}
	
	Card takeNewCard()
	{
		Card randomCard = suit[random.nextInt(52)];
		return randomCard;
	}
	
	void nullBank()
	{
		bank = 0;
	}
	
	void nextRound()
	{
		round++;
	}
	
}
