package Client;

import java.net.Socket;

import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.io.IOException;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


/*
 * This is client's View Layer 
 * 
 * TO DO 
 * Client's GUI: First Frame - connection frame, Second - Game Frame
 * Establishing connection by IP and port
 * 
 */

public class ConnectionFrame extends JFrame implements ActionListener, Runnable
{
	private JLabel output;
	private JButton connectionButton;
    private JTextField adressField, portField;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
	private Client client;
    
	ConnectionFrame(Client client)
	{
		//setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
		 this.client = client;
		 adressField = new JTextField(20);
	     portField = new JTextField(20);
	     
	     connectionButton = new JButton("Connect");
	     connectionButton.addActionListener(this);
	     
	     output = new JLabel();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		output.setText("");
		
		String adress = adressField.getText();
		String port  = portField.getText();
		
		if(event.getSource() == connectionButton)
			client.connectionAttempt(adress, port);
		
	}
	
	public void setOutputText(String text)
	{
		output.setText(text);
	}
	public String getOutputText()
	{
		return output.getText();
	}
	
	
	public void blockConnectButton()
	{
		connectionButton.setEnabled(false);
	}
	/*
	 * Refactor f to frame in the end
	 * */
	public void run() 
	{

		 ConnectionFrame f = new ConnectionFrame(client);
		 
		 f.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); // to avoid ghost processes in memory
		 f.setLayout(new GridLayout(6,1));
		 
		 f.add(new JLabel("Server adress"));
		 f.add(adressField);
		 
		 f.add(new JLabel("Port"));
	     f.add(portField);
	     
	     f.add(connectionButton);
	     
	     f.add(output);
	    
		 
	     f.pack();
		 f.setVisible(true);
	
	}

}
