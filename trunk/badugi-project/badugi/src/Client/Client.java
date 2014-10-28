package Client;

//import javax.swing.SwingUtilities;

import java.io.*;
import java.net.*;

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
		//gameFrame = new GameFrame(this);
		
		//gameFrame.setVisible(false);
		//SwingUtilities.invokeLater(connectionFrame);
		
	}
	
	void connectionAttempt(String adress, String port)
	{
		try
		{
			clientWorker = new ClientWorker(this, connectionFrame, adress, Integer.parseInt(port));
			//clientWorker.listenSocket(adress, Integer.parseInt(port));
			
			Thread t = new Thread(clientWorker);
			t.start();
			
			System.out.println("ARRRRRRRRRRRRRRR");
            //frame.setOutputText("Connected");
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
	
	void blockConnectionFrame()
	{
		connectionFrame.blockConnectButton();
	}
	
	void invokeGameFrame()
	{
		gameFrame = new GameFrame(this);
	}

	public void sendQueryToServer(String query)
	{
		//out.println(query);
		/*
		 * Old example of communication, need to be changed
		 */
		/*try {
		    out.println(input.getText());	//sending query to server
		    output.setText(in.readLine());	//send answer to output
		}
		catch (IOException e) {
		    System.out.println("Read failed"); System.exit(1);
		}
		catch(NullPointerException e){
		    //output.setText("No connection");
		}*/
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
				//suit[i*j-1].setCardFigure(i);
				//suit[i*j-1].setCardColor(j);
			}
		}
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
