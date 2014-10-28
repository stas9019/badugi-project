package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import badugi.Game;

public class ClientWorker implements Runnable
{
	
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
    private Client client;
    private ConnectionFrame connectionFrame;
    String adress;
    int port;
    
	ClientWorker(Client client, ConnectionFrame connectionFrame, String adress, int port ) //was Server server
	{
		this.client = client;
		this.connectionFrame = connectionFrame;
		this.adress = adress;
		this.port = port;
	}
	
	public void sayToClient(String answer)
	{
		
		out.println(answer);
		System.out.println("Client worker " +answer);
	}
	
	
	public void listenSocket(String adress, int port)
	{
		
	        try 
	        {
	        	socket = new Socket(adress, port);
	        	out = new PrintWriter(socket.getOutputStream(), true);
	        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	          
	        	System.out.println("Connected on " + socket.getPort());
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
	        
	        String text = "";
	        
	        while( text != null )
			{
	        	
		        try
				{
		        	System.out.println("Client text " + text);
					text = in.readLine();
					
		        	//connectionFrame.setOutputText(text);
				} 
	        	
	        	catch (IOException e)
				{
	        		connectionFrame.setOutputText("No I/O"); 
				}
		        
		        /*use frame's outputText only here, then in.readline().equals("SomeString")*/
		        if(text.equals("Connected!"))
		        {
		        	client.blockConnectionFrame();
		        	
		        	//sendQueryToServer(" asdfghjkl;");
		        	//text = "";
		        	//initSuit();
		        }
		        
		        if(text.equals("Game starts!"))
		        {
		        	System.out.println("Client game");
		        	client.invokeGameFrame();
		        	//text = "";
		        	
		        	//connectionFrame.blockConnectButton();
		        	//connectionFrame.setVisible(false);
		        	
		        	
		        	//gameFrame.setVisible(true);
		        	//sendQueryToServer(" 12345678");
		        	
		        	
		        	//gameFrame.setVisible(true);
		        	//SwingUtilities.invokeLater(gameFrame);
		        	
		        	
		        	//client.initSuit();
		        	
		        
		        }
		        
		        
		        
		        /*server's answer analyze part
		         * maybe some special function for this*/
			}      
    }

	@Override
	public void run()
	{
		listenSocket(adress, port);
		
	}
}
