package Client;


import java.io.*;


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

	private Card suit[];
	private Card playerHand[] = new Card[4];
	private int playerMoney;
	private boolean isDealer;
	private ConnectionFrame connectionFrame;
	private GameFrame gameFrame;
	private ClientWorker clientWorker;
	
	String text;
	
	Client()
	{
		connectionFrame = new ConnectionFrame(this); 	
	}
	
	void connectionAttempt(String adress, String port)
	{
		try
		{
			clientWorker = new ClientWorker(this, connectionFrame, adress, Integer.parseInt(port));
			
			Thread t = new Thread(clientWorker);
			t.start();
			
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
		//out.println(query);
	}
	
	/*initiate suit, after player joins game*/
	public void initSuit()
	{
		suit = new Card[52];
		
		for(int i=1; i<=13; i++)
		{
			for(int j=1; j<=4; j++)
			{
				suit[i*j-1] = new Card(i,j);
			}
		}
	}
	
	public void setAsDealer()
	{
		isDealer = true;
	}
	
	public ConnectionFrame getConnectionFrame()
	{
		return connectionFrame;
	}
	
	
	public static void main(String[] args) 
	{
		Client client = new Client();
	}
	
}
