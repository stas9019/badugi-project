package badugi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game
{
	//private int round; removed //revision 30
	private int bank, smallBlind, bigBlind, currentPot;//money removed //revision 30
	private int dealerPosition;
	private boolean endOfCheckingCards;		//revision 32
	private ArrayList<Socket> players;
	private ArrayList<Socket> leftPlayers;
	private int playerColor[];
	private int playerFigure[];
	private ArrayList<Card> suit = new ArrayList<Card>();
	private ArrayList<Card> secondSuit = new ArrayList<Card>();
	public Random random = new Random();
	
	
	protected Game(ArrayList<Socket> players, int money, int smallBlind)
	{
		
		this.players = players;
		this.smallBlind = smallBlind;
		bigBlind=2*smallBlind;
		
		leftPlayers = new ArrayList<Socket>(players);			// is necessary? revision 30
		
		
		//initializeSuit();					//revision 30
		
		informAllPlayers("Game starts!", false);
		informAllPlayers("Start cash "+money, false);
		informAllPlayers("Small blind "+smallBlind, false);
		chooseDealer();
		
		startGame();
	}

	
	/*_________________________________________________
	 *             Communication with players part
	 *_________________________________________________*/
	
	private void informAllPlayers(String phrase, boolean waitForAnswer)
	{
		System.out.println("Informing All Players: " +phrase);
		for(int i=0; i < players.size(); i++)
		{
			PrintWriter out = null;
			try
			{
				out = new PrintWriter(players.get(i).getOutputStream(), true);
			} 
			catch (IOException e)
			{
				System.out.println("I/O Exception");					//remove player also?	
				
			}
			System.out.println("Informing Player "+i);
			out.println(phrase);	
			
			if(waitForAnswer)
			{
				try
				{
					listenForPlayer(players.get(i));
				}
				catch (NullPointerException e)
				{
					playerDisconected(i);				// revision 30
					
					/*System.out.println("Client " +i +" disconected");
					players.remove(i);
					
					??not working with many players, maybe cause remove not very fast 
					if(players.size() < 2)
					{
						System.out.println("Game over - just one player remain");
						System.exit(0);
					}*/
						
				}
				
			}
		}
	}

	private void informAllLeftPlayers(String phrase, boolean waitForAnswer)
	{
		System.out.println("Informing All left Players: " +phrase);	
		
		for(int i=0; i < leftPlayers.size(); i++)
		{
			PrintWriter out = null;
			try
			{
				out = new PrintWriter(leftPlayers.get(i).getOutputStream(), true);
			} 
			catch (IOException e)
			{
				System.out.println("I/O Exception");
			}
			System.out.println("Informing Player "+i);	
			out.println(phrase);	
			
			if(waitForAnswer)
			{
				try
				{
					System.out.println("Listen for player " +i);
					listenForPlayer(leftPlayers.get(i));
					//return;
				}
				catch (NullPointerException e)
				{
					playerDisconected(i);				// revision 30
					
					/*System.out.println("Client " +i +" disconected");
					leftPlayers.remove(i);
					
					??not working with many players, maybe cause remove not very fast 
					if(leftPlayers.size() < 2)
					{
						System.out.println("Game over - just one player remain");
						System.exit(0);
					}*/
						
				}
				
			}
		}
	}
	
	private void informConcretePlayer(String phrase, int playerNum, boolean waitForAnswer )
	{
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(players.get(playerNum).getOutputStream(), true);
			
			System.out.println("Informing concrete Player "+playerNum +": "+ phrase);			
			out.println(phrase);	
			
			if(waitForAnswer)
			{
				listenForPlayer(players.get(playerNum));
			}
		} 
		catch (IOException e)
		{
			System.out.println("I/O Exception");
		}
		catch (NullPointerException e)
		{
			playerDisconected(playerNum);				// revision 30
			/*
			System.out.println("Player "+playerNum +" disconected");
			players.remove(playerNum);
			leftPlayers.remove(playerNum);
			
			if(players.size() < 2)
			{
				System.out.println("Game over - just one player remain");
				System.exit(-1);		
			}
			*/
		}		
		
	}

	private void listenForPlayer(Socket player)
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
				System.out.println("Waiting for player "+players.indexOf(player)+" answer...");
				text = in.readLine();
				System.out.println("Player "+players.indexOf(player)+" answer: '" + text +"'");
			} 
	        	
			catch (IOException e)
			{
				System.out.println("No I/O while listening"); 
				return;
			}
			
			if(text.equals("Issue 4 cards"))
			{
				startCardsDistribution(out, 4);
				return;
			}
			
			if(text.equals("Ready"))
			{
				return;
			}
			
			if(text.startsWith("Take card back "))
			{			
				String color = String.valueOf(text.charAt(15));
	        	String figure = text.substring(17);	
	        	takeCardBack(color, figure);
	        	/////////////////////////////
			}
			
			if(text.equals("Issue one card"))
			{
				startCardsDistribution(out, 1);
				//return;
			}
			if(text.equals("End of changing"))
			{
				System.out.println("Change end");
				return;
			}
			
			if(text.equals("End of returning"))			//revision 30
			{
				System.out.println("Return end");
				return;
			}
			/*Add to client worker*/
			if(text.equals("Small Blind"))
			{
				bank+=smallBlind;
				return;
			}
			/*Add to client worker*/
			if(text.equals("Big Blind"))
			{
				bank+=bigBlind;
				return;
			}
			
			if(text.equals("Fold"))
			{
				//just for continue listening and more understandability 
				leftPlayers.remove(player);	//revision 32
			}
			
			if(text.equals("I'm Fold"))//revision 30
			{  
				return;
			}
			
			if(text.startsWith("Call "))		//revision 32    difference between current pot and player's pot added
			{
				int diff = Integer.parseInt(text.substring(5));
	        	bank+=diff;

				return;
			}
			
			if(text.equals("Check"))
			{
				return;
			}
			
			if(text.startsWith("Raise "))
	        {
	        	int newPot = Integer.parseInt(text.substring(6));
	        	bank+=currentPot;
	        	bank-=newPot;
	        	currentPot = newPot;
	        	return;
	        }
			
			//making double analyzing for all in
			if(text.startsWith("All-in change bank "))
	        {
	        	int sum = Integer.parseInt(text.substring(19));
	        	bank+=sum;
	        	listenForPlayer(player);//replace maybe
	        	return;
	        }
			if(text.startsWith("All-in "))
	        {
	        	int sum = Integer.parseInt(text.substring(7));
	        	
	        	if(sum > currentPot)
	        		currentPot = sum;

	        	return;
	        }
			if(text.startsWith("My colors "))		//revision 32
	        {	
	        	int colorsNum = Integer.parseInt(text.substring(10));	        	
	        	playerColor[leftPlayers.indexOf(player)] = colorsNum;
	        	
	        	return;
	        }
			
			if(text.startsWith("My next highest figure "))		//revision 32
	        {	
	        	int figure = Integer.parseInt(text.substring(23));	        	
	        	playerFigure[leftPlayers.indexOf(player)] = figure;
	        	
	        	return;
	        }
			
			if(text.equals("No more cards left"))		//revision 32
	        {	
				endOfCheckingCards = true;	        	
	        	return;
	        }
		}
	}

	
	private boolean checkPotsOfAllLeftPlayers()
	{
		String pot="";
		
		for(int i=0; i < leftPlayers.size(); i++)
		{
			PrintWriter out = null;
			try
			{
				out = new PrintWriter(leftPlayers.get(i).getOutputStream(), true);
			} 
			catch (IOException e)
			{
				System.out.println("I/O Exception");
			}
				
			out.println("Check pot");	
			
				try
				{
					pot = checkConcretePlayerPot(leftPlayers.get(i));
					
					if(pot.equals("-1"))					//especially for all in case, also for fold maybe
						pot = String.valueOf(currentPot);
				}
				catch (NullPointerException e)
				{
					System.out.println("Client " +i +" disconected");
					leftPlayers.remove(i);
					
					/*not working with many players, maybe cause remove not very fast */
					if(leftPlayers.size() < 2)
					{
						System.out.println("Game over");
						System.exit(0);
					}
						
				}
				
				System.out.println("Checking pot of player " +i);
				//System.out.println(leftPlayers.size());
				System.out.println("Current pot " +currentPot);
				System.out.println("Player pot " +pot);
				
			if(currentPot != Integer.parseInt(pot))
				return false;
		}
		return true;
	}
	
	private String checkConcretePlayerPot(Socket player)				// maybe join with previous function revision 30
	{
		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new InputStreamReader(player.getInputStream()));
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
				System.out.println("Waiting for player pot...");
				text = in.readLine();
				System.out.println("Player pot: '" + text +"'");
				return text;
			} 
	        	
			catch (IOException e)
			{
				System.out.println("No I/O while listening"); 
				return text;
			}
		}
		return text;
	}
	
	private void playerDisconected(int playerNum)				//revision 30
	{
		System.out.println("Player "+playerNum +" disconected");
		players.remove(playerNum);
		leftPlayers.remove(playerNum);
		
		if(players.size() < 2)
		{
			System.out.println("Game over - just one player remain");
			System.exit(0);		
		}
	}
	
	/*_________________________________________________
	 *             Game process part
	 *_________________________________________________*/
	
	private void startGame()
	{
		
		while(players.size()>1)
		{			
			/*null/false/clear all variables and arrays*/
			playerColor = null;
			playerFigure = null;
			endOfCheckingCards = false;		//revision 32
			secondSuit.clear();				//revision 32
			
			
			if(players.size() < 2)
			{
				informConcretePlayer("You are winner!", 0, false);
				break;
			}
			
			leftPlayers = new ArrayList<Socket>(players);
			
			initializeSuit();						//revision 30
			informAllPlayers("New game", true);
			
			informAllPlayers("First card distribution", true);
			
			int whoBetSmallBlind = (dealerPosition+1)%players.size();
			int whoBetBigBlind =   (dealerPosition+2)%players.size();
			
			informConcretePlayer("Dealer", dealerPosition, false);
			informConcretePlayer("Bet small blind", whoBetSmallBlind, true);//wait for response, if they have enough money, should be true
			informConcretePlayer("Bet big blind", whoBetBigBlind, true);
					
			currentPot = bigBlind;
			
			for(int i=1; i<=4; i++ )
			{
				System.out.println("Round "+i);
				
				startAuctionRound();
				
				//check if more then 1 player left		revision 32
				if(leftPlayers.size() < 2)
				{
					informWinner();
					break;
				}
				
				if(i!=4)											//no card changing after first round
					informAllLeftPlayers("Change cards", true);
				
			}
			
			/*final stage*/
			finalStage();	// find a winner and give him bank
			nullBank();
			nullCurrentPot();//?
			changeDealer();//?
		}
	}
	
	private void startAuctionRound()
	{
		boolean equality=false;
		
		while(!equality)
		{
			for(int i = 1; i <= leftPlayers.size(); i++)
			{
				informConcretePlayer("Auction "+currentPot, (dealerPosition+2 + i)%leftPlayers.size(), true);
			}
			equality = checkPotsOfAllLeftPlayers();
		}
	}
	
	private void chooseDealer()
	{
		dealerPosition = new Random().nextInt(players.size());
	}
	
	/*should be more complicated*/
	private void changeDealer()
	{
		informConcretePlayer("You are not dealer anymore", dealerPosition, false);
		dealerPosition = (++dealerPosition)%players.size();	//test it
	}
	
	private void nullBank()		//just for easier understanding
	{
		bank = 0;
	}
	
	private void informWinner()	//just prototype revision 32   
	{
		if(leftPlayers.size() < 2)								//check if player not All-In
			informConcretePlayer("Your won! "+bank, 0, false);
	}
	
	private void nullCurrentPot()
	{
		currentPot = 0;
	}
	
	private void finalStage()		// revision 32
	{
		
		playerColor = new int[leftPlayers.size()];					//revision 32 null it after every round

		for(int i = 0; i < leftPlayers.size(); i++)
		{
			informConcretePlayer("How many different colors do you have?", i, true);
		}

		int max = 0;
		for(int i = 0; i < leftPlayers.size(); i++)		//find max
		{
			if(playerColor[i] > max)
				max = playerColor[i];
		}
		for(int i = leftPlayers.size()-1; i>=0; i--)	//staying players with max unique colors
		{
			if(playerColor[i] < max)
				leftPlayers.remove(i);
		}
		if(leftPlayers.size() < 2)				// if just one player remain
		{
			informWinner();
			return;
		}
		//else, checking players hands
		checkLeftPlayersCards();
		
		if(leftPlayers.size() < 2)				// if just one player remained
		{
			informWinner();
			return;
		}
		else									// if more than one player remained
		{
			informAllPlayers("Draw, casino took your money :P", false);
			nullBank();
		}
		
	}
	
	private void checkLeftPlayersCards()	//revision 32
	{
		playerFigure = new int[leftPlayers.size()]; 
		
		for(int i=0; i<4; i++)	//change on while maybe, max 4 cards or return will happen earlier
		{	
			for(int j=0; j<leftPlayers.size(); j++)
				informConcretePlayer("Show your next highest figure", j, true);
			
			if(endOfCheckingCards)
				return;
			
			int min = 15;
			for(int j = 0; j < leftPlayers.size(); j++)		//find lowest figure
			{
				if(playerFigure[j] < min)
					min = playerFigure[j];
			}

			for(int j = leftPlayers.size()-1; j>=0; j--)	//staying players with lowest figure
			{
				if(playerFigure[j] > min)
					leftPlayers.remove(j);
			}
			
			if(leftPlayers.size() < 2)				// if just one player remain
				return;
			
		}
	}
	
	/*_________________________________________________
	 *             Working with cards part
	 *_________________________________________________*/

	
	private void initializeSuit()
	{
		suit.clear();
		for(int i=1; i<=4; i++)
		{
			for(int j=1; j<=13; j++)
			{
				suit.add(new Card(i,j));
			}
		}
		
		long seed = System.nanoTime();
		Collections.shuffle(suit, new Random(seed));		//shuffle suit, cause random sometimes behaves bit strange
	}
	
	/*Maybe can be with integer parameter for changing cards also*/
	private void startCardsDistribution(PrintWriter out, int amount)
	{
		if(suit.size() < amount)						//revision 30
			suit = new ArrayList<Card>(secondSuit);
		
		for(int i=0; i < amount; i++)
		{
			String cardDescription = issueNewCard();
			System.out.println("Card description: " +cardDescription); 
			out.println("New card "+cardDescription);
		}
	}
		
	private String issueNewCard()//revision 30
	{
		
		int whichCard = random.nextInt(suit.size());
		Card randomCard = suit.get(whichCard);
		suit.remove(whichCard);
		
		//System.out.println("figure "+String.valueOf(randomCard.figure));
		//System.out.println("color "+String.valueOf(randomCard.colour));

		return String.valueOf(randomCard.getCardColor())+ " " + String.valueOf(randomCard.getCardFigure());
	}
	
	private void takeCardBack(String color, String figure)
	{		
		secondSuit.add(new Card(Integer.parseInt(color),Integer.parseInt(figure)));		
		System.out.println("Card took back " +color + " " +figure);
	}
}
