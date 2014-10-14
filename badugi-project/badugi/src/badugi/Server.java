import java.io.*;
import java.net.*;

public class Server {

	private int bank;
	private Card suit[52];
	int round;
	
	
	/*
	 * args[0] - Liczba uczęstników
	 * args[1] - Ilość żetonów, które dostaje każdy z graczy
	 * */
	public static void main(String[] args) {
		
		int playersNumber = 0;
		int money = 0;
		
		try
		{
			playersNumber = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e)
		{
			System.out.
		}
		Server serv = new Service();

	}

}
