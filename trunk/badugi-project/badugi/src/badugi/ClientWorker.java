package badugi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

import java.net.Socket;

public class ClientWorker implements Runnable
{
	Socket socket;
	private BufferedReader in = null;
    private PrintWriter out = null;
    
	ClientWorker(Socket socket, Game game) //was Server server
	{
		this.socket = socket;	
	}

	@Override
	public void run()
	{
		String query="";
	    
        try
        {
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	out = new PrintWriter(socket.getOutputStream(), true);
          
        	System.out.println("Connected with " + socket.getLocalSocketAddress());
        } 
        catch (IOException e) {
        	System.out.println("Acception failed: " + socket.getLocalSocketAddress()); 
        	return;
        }
        
        out.println("Connected!");
        
        while(query != null) 
        {
            try 	
            {
                //query = in.readLine();
                
                
                /*
            	 * Query analyzing
            	 * 
            	 * */
            	
            }
           /* catch (IOException e) 
            {
                System.out.println("Read failed"); 
                return;
            } */
            catch(NullPointerException e)
            {
                System.out.println("Unexpected connection fail " 
                					+socket.getLocalSocketAddress());
                return;
            }
        }
	}
}
