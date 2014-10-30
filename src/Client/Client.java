package Client;


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
	private ArrayList<Card> suit = new ArrayList<Card>();
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	private int playerMoney;
	private boolean isDealer;
	private ConnectionFrame connectionFrame;
	private GameFrame gameFrame;
	private ClientWorker clientWorker;
	PrintWriter out = null;
	String text;
	int cardCounter = 0;
	Client()
	{
		connectionFrame = new ConnectionFrame(this); 
	}
	
	void connectionAttempt(String adress, String port)
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
				gameFrame.setStatusText("No I/O");
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
	
	
	void invokeGameFrame()
	{
		gameFrame = new GameFrame(this);
	}

	/*TO-DO
	 * Function to communicate with server
	 * Separate class isn't necessary, I think
	 * 	*/
	public void sendQueryToServer(String query)
	{
		out.println(query);
	}
	
	/*initiate suit, after player joins game*/
	public void initializeSuit()
	{
		for(int i=1; i<=4; i++)
		{
			for(int j=1; j<=13; j++)
			{
				suit.add(new Card(i,j));
			}
		}
	}
	
	public void getCards()
	{
			sendQueryToServer("Take cards");
	}
	/*Function to take new cards
	 * Game Frame or just cards views should be refreshed*/
	public void takeNewCard(String color, String figure)
	{
		playerHand.add(new Card(Integer.parseInt(color),Integer.parseInt(figure)));
		
		cardCounter++;
	}
	public void setAsDealer()
	{
		isDealer = true;
	}
	
	public ConnectionFrame getConnectionFrame()
	{
		return connectionFrame;
	}
	
	void setPlayerMoney(int cash)
	{
		playerMoney = cash;
	}
	
	public static void main(String[] args) 
	{
		new Client();
	}
	
}
