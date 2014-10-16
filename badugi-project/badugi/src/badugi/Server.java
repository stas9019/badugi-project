package badugi;


import java.io.*;
import java.net.*;

import Client.Client;

/*
 * Segregate Server and ClientWorker work
 * Server makes general changes, ClientWorker only players needs
 * Make smth like in Wolf-Hare game, to allow players make decisions step-by-step
 * 
 */
public class Server 
{

	private int port;
	
	
	private ServerSocket socket;
	private ClientWorker workers[];  
	private Game game;
	
	Server(int playersNumber, int money, int port)
	{
		
		this.port = port;
		
		game = new Game(playersNumber, money);
		workers = new ClientWorker[playersNumber]; 	

	}
	
	public void listenSocket()
	{
		try 
		{
	    	socket = new ServerSocket(port); 
	    } 
		catch (IOException e) 
		{
			System.out.println("Could not listen on port " + port); 
			//System.exit(-1);
		}
		while(true)
		{
		    /*Add counter to mark clients*/    
			ClientWorker worker;
		            
		    try
		    {
		    	worker = new ClientWorker(socket.accept(), game);
		    	Thread t = new Thread(worker);
		   	    t.start();
		    } 
		    catch (IOException e) 
		    {
		     	System.out.println("Acception failed");
		        //System.exit(-1);
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
