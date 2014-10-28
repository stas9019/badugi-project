package badugi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Game
{
	private int round;
	private int bank, money, port;
	private Socket players[];
	private Card suit[];
	public Random random = new Random();
	
	/*
	 * Initializing card suite
	 */
	Game(Socket players[], int money)
	{
		
		this.players = players;
		this.money = money;
		
		suit = new Card[52];
		
		for(int i=1; i<=13; i++)
		{
			for(int j=1; j<=4; j++)
			{
				suit[i*j-1] = new Card(i,j);
			}
		}
		
		for(int i=0; i < players.length; i++)
		{
			PrintWriter out = null;
			try
			{
				out = new PrintWriter(players[i].getOutputStream(), true);
			} 
			catch (IOException e)
			{
				System.out.println("I/O Exception");
			}
						
			out.println("Game starts!");		
			
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
