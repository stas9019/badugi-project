package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import badugi.Card;

/*
 * TO DO 
 * Client's GUI
 * Establishing connection by IP and port
 * Make ClientWorker class, which analyze players commands 
 */
public class Client implements ActionListener{

	private Card playerSuit[] = new Card[4];
	private int playerMoney;
	private boolean dealer;
	
	public void setAsDealer()
	{
		dealer = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	

}
