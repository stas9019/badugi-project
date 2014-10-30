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
    private String adress;
    private int port;
    
	ClientWorker(Client client, Socket socket) //was Server server
	{
		this.socket=socket;
		this.client = client;
		
		connectionFrame = client.getConnectionFrame();
	}
	
	public void sayToClient(String answer)
	{
		
		out.println(answer);
		System.out.println("Client worker " +answer);
	}
	
	
	public void listenSocket() throws NullPointerException
	{
		
	        try 
	        {
	        	out = new PrintWriter(socket.getOutputStream(), true);
	        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
					System.out.println("Client worker " +text);
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
		        	connectionFrame.setVisible(false);
		        	
		        	client.invokeGameFrame();
		        	client.initializeSuit();	        	
		        }
		        
		        if(text.startsWith("Start cash "))
		        {
		        	int cash = Integer.parseInt(text.substring(11));
		        	client.setPlayerMoney(cash);
		        	System.out.println("Player cash "+cash);
		        }
		        
		        if(text.equals("First card distribution"))
		        {
		        	client.getCards();
		        }
		        //!!!   Format is like "New card x y" where x - card color, y - card figure
		        if(text.startsWith("New card"))
		        {
		        	String color = String.valueOf(text.charAt(9));
		        	String figure = text.substring(11);	
		        	client.takeNewCard(color, figure);
		        }
		        
		        
			}      
    }

	@Override
	public void run()
	{
		try
		{
			listenSocket();
		}
		catch(NullPointerException e)
		{
			//listenSocket();
			System.out.println("end");
		}
		
	}
	
	protected void finalize() {
        try 
        {
          in.close();
          out.close();
        } 
        catch (IOException e) 
        {
          System.out.println("Could not close.");// System.exit(-1);
        }
    }
}
