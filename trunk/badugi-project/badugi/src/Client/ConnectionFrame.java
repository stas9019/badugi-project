package Client;

import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.*;


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
    private JTextField adres, port;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
	private Client client;
    
	ConnectionFrame(Client client)
	{
		//setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
		this.client = client;
        connectionButton.addActionListener(this);
        
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void run() 
	{

		 ConnectionFrame f = new ConnectionFrame(client);
		 
		 adres = new JTextField(20);
	     port = new JTextField(20);

	     connectionButton = new JButton("Connect");
	     connectionButton.addActionListener(this);
	        
	     output = new JLabel();
		
		 f.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); // to avoid ghost processes in memory
		 f.setLayout(new GridLayout(6,1));
		 
		 f.add(new JLabel("Server adress"));
		 f.add(adres);
		 f.add(new JLabel("Port"));
	     f.add(port);
	     f.add(output);
	     f.add(connectionButton);
		 
	     f.pack();
		 f.setVisible(true);
	
	}

}
