package badugi;


import java.io.*;
import java.net.*;

/*
 * Segregate Server and ClientWorker work
 * Game class makes general changes, ClientWorker only players needs
 * Make smth like in Wolf-Hare game, to allow players make decisions step-by-step
 * 
 */
public class Server 
{

	private int port;
	private int money;
	private int playersNumber;
	
	private int playersCounter = 0;
	//private boolean init;
	private ServerSocket socket;
	private Socket players[];  
	private Game game;
	private PrintWriter out[];
	
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
			//System.exit(-1);
		}
		
		//init = true;
		
		players = new Socket[playersNumber]; //1 more, because server makes worker[0]
		out = new PrintWriter[playersNumber];

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
					
					
					//PrintWriter out = null;
					try
					{
						out[playersCounter] = new PrintWriter(players[playersCounter].getOutputStream(), true);
						
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("IO exception");
					}
					out[playersCounter].println("Connected!");
					
					
					
					System.out.println(playersCounter);
					
					//Thread t = new Thread(players[playersCounter]);
					playersCounter++;
					//t.start();
					 
				} 
				catch (IOException e) 
				{
					System.out.println("Acception failed");
					//System.exit(-1);
				}
				
				if(playersCounter  == playersNumber )
				{
					game = new Game(players, out, money);
					if(game != null)
						System.out.println("Game starts, Server");
					
					
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
		
		try
		{
			playersNumber = Integer.parseInt(args[0]);
			money = Integer.parseInt(args[1]);
			serverPort = Integer.parseInt(args[2]);
			
		}
		catch(NumberFormatException e)
		{
			System.out.println("");	
		}
		
		Server serv = new Server(playersNumber, money, serverPort);
		serv.listenSocket();

	}

}