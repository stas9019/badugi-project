package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*TO-DO
 * Finalize method
 * */
/**
* 
*
* @author Stas Zamana
*/
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
					System.out.println("Client worker: " +text);
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
		        	client.setGameStatus(text);
		        }
		        
		        if(text.equals("New game"))
		        {
		        	client.newGamePlayersChecking();
		        }
		       
		        if(text.startsWith("Draw"))
		        {
		        	client.setGameStatus(text);
		        }
		        
		        if(text.equals("Are you All-In?"))
		        {
		        	client.answerIfAllIn();
		        }
		        
		        if(text.startsWith("Start cash "))
		        {
		        	int cash = Integer.parseInt(text.substring(11));
		        	client.setPlayerMoney(cash);
		        	client.setGameStatus("Player cash "+cash);
		        }
		        
		        if(text.startsWith("Small blind "))
		        {
		        	int smallBlind = Integer.parseInt(text.substring(12));
		        	client.setBlinds(smallBlind);
		        	client.setGameStatus("Small blind "+smallBlind);
		        }
		        
		        if(text.startsWith("Dealer"))
		        {
		        	client.setAsDealer(true);
		        }
		        
		        if(text.startsWith("You are not dealer anymore"))//add to game class
		        {
		        	client.setAsDealer(false);
		        }
		        
		        if(text.equals("First card distribution"))
		        {
		        	client.newGameStarted();	
		        }
		     
		        if(text.equals("You are winner!"))		//revision 32
		        {
		        	//show in status text
		        	client.setGameStatus(text);
		        	client.gameOver();	
		        }

		        if(text.equals("Show your next highest figure"))		//revision 32
		        {
		        	//show in status text
		        	client.showNextHighestFigure();	
		        }
		        
		        if(text.equals("How many different colors do you have?"))		//revision 32
		        {
		        	//show in status text
		        	client.answerHowMuchColors();	
		        }
		        //!!!   Format is like "New card x y" where x - card color, y - card figure
		        if(text.startsWith("New card"))
		        {
		        	final String color = String.valueOf(text.charAt(9));
		        	String figure = text.substring(11);	
		        	client.takeNewCard(color, figure);
		        }
		        
		        if(text.startsWith("Bet small blind"))
		        {
		        	client.betSmallBlind();
		        }
		        
		        if(text.startsWith("Bet big blind"))
		        {
		        	client.betBigBlind();
		        }
		        
		        if(text.startsWith("Auction "))
		        {
		        	int currentPot = Integer.parseInt(text.substring(8));
		        	client.setGameStatus("Auction");
		        	client.auction(currentPot);
		        }
		        
		        if(text.startsWith("Check pot"))
		        {
		        	client.checkPot();
		        }
		        
		        if(text.startsWith("Your real pot?"))
				{
		        	client.checkRealPot();
				}
		        
		        if(text.startsWith("Change cards"))
		        {
		        	client.setGameStatus("You can change cards");
		        	client.cardChangingStage();
		        }
		        
		        if(text.startsWith("Your won! "))		//revision 32
		        {
		        	client.setAsWinner(true);
		        	client.setGameStatus(text);
		        	int bank = Integer.parseInt(text.substring(10));
		        	client.getBank(bank);
		        }
		        
		        if(text.startsWith("Take back "))		//revision 32
		        {
		        	client.setGameStatus(text);
		        	int bank = Integer.parseInt(text.substring(10));
		        	client.getBank(bank);
		        }
		        if(text.startsWith("Show your combination"))		//revision 32
		        {
		        	client.showYourCombination();
		        }
		       
		        if(text.startsWith("Winner combination"))		//revision 32
		        {
		        	if(!client.isWinner)
		        	{
		        		client.setGameStatus(text);
		        	}
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
