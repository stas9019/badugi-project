package badugi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Game
{
	private int round;
	private int bank, money;
	private ArrayList<Socket> players;
	private ArrayList<Card> suit = new ArrayList<Card>();
	public Random random = new Random();
	
	/*
	 * Initializing card suite
	 */
	Game(ArrayList<Socket> players, int money)
	{
		
		this.players = players;
		this.money = money;
		
		initializeSuit();
		
		sayForAllPlayers("Game starts!", false);
		
		sayForAllPlayers("First card distribution", true);
		sayForAllPlayers("Start cash "+money, false);
		
		
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
		Card randomCard = suit.get(random.nextInt(52));

		System.out.println("figure "+String.valueOf(randomCard.figure));
		System.out.println("color "+String.valueOf(randomCard.colour));
		
		
		
		return String.valueOf(randomCard.getCardColor())+ " " + String.valueOf(randomCard.getCardFigure());
	}
	
	void nullBank()
	{
		bank = 0;
	}
	
	void nextRound()
	{
		round++;
	}
	
	void initializeSuit()
	{
		
		
		for(int i=1; i<=4; i++)
		{
			for(int j=1; j<=13; j++)
			{
				suit.add(new Card(i,j));
			}
		}
	}
	
	void sayForAllPlayers(String phrase, boolean waitForAnswer)
	{
		for(int i=0; i < players.size(); i++)
		{
			PrintWriter out = null;
			try
			{
				out = new PrintWriter(players.get(i).getOutputStream(), true);
			} 
			catch (IOException e)
			{
				System.out.println("I/O Exception");
			}
						
			out.println(phrase);	
			
			if(waitForAnswer)
			{
				listenForPlayer(players.get(i));
			}
		}
	}
	
	/*void firstCardDistribution()
	{
		for(int i=0; i < players.size(); i++)
		{
			PrintWriter out = null;
			try
			{
				out = new PrintWriter(players.get(i).getOutputStream(), true);
			} 
			catch (IOException e)
			{
				System.out.println("I/O Exception");
			}
						
			out.println("First card distribution");	
			listenForPlayer(players.get(i));
		}
	}*/
	
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
				System.out.println("Waiting for player answer...");
				text = in.readLine();
				System.out.println("Player answer: '" + text +"'");
			} 
	        	
			catch (IOException e)
			{
				System.out.println("No I/O while listening"); 
			}
			
			if(text.equals("Take cards"))
			{
				startCardsDistribution(out);
				//out.println("");
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
			System.out.println("Card description: " +cardDescription); 
			out.println("New card "+cardDescription);
		}
	}
}
