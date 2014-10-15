package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import badugi.Card;

/*
 * TO DO 
 * Client's GUI: First Frame - Connection Frame, Second - Game Frame
 * Establishing connection by IP and port in Connection Frame
 * Make ClientWorker class, which analyze players commands 
 */
public class Client {

	private Card playerSuit[] = new Card[4];
	private int playerMoney;
	private boolean isDealer;
	
	Client()
	{
		ConnectionFrame connectionFrame = new ConnectionFrame(this);
		connectionFrame.setVisible(true);
	}
	
	
	public void setAsDealer()
	{
		isDealer = true;
	}
	
	
	
	public static void main(String[] args) 
	{
		Client client = new Client();
	
	}

	

}
