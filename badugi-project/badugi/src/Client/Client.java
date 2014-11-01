package Client;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


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
	private boolean isAllIn = false;	//for a new game make false again
	
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
		//prepareCardView(newCard);
		cardCounter++; /*maybe function check card counter*/
		
		if(cardCounter == 4)
		{
			for(int i=0; i<4; i++)
				prepareCardView(playerHand.get(i), i);
		}
	}
	
	private void prepareCardView(Card card, int index)
	{
		String view="";
		
		switch(card.getCardColor())
		{/*♥♦♣♠ ♡♢♧♤*/
			case 1:
				view="♥";break;
			case 2:
				view="♦";break;
			case 3:
				view="♣";break;
			case 4:
				view="♠";break;
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
		
		gameFrame.setCardView(index, view);
	}
	
	protected void changeCards(int index)
	{
		//return card
		sendQueryToServer("Take card back "+playerHand.get(index).getCardColor() +" " + playerHand.get(index).getCardFigure());
		
		playerHand.remove(index);
		cardCounter--;
		
		sendQueryToServer("Issue one card");
		//take new card query
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
			sendQueryToServer("Check");	
		
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
		gameFrame.setCurrentPot(bid);
		sendQueryToServer("Raise "+bid);
		blockGameFrame(true);
	}
	
	protected void fold()
	{
		sendQueryToServer("Fold");
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
		if(playerMoney != 0)
			out.println(String.valueOf(playerPot));
		else
			out.println("-1");	
	}
	
	protected void newGameStarted()  //maybe other name
	{
		isAllIn = false;
	}
	
	protected void cardChangingStage() 
	{
		System.out.println("Player should change cards");//all system.out to status bar
		gameFrame.blockChangeButton(false);
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
