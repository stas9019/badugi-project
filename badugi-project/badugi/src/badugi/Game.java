package badugi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Game
{
	
	private int bank;
	private int smallBlind;
	private int bigBlind;
	private int currentPot;
	private int dealerPosition;
	private int playerPot;
	private int allInWinnerPot;
	
	private boolean winnerIsAllIn;			//revision 33
	//private boolean endOfCheck;		//revision 37
	
	private String winnerCombination;
	
	private int playersColors[];
	private int playerFigure[];
	
	private ArrayList<Socket> players;
	private ArrayList<Socket> leftPlayers;
	
	private ArrayList<Card> suit = new ArrayList<Card>();
	private ArrayList<Card> secondSuit = new ArrayList<Card>();
	
	private ArrayList<ArrayList<Card>> playersHands = new ArrayList<ArrayList<Card>>();

	public Random random = new Random();
	
	
	protected Game(ArrayList<Socket> players, int money, int smallBlind)
	{
		
		this.players = players;
		this.smallBlind = smallBlind;
		bigBlind=2*smallBlind;
		
		leftPlayers = new ArrayList<Socket>(players);	

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
		}		
		
	}

	private void informConcreteLeftPlayer(String phrase, int playerNum, boolean waitForAnswer )			//revision 33
	{
		PrintWriter out = null;
		try
		{
				out = new PrintWriter(leftPlayers.get(playerNum).getOutputStream(), true);
			
			System.out.println("Informing concrete Left Player "+playerNum +": "+ phrase);			
			out.println(phrase);	
			
			if(waitForAnswer)
			{
				listenForPlayer(leftPlayers.get(playerNum));
			}
		} 
		catch (IOException e)
		{
			System.out.println("I/O Exception");
		}
		catch (NullPointerException e)
		{
			playerDisconected(playerNum);				// revision 30
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
			
			if(text.equals("I lost"))
			{
				players.remove(player);
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
	        	currentPot = newPot;
	        	//return;
	        }
			
			if(text.startsWith("Difference Raise "))
	        {
	        	int diff = Integer.parseInt(text.substring(17));
	        	bank+=diff;

	        	return;
	        }
			
			//making double analyzing for all in
			if(text.startsWith("All-in change bank "))
	        {
	        	int sum = Integer.parseInt(text.substring(19));
	        	bank+=sum;
	        	//listenForPlayer(player);//replace maybe	//revision 33
	        	//return;
	        }
			if(text.startsWith("I'm All-in "))
	        {
	        	int pot = Integer.parseInt(text.substring(11));
	        	
	        	if(pot > currentPot)
	        		currentPot = pot;

	        	return;
	        }
			if(text.startsWith("My colors "))		//revision 32
	        {	
	        	int colorsNum = Integer.parseInt(text.substring(10));	        	
	        	playersColors[leftPlayers.indexOf(player)] = colorsNum;
	        	
	        	return;
	        }
			
			if(text.startsWith("My next highest figure "))		//revision 32
	        {	
	        	int figure = Integer.parseInt(text.substring(23));	        	
	        	playerFigure[leftPlayers.indexOf(player)] = figure;
	        	
	        	return;
	        }
			
			if(text.startsWith("My card "))		//revision 37
	        {	
	        	int color = Integer.parseInt(String.valueOf(text.charAt(8)));
	        	int figure = Integer.parseInt(text.substring(10));

	        	if(checkPlayerCard(color, figure))
	        	{
	        		playersHands.get(playersHands.size()-1).add(new Card(color, figure));
	        	}
	        	else
	        	{
	        		informConcretePlayer("You're cheater!", players.indexOf(player), false);
	        		leftPlayers.remove(player);
	        		players.remove(player);
	        		playersHands.remove(playersHands.size()-1);
	        	}
	        }
			
			if(text.equals("No more cards left"))		//revision 37
	        {	      	
	        	return;
	        }
			
			if(text.startsWith("Yes, I'm All-in "))		//revision 32
	        {	
				winnerIsAllIn = true;
				allInWinnerPot = Integer.parseInt(text.substring(16));
	        	return;
	        }
			if(text.equals("I'm not All-in"))		//revision 32
	        {		        	
	        	return;
	        }
			
			if(text.startsWith("Real pot "))		//revision 32
	        {	
				playerPot = Integer.parseInt(text.substring(9));
	        	return;
	        }
			
			if(text.startsWith("Winner combination "))		//revision 32
	        {	
				winnerCombination = text.substring(19);
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
	
	
	private void playerDisconected(int playerNum)			
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
			winnerCombination = "";
			playersColors = null;
			playerFigure = null;
			
			//endOfCheck = false;		//revision 37
			winnerIsAllIn = false;			//revision 33
			secondSuit.clear();				//revision 32
			leftPlayers.clear();
			playersHands.clear();
			
			
			
			leftPlayers = new ArrayList<Socket>(players);//makes just reference, not copy, but makes the same size to make a copy in next step
			Collections.copy(leftPlayers, players);	//revision 33
			
			
			initializeSuit();					
			//informAllPlayers("New game", true);
			
			for(int i = players.size()-1; i>=0; i--)
				informConcretePlayer("New game",i, true);			//revision 34 cause some players can leave game in this moment
			
			if(players.size() < 2)
			{
				informConcretePlayer("You are winner!", 0, false);
				break;
			}
			
			
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
				
				if(i!=4)											//no card changing after four round
					informAllLeftPlayers("Change cards", true);
				
			}
			
			/*final stage*/
			finalStage();	// find a winner and give him bank
			nullBank();
			nullCurrentPot();
			changeDealer();
		}
	}
	
	private void startAuctionRound()
	{
		boolean equality=false;
		
		while(!equality)
		{
			for(int i = 1; i <= leftPlayers.size(); i++)
			{
				informConcreteLeftPlayer("Auction "+currentPot, (dealerPosition+2 + i)%leftPlayers.size(), true);
			}
			equality = checkPotsOfAllLeftPlayers();
		}
	}
	
	private void chooseDealer()
	{
		dealerPosition = new Random().nextInt(players.size());
	}
	
	private void changeDealer()
	{
		informConcretePlayer("You are not dealer anymore", dealerPosition, false);
		dealerPosition = (dealerPosition+1)%players.size();							//revision 33 
	}
	
	private void nullBank()		//just for easier understanding
	{
		bank = 0;
	}
	
	private void informWinner()	//revision 33
	{
			
			informConcreteLeftPlayer("Are you All-In?", 0, true);
			if(winnerIsAllIn)										//should be tested
			{
				for(int i = 0; i < players.size(); i++)
				{
					informConcretePlayer("Your real pot?", i, true);
					if(allInWinnerPot < playerPot)
					{
						informConcretePlayer("Take back "+(playerPot - allInWinnerPot), i, false);
					
						bank-=(playerPot - allInWinnerPot);
					}
				}
			}
			informConcreteLeftPlayer("Your won! "+bank, 0, false);
			
			showWinnersCardsForAllPlayers();

	}
	
	private void informPlayersAboutDraw()
	{									// if more than one player remained	
			informAllLeftPlayers("Draw, casino took your money :P", false);
			nullBank();
	}
		
	private void nullCurrentPot()
	{
		currentPot = 0;
	}
	
	private void finalStage()		// revision 37
	{
		
		for(int i = leftPlayers.size()-1; i>=0; i--)
		{
			playersHands.add(new ArrayList<Card>());
			informConcreteLeftPlayer("Show your cards", i, true);			
		}
		
		playersColors = new int[leftPlayers.size()];
		
		for(int i = leftPlayers.size()-1; i>=0; i--)
		{
			playersColors[i] = answerHowMuchColors(playersHands.get(i));
		}

		int max = 0;
		for(int i = 0; i < leftPlayers.size(); i++)		//find max amount of different colors on players hands 
		{
			if(playersColors[i] > max)
				max = playersColors[i];
		}
		for(int i = leftPlayers.size()-1; i>=0; i--)	//leave players with max unique colors
		{
			if(playersColors[i] < max)
			{
				leftPlayers.remove(i);
				playersHands.remove(i);
			}
				
		}
		
		if(leftPlayers.size() < 2)				// if just one player remain
		{
			informWinner();
			return;
		}
		//else, checking players hands
		checkLeftPlayersCards();				//players cards comparing
		
		if(leftPlayers.size() < 2)				// if just one player remained
		{
			informWinner();
		}
		else									// if more than one player remained
		{
			informPlayersAboutDraw();
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
	
	private void startCardsDistribution(PrintWriter out, int amount)
	{
		if(suit.size() < amount)
		{//revision 30
			suit = new ArrayList<Card>(secondSuit);
		}
		
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

	private void checkLeftPlayersCards()	//revision 32
	{
		playerFigure = new int[leftPlayers.size()]; 
		int lim = playersHands.get(0).size();
		
		for(int i=lim-1; i>=0; i--)	// i - number of cards of each player in final(must be the same)
		{							//starting from last card
			for(int j=0; j<leftPlayers.size(); j++)// j - number of player
			{
				playerFigure[j] = playersHands.get(j).get(i).getCardFigure();
			}
			
			int min = 15;
			for(int j = 0; j < leftPlayers.size(); j++)		//find lowest figure
			{
				if(playerFigure[j] < min)
					min = playerFigure[j];
			}

			for(int j = leftPlayers.size()-1; j>=0; j--)	//staying players with lowest figure
			{
				if(playerFigure[j] > min)
				{
					leftPlayers.remove(j);
					playersHands.remove(j);
				}
			}
			
			if(leftPlayers.size() < 2)				// if just one player remain
				return;
			
		}
	}

	private void showWinnersCardsForAllPlayers()			//revision 33
	{
		informConcreteLeftPlayer("Show your victorious combination", 0, true);
		
		for(int i = 0; i < players.size(); i++)
		{
			informAllPlayers("Winner combination " + winnerCombination, false);
		}
	}
	
	/*Block was taken from Client class*/		// revision 37
	
	protected void sortCardsByColor(ArrayList<Card> playerHand)										
	{
		System.out.println("Order cards by color");
		
		Collections.sort(playerHand, new Comparator<Card>() {								
            @Override
            public int compare(Card card1, Card card2) {
                return String.valueOf(card1.getCardColor()).compareTo(String.valueOf(card2.getCardColor()));		//cause cannot compare by primitive type integer
            }
        });
	}
	
	protected void sortCardsByFigure(ArrayList<Card> playerHand)										// revision 32
	{
		
		 System.out.println("Order cards by figure");
		 
		 Collections.sort(playerHand, new Comparator<Card>() 
				 {
		            @Override
		            public int compare(Card o1, Card o2) 
		            {
		                return o1.getCardFigureLexic().compareTo(o2.getCardFigureLexic());		//cause cannot compare by primitive type integer
		            }
				 });
	}
	
	protected int countMatchingColorsByColor(int color, ArrayList<Card> playerHand)	
	{
		int unUniqueColors = 0;
		for(int i=0; i<3; i++)
		{
			if(playerHand.get(i).getCardColor() == color)
				unUniqueColors++;				
		}
		return unUniqueColors;
	}
	
	protected void removeMarkedCards(ArrayList<Card> playerHand)
	{
		for(int i=playerHand.size()-1; i>=0; i--)			// delete marked  
		{
			if(playerHand.get(i).isMarked())
			{
				playerHand.remove(i);
			    //gameFrame.setCardView(i, "", null);
			}
		}
	}
	
	protected int answerHowMuchColors(ArrayList<Card> playerHand)	
	{
		sortCardsByFigure(playerHand);
		
		for(int i=0; i<3; i++)	//find matching figures, mark less useful, after delete marked
		{
			if(playerHand.get(i).getCardFigure() == playerHand.get(i+1).getCardFigure())
			{
				if( countMatchingColorsByColor(playerHand.get(i).getCardColor(), playerHand) >= countMatchingColorsByColor(playerHand.get(i+1).getCardColor(), playerHand ))	
				{
					playerHand.get(i).markCard();
				}
				else
				{
					playerHand.get(i+1).markCard();
				}
			}
		}
		//System.out.println("Size before first removing "+playerHand.size());
		removeMarkedCards(playerHand);		// delete marked  
		//System.out.println("Size after first removing "+playerHand.size());
		
		sortCardsByFigure(playerHand);
		sortCardsByColor(playerHand);
		
		int uniqueColors = 1;// was 0
		
		for(int i=0; i<playerHand.size()-1; i++)	
		{
			if(playerHand.get(i).getCardColor() != playerHand.get(i+1).getCardColor())
				uniqueColors++;				
		}
		System.out.println("Unique colors "+uniqueColors);
	
		/*now, if some cards have same color, leave lowest card, remove others*/
		
		
			/*just pair of the same colors */
				/*3 colors - 4 cards*/
				/*2 colors - 3 cards*/
				/*1 color - 2 cards*/
		if(uniqueColors == playerHand.size()-1)		
		{	
			for(int i=0; i<playerHand.size()-1; i++)
			{
				if(playerHand.get(i).getCardColor() == playerHand.get(i+1).getCardColor())
				{
						playerHand.get(i+1).markCard();//we know, that highest card is next
				}
					
			}
		}
		
		//removeMarkedCards(); ????
		
		/*triple of the same colors*/						
			/*2 colors - 4cards, also can be two pairs*/
			/*1 color - 3cards*/
		if(uniqueColors == playerHand.size()-2)		
		{	
			/*is it case of two pairs?*/
			if(playerHand.size()>3)		// two if's to make code more readable was!=
			{
					if(playerHand.get(1).getCardColor() != playerHand.get(2).getCardColor())//only in this case cards colors are not the same
					{
						playerHand.remove(3);
						playerHand.remove(1);
					}
			}
				if(playerHand.size() >= 3) // it means, that two cards were removed before//corrected  was <
				{
					for(int i=0; i<=1; i++)
					{
						if(playerHand.get(i).getCardColor() == playerHand.get(i+1).getCardColor())
						{
								playerHand.get(i+1).markCard();
								if(playerHand.get(i+1).getCardColor() == playerHand.get(i+2).getCardColor())
								{
									playerHand.get(i+2).markCard();
									break;
								}
						}
							
					}
				}
			
		}
		
		//removeMarkedCards();	// if two cards were removed, no marked cards
		
		/*four cards of the same colors*/										
		if(uniqueColors == playerHand.size()-3)		//is some card have same color, leave lowest 			revision 32
		{		
			playerHand.remove(3);
			playerHand.remove(2);
			playerHand.remove(1);
		}
		
		removeMarkedCards(playerHand);	// delete marked again
		
		
		
		sortCardsByFigure(playerHand);
		/*refresh all cards view
		 * cards will be shown from lowest card to highest
		 */
		
		return uniqueColors;
		
	}
	
	/*-------------------------------------------------------------------------*/
	
	protected boolean checkPlayerCard(int color, int figure)		// revision 37
	{
		for(int i = 0; i<suit.size(); i++)
		{
			if(suit.get(i).getCardFigure() == figure)
			{
				if(suit.get(i).getCardColor() == color)
				{
					return false;
				}
			}
		}
		
		for(int i = 0; i<secondSuit.size(); i++)
		{
			if(secondSuit.get(i).getCardFigure() == figure)
			{
				if(secondSuit.get(i).getCardColor() == color)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
}
