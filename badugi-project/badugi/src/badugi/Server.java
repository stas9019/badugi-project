package badugi;


import java.io.*;
import java.net.*;

/*
 * Segregate Server and ClientWorker work
 * Server makes general changes, ClientWorker only players needs
 * Make smth like in Wolf-Hare game, to allow players make decisions step-by-step
 * 
 */
public class Server {

	private int bank, playersNumber, money, port;
	private Card suit[] = new Card[52];
	int round = 0;
	
	Server(int playersNumber, int money, int port)
	{
		this.playersNumber = playersNumber;
		this.money = money;
		this.port = port;
		/*
		 * initializing card suite
		 */
		for(int i=1; i<=13; i++)
		{
			for(int j=1; j<=4; j++)
			{
				suit[i*j-1].setCardColor(j);
			}
		}
		
	}
	
	public void listenSocket()
	{
		 
	}
	
	void nullBank()
	{
		bank = 0;
	}
	
	/*
	 * args[0] - Liczba uczęstników
	 * args[1] - Ilość żetonów, które dostaje każdy z graczy
	 * args[2] - server port
	 */
	public static void main(String[] args) {
		
		int playersNumber = 0;
		int money = 0;
		int serverPort = 0;
		
		try
		{
			playersNumber = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e)
		{
			System.out.println("");
			
		}
		
		Server serv = new Server(playersNumber, money, serverPort);
		serv.listenSocket();

	}

}
