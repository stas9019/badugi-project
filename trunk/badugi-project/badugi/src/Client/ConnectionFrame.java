package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConnectionFrame extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lAdress, lPort, lOutput;
	private JTextField tAdress, tPort;
	private JButton bConnect;

	private Client client;

	public ConnectionFrame(Client client)
	{
		super();
		this.client = client;
		
		setSize(250,200);
		setTitle("Getting Connection");
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); //set JFrame in the middle of screen
		setResizable(false); //not allowed do change size of JFrame
		setLayout(null);

		lAdress = new JLabel("Server adress:");
		lAdress.setBounds(10, 10, 225, 20);
		add(lAdress);

		tAdress = new JTextField(20);
		tAdress.setBounds(10, 30, 225, 20);
		add(tAdress);

		lPort = new JLabel("Port number:");
		lPort.setBounds(10, 50, 225, 20);
		add(lPort);

		tPort = new JTextField(20);
		tPort.setBounds(10, 70, 225, 20);
		add(tPort);

		bConnect = new JButton("Connect");
		bConnect.setBounds(30, 105, 185, 20);
		add(bConnect);
		bConnect.addActionListener(this);

		lOutput = new JLabel("");
		lOutput.setBounds(10, 140, 225, 20);
		//lOutput.setBackground(new Color(200,200,200));
		//lOutput.setOpaque(true);
		add(lOutput);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		lOutput.setText("");

		String adress = tAdress.getText();
		String port  = tPort.getText();

		if(e.getSource() == bConnect)
			client.connectionAttempt(adress, port);
	}

	public void setOutputText(String text)
	{
		lOutput.setText(text);
	}

	public String getOutputText()
	{
		return lOutput.getText();
	}

	public void blockConnectButton()
	{
		bConnect.setEnabled(false);
	}

}