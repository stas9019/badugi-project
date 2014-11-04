package Client;

import java.awt.Color;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


//import badugi.Card;	revision 32

/*
 * This is client's Logic Layer
 * 
 * TO DO 
 * Client's GUI: First Frame - Connection Frame, Second - Game Frame
 * Establishing connection by IP and port in Connection Frame
 * Make ClientWorker class, which analyze players commands 
 */

public class Bot extends Client{

	private Socket socket = null;
	private ClientWorker clientWorker;
	private PrintWriter out = null;
	
	
	
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	
	
	private int playerMoney = 0;
	private int cardCounter = 0; 			
	private int smallBlind 	= 0;
	private int bigBlind 	= 0;
	private int currentPot 	= 0;
	private int playerPot 	= 0;
	
	//private boolean isDealer = false;
	private boolean isAllIn = false;	
	private boolean isFold = false;		//revision 30
	protected boolean isWinner = false;
	
	private ConnectionFrame connectionFrame;
	private GameFrame2 		gameFrame;
	
	public Random random = new Random();
	
	Bot()
	{		
		//super();
		connectionFrame = new ConnectionFrame(this); 
	}
	
	
	/*_________________________________________________
	 *             Connection with server part
	 *_________________________________________________*/
	
	
	protected void connectionAttempt(String adress, String port)
	{
		try
		{
			socket = new Socket(adress, Integer.parseInt(port));
			
			try
			{
				out = new PrintWriter(socket.getOutputStream(), true);
			} 
			catch (IOException e)
			{
				connectionFrame.setOutputText("No I/O");
			}
			
			clientWorker = new ClientWorker(this, socket);
			
			
			Thread t = new Thread(clientWorker);
			t.start();
			
	    }
		catch (IOException e)
		{
		}
	    catch(NullPointerException e)
		{
	    	connectionFrame.setOutputText("Connection failed");
	    	return;
	    }	
		catch  (NumberFormatException e) 
		{
			connectionFrame.setOutputText("Wrong port: "+ port); //System.exit(1);
			return;
	    }
		//frame.setOutputText("No answer from server");		
	}

	protected void sendQueryToServer(String query)
	{
		out.println(query);
	}
	
	
	/*_________________________________________________
	 *             Working with cards part
	 *_________________________________________________*/

	
	protected void takeNewCard(String color, String figure)
	{
		Card newCard = new Card(Integer.parseInt(color),Integer.parseInt(figure));
		playerHand.add(newCard);
		//prepareCardView(newCard);				//revision 30
		//cardCounter++; //revision 32
		
		if(playerHand.size() == 4)	//revision 32
		{
			//sort cards
			sortCardsByFigure();		
			sortCardsByColor();
			for(int i=0; i<4; i++)
				prepareCardView(playerHand.get(i), i);
		}
	}
	
	private void prepareCardView(Card card, int index)
	{
		String view="";
		Color color = null;
		switch(card.getCardColor())
		{/*♥♦♣♠ ♡♢♧♤*/
			case 1:
				view="♥";color=Color.RED;break;
			case 2:
				view="♦";color=Color.RED;break;
			case 3:
				view="♣";color=Color.BLACK;break;
			case 4:
				view="♠";color=Color.BLACK;break;
		}
		
		view+=" ";
		
		switch(card.getCardFigure())
		{/*♥♦♣♠ ♡♢♧♤*/
			case 1:
				view+="A";break;
			case 2:
				view+="2";break;
			case 3:
				view+="3";break;
			case 4:
				view+="4";break;
			case 5:
				view+="5";break;
			case 6:
				view+="6";break;
			case 7:
				view+="7";break;
			case 8:
				view+="8";break;
			case 9:
				view+="9";break;
			case 10:
				view+="10";break;
			case 11:
				view+="J";break;
			case 12:
				view+="Q";break;
			case 13:
				view+="K";break;	
		}
		
		gameFrame.setCardView(index, view, color);
	}
	
	protected void cardChangingStage() 
	{	
		if(isFold)							//revision 30
		{	
			sendQueryToServer("I'm Fold");	//revision 30
			return;						
		}
		
		System.out.println("Player can change cards");//all system.out to status bar
		sendQueryToServer("End of changing");

	}
	
	protected void changeCards(int index)
	{
		sendQueryToServer("Take card back "+playerHand.get(index).getCardColor() +" " + playerHand.get(index).getCardFigure());
		
		playerHand.remove(index);
		//cardCounter--;		//revision 32
		
		sendQueryToServer("Issue one card");
	}
	
