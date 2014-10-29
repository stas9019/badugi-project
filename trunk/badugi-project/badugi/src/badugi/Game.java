package badugi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
		firstCardDistribution();
		
		/*for(int i=0; i < players.length; i++)
		{
			try
			{
				listenForPlayer(players[i]);
			}
			catch(NullPointerException e)
			{}
		}*/
	}
	
	String takeNewCard()
	{
		Card randomCard = suit[random.nextInt(52)];
		String cardDescription = new String(String.valueOf(randomCard.getCardColor())+ " " + randomCard.getCardFigure());
		System.out.println(cardDescription);
		
		return cardDescription;
	}
	
	void nullBank()
	{
		bank = 0;
	}
	
	void nextRound()
	{
		round++;
	}
	
	void firstCardDistribution()
	{
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
						
			out.println("First card distribution");	
			listenForPlayer(players[i]);
		}
	}
	
	void listenForPlayer(Socket player)
	{
		BufferedReader in = null;
		PrintWriter out = null;
		try
		{
			in = new BufferedReader(new InputStreamReader(player.getInputStream()));
			out = new PrintWriter(player.getOutputStream(), true);
		} 
		catch (IOException e)
		{
			System.out.println("No I/O"); 
		}
		
		String text = "";
	        
		while( text != null )
		{	
			try
			{
				System.out.println("[eq");
				text = in.readLine();					
			} 
	        	
			catch (IOException e)
			{
				System.out.println("No I/O while listening"); 
			}
			
			/*if(text.equals("ready"))
			{
				System.out.println("Player ready"); 
			}*/
			if(text.equals("Take cards"))
			{
				startCardsDistribution(out);
				out.close();
				return;
			}
			
			
		}
	}
	/*Maybe can be with parameter int for changing cards also*/
	void startCardsDistribution(PrintWriter out)
	{
		for(int i=0; i < 4; i++)
		{
			String cardDescription = takeNewCard();
			out.println("New card "+cardDescription);
		}
	}
}
