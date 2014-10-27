package Client;

import javax.swing.SwingUtilities;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
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
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	String text;
	
	Client()
	{
		connectionFrame = new ConnectionFrame(this); 
		
		SwingUtilities.invokeLater(connectionFrame);
		
	}
	
	void connectionAttempt(String adress, String port) throws InvocationTargetException, InterruptedException
	{
		try
		{
            listenSocket(adress, Integer.parseInt(port));
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
	
	public void listenSocket(String adress, int port)
	{
        try 
        {
        	socket = new Socket(adress, port);
        	out = new PrintWriter(socket.getOutputStream(), true);
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	
        	text = in.readLine();
        	connectionFrame.setOutputText(text);
        }
        catch(NullPointerException e)
        {
        	connectionFrame.setOutputText("No connection");
        }
        catch(NumberFormatException e) 
        {
        	connectionFrame.setOutputText("NumberFormatException"); 
        }
        catch (UnknownHostException e) 
        {
        	connectionFrame.setOutputText("Unknown host: "+ adress); 
        }
        catch  (IOException e) 
        {
        	connectionFrame.setOutputText("No I/O"); 
        }
        catch (IllegalArgumentException e) 
        {
        	connectionFrame.setOutputText("Wrong port: "+ port); 
        }
        
        /*use frame's outputText only here, then in.readline().equals("SomeString")*/
        if(text.equals("Connected!"))
        {
        	connectionFrame.blockConnectButton();
        	//initSuit();
        }
        
        if(text.equals("Game starts!"))
        {
        	
        	connectionFrame.blockConnectButton();
        	connectionFrame.setVisible(false);
        	
        	//sendQueryToServer("");
        	gameFrame = new GameFrame(this);
        	
        	//gameFrame.setVisible(true);
        	SwingUtilities.invokeLater(gameFrame);
        	
        	
        	initSuit();
        	
        
        }
        /*server's answer analyze part
         * maybe some special function for this*/
        
    }

	public void sendQueryToServer(String query)
	{
		out.println(query);
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
