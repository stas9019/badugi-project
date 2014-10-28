package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import badugi.Game;

/*TO-DO
 * Finalize method
 * */


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
	          
	        	//System.out.println("Connected on " + socket.getPort());
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
					text = in.readLine();					
				} 
	        	
	        	catch (IOException e)
				{
	        		connectionFrame.setOutputText("No I/O"); 
				}
		        
		        /*server's answer analyze part
		         * maybe some special function for this*/
		        
		        if(text.equals("Connected!"))
		        {
		        	connectionFrame.blockConnectButton();
		        	connectionFrame.setOutputText("Connected!");		        	
		        }
		        
		        if(text.equals("Game starts!"))
		        {
		        	System.out.println("Client game");
		        	connectionFrame.setVisible(false);
		        	
		        	client.invokeGameFrame();
		        	client.initSuit();	        	
		        }
			}      
    }

	@Override
	public void run()
	{
		listenSocket(adress, port);
		
	}
}