	protected void sortCardsByColor()										// revision 32
	{
		System.out.println("Order cards by color");
		
		Collections.sort(playerHand, new Comparator<Card>() {								
            @Override
            public int compare(Card o1, Card o2) {
                return String.valueOf(o1.getCardColor()).compareTo(String.valueOf(o2.getCardColor()));		//cause cannot compare by primitive type integer
            }
        });
	}
	
	protected void sortCardsByFigure()										// revision 32
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
	
	protected void clearCardView()
	{
		for(int i = 0; i<4; i++)
			gameFrame.setCardView(i, "", null);
	}

	protected void removeMarkedCards()//revision 32
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
	
	protected void showYourCombination()
	{
		String view="";
		
		for(int i =0; i<playerHand.size(); i++)
		{

			switch(playerHand.get(i).getCardColor())
			{/*♥♦♣♠ ♡♢♧♤*/
				case 1:
					view+="♥";break;
				case 2:
					view+="♦";break;
				case 3:
					view+="♣";break;
				case 4:
					view+="♠";break;
			}
			
			
			
			switch(playerHand.get(i).getCardFigure())
			{/*♥♦♣♠ ♡♢♧♤*/
				case 1:
					view+="A";break;
				case 2:
					view+="2";break;
				case 3:
					view+="3";break;
				case 4:
					view+="4";break;
				case 5:
					view+="5";break;
				case 6:
					view+="6";break;
				case 7:
					view+="7";break;
				case 8:
					view+="8";break;
				case 9:
					view+="9";break;
				case 10:
					view+="10";break;
				case 11:
					view+="J";break;
				case 12:
					view+="Q";break;
				case 13:
					view+="K";break;	
					
				
			}
			view+=" ";
		}
		sendQueryToServer("Winner combination "+ view);
	}
	
	/*_________________________________________________
	 *             Game process part
	 *_________________________________________________*/
	
	
	protected void betSmallBlind()
	{
		if(playerMoney > smallBlind)
		{
			playerMoney-=smallBlind;
			playerPot+=smallBlind;
			gameFrame.setYourMoney(playerMoney +"");
			gameFrame.setCurrentPot(bigBlind);
			gameFrame.setYourPot(playerPot);
			sendQueryToServer("Small Blind");
		}
		else
			allIn();
	}
	
	protected void betBigBlind()
	{
		if(playerMoney > bigBlind)
		{
			playerMoney-=bigBlind;
			playerPot+=bigBlind;
			gameFrame.setYourMoney(playerMoney +""); //always show changes
			gameFrame.setYourPot(playerPot);
			gameFrame.setCurrentPot(bigBlind);
			sendQueryToServer("Big Blind");
		}
		else
			allIn();
	}
	
