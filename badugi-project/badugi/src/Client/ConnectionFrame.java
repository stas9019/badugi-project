package Client;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;

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
 * Make constant size
 * Set position in center of display
 */

public class ConnectionFrame extends JFrame implements ActionListener//, Runnable
{
	private JLabel output;
	private JButton connectionButton;
    private JTextField adressField, portField;
	
    private Client client;
    
	ConnectionFrame(Client client)
	{
		 super();
		 
		 this.client = client;
		 adressField = new JTextField(20);
	     portField = new JTextField(20);
	     
	     connectionButton = new JButton("Connect");
	     connectionButton.addActionListener(this);
	     
	     output = new JLabel();
	     
	     
	     setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); // to avoid ghost processes in memory
		 setLayout(new GridLayout(6,1));
		 
		 add(new JLabel("Server adress"));
		 add(adressField);
		 
		 add(new JLabel("Port"));
	     add(portField);
	     
	     add(connectionButton);
	     
	     add(output);
	     
	     pack(); 
	     setVisible(true);
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
}
