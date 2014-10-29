package badugi;


import java.io.*;
import java.net.*;

/*
 * Segregate Server and ClientWorker work
 * Game class makes general changes, ClientWorker only players needs
 * Make something like in Wolf-Hare game, to allow players make decisions step-by-step
 * 
 */
/*TO-DO
 * Finalize method
 * */
public class Server 
{

	private int port;
	private int money;
	private int playersNumber;
	
	private int playersCounter = 0;
	private ServerSocket socket;
	private Socket players[];  
	private Game game;
	
	Server(int playersNumber, int money, int port)
	{
		
		this.port = port;
		this.money = money;
		this.playersNumber = playersNumber;
		
		try 
		{
	    	socket = new ServerSocket(port); 
	    } 
		catch (IOException e) 
		{
			System.out.println("Could not listen on port " + port); 
			System.exit(-1);
		}
		
		players = new Socket[playersNumber]; 

	}
	
	public void listenSocket() 
	{
		while(true)
		{
			if(playersCounter < playersNumber )
			{
			
				try
				{
					
					players[playersCounter] = socket.accept();
					
					PrintWriter out = null;
					try
					{
						out = new PrintWriter(players[playersCounter].getOutputStream(), true);
						
					}
					catch (IOException e)
					{
						System.out.println("IO exception");
					}
					
					out.println("Connected!");
					
					playersCounter++;
					System.out.println(playersCounter);
					 
				} 
				catch (IOException e) 
				{
					System.out.println("Acception failed");
				}
				
				if(playersCounter  == playersNumber )
				{
					System.out.println("Game starts, Server");
					game = new Game(players, money);
				}
			}
			else
			{	
				try
				{	
					PrintWriter out;
					out = new PrintWriter(socket.accept().getOutputStream(), true);
					out.println("Sorry, game already started");
				}
				catch(IOException e) 
				{
					System.out.println("Acception failed");
				}
			}
		}
	}
	
	/*
	 * args[0] - Number of players
	 * args[1] - Start sum for each player
	 * args[2] - Server port
	 */
	public static void main(String[] args) 
	{
		
		int playersNumber = 0;
		int money = 0;
		int serverPort = 0;
		
		/*TO-DO
		 * Check parameters
		 * */
		try
		{
			playersNumber = Integer.parseInt(args[0]);
			money = Integer.parseInt(args[1]);
			serverPort = Integer.parseInt(args[2]);
			
		}
		catch(NumberFormatException e)
		{
			System.out.println("Wrong parametrs");	
			return;
		}
		
		Server server = new Server(playersNumber, money, serverPort);
		server.listenSocket();

	}

}