	protected void auction(int pot)
	{
	
		currentPot = pot;
		gameFrame.setCurrentPot(pot);
		
		blockGameFrame(true);
		
		if(isAllIn)
		{
			sendQueryToServer("Check");	
			return;							//revision 30
		}
		if(isFold)							//revision 30
		{	
			sendQueryToServer("I'm Fold");
			return;						
		}
		
		if(playerMoney < currentPot)
		{
			switch(random.nextInt(2))
			{
				case 0: fold();break;
				case 1: allIn();break;
			}
			return;
			/*gameFrame.blockCallButton(true);
			gameFrame.blockRaiseButton(true);
			gameFrame.blockCheckButton(true);
			gameFrame.blockYourBidField(true);
			
			gameFrame.blockAllInButton(false);
			gameFrame.blockFoldButton(false);*/
		}
		if(playerMoney == currentPot)
		{
			
			switch(random.nextInt(2))
			{
				case 0: check();break;
				case 1: call();break;
				//case 2: fold();break;	
			}
			
			return;
			/*gameFrame.blockCallButton(true);
			gameFrame.blockRaiseButton(true);
			gameFrame.blockYourBidField(true);
			
			gameFrame.blockFoldButton(false);
			gameFrame.blockCheckButton(false);
			gameFrame.blockCallButton(false);
			gameFrame.blockAllInButton(false);*/
			
		}
		if(playerMoney > currentPot)
		{
			switch(random.nextInt(2))
			{
				case 0: check();break;
				case 1: call();break;
				//case 2: fold();break;	
			}
			
			return;
			//blockGameFrame(false);
		}
		
		if(playerPot == currentPot)
		{
			
			check();
			return;
			/*gameFrame.blockCallButton(true);
			
			gameFrame.blockCheckButton(false);
			gameFrame.blockRaiseButton(false);
			gameFrame.blockYourBidField(false);
			gameFrame.blockAllInButton(false);*/
		}
		
		if((playerPot < currentPot)&&(playerMoney+playerPot < currentPot))
		{
			fold();
			return;
			/*gameFrame.blockCheckButton(true);
			
			gameFrame.blockAllInButton(false);
			gameFrame.blockFoldButton(false);*/
		}
		
		if((playerPot < currentPot)&&(playerMoney+playerPot >= currentPot))
		{
			
			switch(random.nextInt(2))
			{
				case 0: fold();break;
				case 1: call();break;
			}
			return;
			/*gameFrame.blockCheckButton(true);
			
			gameFrame.blockCallButton(false);
			gameFrame.blockAllInButton(false);
			gameFrame.blockFoldButton(false);
			
			if(playerMoney+playerPot > currentPot)
			{
				gameFrame.blockRaiseButton(false);
				gameFrame.blockYourBidField(false);
			}*/
		
		}
		
		if((playerMoney == 0) && (playerPot == currentPot))
		{
			check();
			return;
			//gameFrame.blockCheckButton(false);
		}
		
		if((playerMoney == 0) && (playerPot < currentPot))
		{
				allIn();
				return;
			/*gameFrame.blockCheckButton(true);
			
			gameFrame.blockAllInButton(false);*/
		}
		
		if(playerPot+playerMoney <= currentPot)
		{
			switch(random.nextInt(2))
			{
				case 0: fold();break;
				case 1: allIn();break;
			}
			return;
		}
	}

	protected void check()
	{
		sendQueryToServer("Check");	
		blockGameFrame(true);
	}
	
	protected void call()
	{
		
		if(playerMoney <= currentPot)		//revision 33
		{
			allIn();
			return;
		}
		sendQueryToServer("Call "+(currentPot - playerPot));	//revision 32

		playerMoney-=currentPot;
		playerMoney+=playerPot;		
		playerPot=currentPot;
		
		gameFrame.setYourPot(playerPot);
		gameFrame.setCurrentPot(currentPot);					//revision 30
		gameFrame.setYourMoney(String.valueOf(playerMoney));
		
		blockGameFrame(true);
	}
	
	protected void raise(int bid)
	{
		int diff = bid - playerPot;
		playerMoney+=playerPot;
		playerMoney-=bid;		
		playerPot=bid;
		
		gameFrame.setYourMoney(String.valueOf(playerMoney));
		gameFrame.setYourPot(playerPot);
		gameFrame.setCurrentPot(bid);
		
		sendQueryToServer("Raise "+bid);
		sendQueryToServer("Difference Raise "+diff);
		blockGameFrame(true);
	}
	
	protected void fold()
	{
		sendQueryToServer("Fold");
		
		for(int i=3; i>=0; i--)			// revision 30
		{
			sendQueryToServer("Take card back "+playerHand.get(i).getCardColor() +" " + playerHand.get(i).getCardFigure());		
			playerHand.remove(i);
			//cardCounter--;		//revision 32
			
			gameFrame.setCardView(i, "", null);
		}
		sendQueryToServer("End of returning");// revision 30
		
		isFold = true;
		blockGameFrame(true);
	}
	
	protected void allIn()
	{
		
		playerPot+=playerMoney;
		
		isAllIn = true;
		
		if(playerPot > currentPot)//>=?
			gameFrame.setCurrentPot(playerPot);//?;
		
		sendQueryToServer("All-in change bank "+playerMoney);
		sendQueryToServer("I'm All-in "+playerPot);
		
		playerMoney = 0;
		gameFrame.setYourPot(playerPot);
		gameFrame.setYourMoney(String.valueOf(playerMoney));
		
		blockGameFrame(true);
	}
	
	protected void answerIfAllIn()
	{
		if(isAllIn)
			sendQueryToServer("Yes, I'm All-in " + playerPot);
		else
			sendQueryToServer("I'm not All-in");
	}
	
	protected void setAsDealer(boolean b)
	{
		gameFrame.setAsDealer(b);	
	}
	
	protected void setBlinds(int smallBlind)
	{
		this.smallBlind = smallBlind;
		bigBlind = 2*smallBlind;
		
		/*some function to show blinds on game frame*/
	}
	
