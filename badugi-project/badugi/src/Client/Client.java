package Client;

import java.awt.Color;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import badugi.Card;

/*
 * This is client's Logic Layer
 * 
 * TO DO 
 * Client's GUI: First Frame - Connection Frame, Second - Game Frame
 * Establishing connection by IP and port in Connection Frame
 * Make ClientWorker class, which analyze players commands 
 */

public class Client{

	private Socket socket = null;
	private ClientWorker clientWorker;
	private PrintWriter out = null;
	
	
	private ArrayList<Card> suit = new ArrayList<Card>();
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
	
	private ConnectionFrame connectionFrame;
	private GameFrame2 		gameFrame;
	
	/*Singleton pattern used*/
	private Client()
	{
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
	
	
	protected void initializeSuit()
	{
		for(int i=1; i<=4; i++)
		{
			for(int j=1; j<=13; j++)
			{
				suit.add(new Card(i,j));
			}
		}
	}
	
	protected void takeNewCard(String color, String figure)
	{
		Card newCard = new Card(Integer.parseInt(color),Integer.parseInt(figure));
		playerHand.add(newCard);
		//prepareCardView(newCard);				//revision 30
		cardCounter++; /*maybe function check card counter*/
		
		if(cardCounter == 4)
		{
			//sort cards
			sortCards();
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
		gameFrame.blockCheckBoxes(false);
		gameFrame.blockChangeButton(false);
	}
	
	protected void changeCards(int index)
	{
		sendQueryToServer("Take card back "+playerHand.get(index).getCardColor() +" " + playerHand.get(index).getCardFigure());
		
		playerHand.remove(index);
		cardCounter--;
		
		sendQueryToServer("Issue one card");
	}
	
	protected void sortCards()										// revision 30
	{
		//Collections.swap(players, 0, dealerPosition);
		
		 /*for (Card card : playerHand) 	//example of making cycle
	     {				
	     	System.out.println(card);
	     }*/
		
		 System.out.println("Order cards by figure");
		 
		 Collections.sort(playerHand, new Comparator<Card>() {
	            @Override
	            public int compare(Card o1, Card o2) {
	                return o1.getCardFigureLexic().compareTo(o2.getCardFigureLexic());		//cause cannot compare by primitive type int
	            }
	        });

	        for (Card card : playerHand) 	//example of making cycle
	        {				
	            System.out.println(card);
	        }
		 
		System.out.println("Order cards by color");
		
		Collections.sort(playerHand, new Comparator<Card>() {								
            @Override
            public int compare(Card o1, Card o2) {
                return String.valueOf(o1.getCardColor()).compareTo(String.valueOf(o2.getCardColor()));		//cause cannot compare by primitive type int
            }
        });
		
		 /*for (Card card : playerHand) 	//example of making cycle
	     {				
			 System.out.println(card);
	     }*/
		 
		
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
			gameFrame.blockCallButton(true);
			gameFrame.blockRaiseButton(true);
			gameFrame.blockCheckButton(true);
			gameFrame.blockYourBidField(true);
			
			gameFrame.blockAllInButton(false);
			gameFrame.blockFoldButton(false);
		}
		if(playerMoney == currentPot)
		{
			gameFrame.blockCallButton(true);
			gameFrame.blockRaiseButton(true);
			gameFrame.blockYourBidField(true);
			
			gameFrame.blockFoldButton(false);
			gameFrame.blockCheckButton(false);
			gameFrame.blockCallButton(false);
			gameFrame.blockAllInButton(false);
			
		}
		if(playerMoney > currentPot)
		{
			blockGameFrame(false);
		}
		
		if(playerPot == currentPot)
		{
			gameFrame.blockCallButton(true);
			
			gameFrame.blockCheckButton(false);
			gameFrame.blockRaiseButton(false);
			gameFrame.blockYourBidField(false);
			gameFrame.blockAllInButton(false);
		}
		
		if((playerPot < currentPot)&&(playerMoney+playerPot < currentPot))
		{
			gameFrame.blockCheckButton(true);
			
			gameFrame.blockAllInButton(false);
			gameFrame.blockFoldButton(false);
		}
		
		if((playerPot < currentPot)&&(playerMoney+playerPot >= currentPot))
		{
			gameFrame.blockCheckButton(true);
			
			gameFrame.blockCallButton(false);
			gameFrame.blockAllInButton(false);
			gameFrame.blockFoldButton(false);
			
			if(playerMoney+playerPot > currentPot)
			{
				gameFrame.blockRaiseButton(false);
				gameFrame.blockYourBidField(false);
			}
		
		}
		
		if((playerMoney == 0) && (playerPot == currentPot))
		{
			gameFrame.blockCheckButton(false);
		}
		
		if((playerMoney == 0) && (playerPot < currentPot))
		{
			gameFrame.blockCheckButton(true);
			
			gameFrame.blockAllInButton(false);
		}
		
	}

	protected void check()
	{
		sendQueryToServer("Check");	
		blockGameFrame(true);
	}
	
	protected void call()
	{
		playerMoney-=currentPot;
		playerMoney+=playerPot;		
		playerPot=currentPot;
		
		gameFrame.setYourPot(playerPot);
		gameFrame.setCurrentPot(currentPot);					//revision 30
		gameFrame.setYourMoney(String.valueOf(playerMoney));
		
		sendQueryToServer("Call");
		blockGameFrame(true);
	}
	
	protected void raise(int bid)
	{
		playerMoney+=playerPot;
		playerMoney-=bid;		
		playerPot=bid;
		
		gameFrame.setYourMoney(String.valueOf(playerMoney));
		gameFrame.setYourPot(playerPot);
		gameFrame.setCurrentPot(bid);
		sendQueryToServer("Raise "+bid);
		blockGameFrame(true);
	}
	
	protected void fold()
	{
		sendQueryToServer("Fold");
		
		for(int i=3; i>=0; i--)			// revision 30
		{
			sendQueryToServer("Take card back "+playerHand.get(i).getCardColor() +" " + playerHand.get(i).getCardFigure());		
			playerHand.remove(i);
			cardCounter--;
			
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
		sendQueryToServer("All-in "+playerPot);
		
		playerMoney = 0;
		gameFrame.setYourPot(playerPot);
		gameFrame.setYourMoney(String.valueOf(playerMoney));
		
		blockGameFrame(true);
	}
	
	protected void setAsDealer(boolean b)
	{
		//isDealer = b;
		//gameFrame.setAsDealer(b);
		
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
			out.println(String.valueOf(playerPot));
		else
			out.println("-1");	
	}
	
	protected void newGameStarted()  //maybe other name
	{
		isAllIn = false;
		isFold = false;
		playerPot = 0;
		cardCounter = 0;
		
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
		new Client();
	}
	
	
}
