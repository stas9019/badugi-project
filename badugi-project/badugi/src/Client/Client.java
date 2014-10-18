package Client;

import javax.swing.SwingUtilities;

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

	private Card playerSuit[] = new Card[4];
	private int playerMoney;
	private boolean isDealer;
	private ConnectionFrame frame;
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	Client()
	{
		frame = new ConnectionFrame(this); 
		
		SwingUtilities.invokeLater(frame);
	}
	
	void connectionAttempt(String adress, String port)
	{
		try
		{
            listenSocket(adress, Integer.parseInt(port));
            //frame.setOutputText("Connected");
	    }
	    catch(NullPointerException e)
		{
	    	frame.setOutputText("Connection failed");
	    	return;
	    }	
		catch  (NumberFormatException e) 
		{
			frame.setOutputText("Wrong port: "+ port); //System.exit(1);
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
        	
        	frame.setOutputText(in.readLine());
        }
        catch(NullPointerException e)
        {
        	frame.setOutputText("No connection");
        }
        catch(NumberFormatException e) 
        {
        	frame.setOutputText("NumberFormatException"); 
        }
        catch (UnknownHostException e) 
        {
        	frame.setOutputText("Unknown host: "+ adress); 
        }
        catch  (IOException e) 
        {
        	frame.setOutputText("No I/O"); 
        }
        catch (IllegalArgumentException e) 
        {
        	frame.setOutputText("Wrong port: "+ port); 
        }
        
        if(frame.getOutputText().equals("Connected!"))
        	frame.blockConnectButton();
    }
	
	public void sendQueryToServer()
	{
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
	
	public void setAsDealer()
	{
		isDealer = true;
	}
	
	
	
	public static void main(String[] args) 
	{
		Client client = new Client();
	}

	
	

	

}