	protected void setPlayerMoney(int cash)
	{
		playerMoney = cash;
		gameFrame.setYourMoney(String.valueOf(cash));
	}
	
	protected void checkPot()
	{
		if(!isAllIn && !isFold)//was before(playerMoney != 0)
			sendQueryToServer(String.valueOf(playerPot));
		else
			sendQueryToServer("-1");	
	}
	
	protected void checkRealPot()
	{
			sendQueryToServer("Real pot " + playerPot);
	}
	
	protected void getBank(int bank)
	{
		playerMoney += bank;
		gameFrame.setYourMoney(String.valueOf(playerMoney));			//revision 33
		gameFrame.setYourPot(0);
	}
	
	protected int countMatchingColorsByColor(int color)	//revision 32
	{
		int unUniqueColors = 0;
		for(int i=0; i<3; i++)
		{
			if(playerHand.get(i).getCardColor() == color)
				unUniqueColors++;				
		}
		return unUniqueColors;
	}
	
	protected void answerHowMuchColors()	//revision 32
	{
		sortCardsByFigure();
		
		for(int i=0; i<3; i++)	//find matching figures, mark less useful, after delete marked
		{
			if(playerHand.get(i).getCardFigure() == playerHand.get(i+1).getCardFigure())
			{
				if( countMatchingColorsByColor(playerHand.get(i).getCardColor()) >= countMatchingColorsByColor(playerHand.get(i+1).getCardColor()) ) 	
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
		removeMarkedCards();		// delete marked  
		//System.out.println("Size after first removing "+playerHand.size());
		
		clearCardView();
		sortCardsByFigure();
		sortCardsByColor();
		
		for(int i=0; i<playerHand.size(); i++)
			prepareCardView(playerHand.get(i), i);
		
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
		
		removeMarkedCards();	// delete marked again
		
		
		
		sortCardsByFigure();
		/*refresh all cards view
		 * cards will be shown from lowest card to highest
		 */
		clearCardView();
		
		for(int i=0; i<playerHand.size(); i++)
			prepareCardView(playerHand.get(i), i);
		
		cardCounter = playerHand.size();

		
			sendQueryToServer("My colors "+uniqueColors);	
		
	}

	protected void showNextHighestFigure()//revision 32
	{
		cardCounter--;
		if(cardCounter >= 0)
			sendQueryToServer("My next highest figure "+playerHand.get(cardCounter).getCardFigure());
		else
			sendQueryToServer("No more cards left");
	}
	
	
	protected void newGameStarted()  //maybe other name
	{
		isWinner = false;;
		isAllIn = false;
		isFold = false;
		playerPot = 0;
		cardCounter = 0;		//revision 32
		
		playerHand.clear();		//revision 30
		for(int i=3; i>=0; i--)			// revision 30   
		{
			//playerHand.remove(i);
			gameFrame.setCardView(i, "", null);
		}
		
		gameFrame.setYourPot(0);
		gameFrame.blockCheckBoxes(true);
		
		sendQueryToServer("Issue 4 cards");
	}
	
	protected void newGamePlayersChecking()	//revision 32
	{
		if(playerMoney == 0)		//revision 33
			System.exit(0);			//or invoke Connection Frame
		
		sendQueryToServer("Ready");
		//gameFrame.blockReadyButton(false);
	}
	
	protected void gameOver()		//revision 32 //maybe for victory and loss  also
	{
		for(int i=3; i>=0; i--)			
		{	
			gameFrame.setCardView(i, "", null);
			gameFrame.setCurrentPot(0);
			blockGameFrame(true);
		}
	}
	
	protected void setAsWinner(boolean b)
	{
		isWinner = b;
	}
	
	/*_________________________________________________
	 *             Frames part
	 *_________________________________________________*/
	
	
	protected ConnectionFrame getConnectionFrame()
	{
		return connectionFrame;
	}

	
	protected void invokeGameFrame()
	{
		//connectionFrame.dispose();
		gameFrame = new GameFrame2(this);
		
	}
	
	
	protected void blockGameFrame(boolean b)
	{
		gameFrame.blockCallButton(b);
		gameFrame.blockRaiseButton(b);
		gameFrame.blockCheckButton(b);	
		gameFrame.blockAllInButton(b);
		gameFrame.blockFoldButton(b);
		gameFrame.blockYourBidField(b);
	}
	
	
	public static void main(String[] args) 
	{
		new Bot();
	}


	
	
	
}
