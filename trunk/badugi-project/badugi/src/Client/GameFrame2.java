package Client;
import java.awt.Color;
import java.awt.Font;
//import java.awt.Graphics;							// revision 30
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JPanel;
import javax.swing.JTextField;

	/*
	 * To Add:
	 * Dealer/smallBlind/bigBlind
	 * make Status bar which will inform about some general processes f.ex. card changing, some player made pot ... etc
	 * player pot					change places of current pot and player pot
	 * time left label(not necessary, maybe if we will have time to do this)
	 * if we leaving text labels on cards, it can look better with bigger size and colors
	 */

public class GameFrame2 extends JFrame implements ActionListener
{
	/**
	 * serial version?
	 */
	private static final long serialVersionUID = -2977435916670193465L;
	private Client client;
	private JButton bReady, bBet, bCall, bRaise, bAllIn, bCheck, bFold, bChange;
	private JLabel lMoney, lCurrPot, lPot;
	private JButton cards[] = new JButton[4];
	private JCheckBox boxes[] = new JCheckBox[4];
	private JTextField tBid;

	GameFrame2(Client client)
	{
		super();
		this.client = client;

		setSize(680,423);
		setTitle("Poker - Badugi");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//better leave dispose and invoke connection frame again, but here it's not critical
		setLocationRelativeTo(null); //set JFrame in the middle of screen
		setResizable(false); //not allowed do change size of JFrame
		setLayout(null);

		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/Client/back.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		lMoney = new JLabel("  Your money: ");
		lMoney.setBounds(25, 85, 205, 20);				//revision 30
		lMoney.setBackground(new Color(250,250,250));
		lMoney.setOpaque(true);
		add(lMoney);
			
		lPot = new JLabel("  Your pot: ");				//revision 30
		lPot.setBounds(25, 285, 205, 20);
		lPot.setBackground(new Color(250,250,250));
		lPot.setOpaque(true);
		add(lPot);
		
		lCurrPot = new JLabel("  Current pot: ");
		lCurrPot.setBounds(235, 285, 205, 20);
		lCurrPot.setBackground(new Color(250,250,250));
		lCurrPot.setOpaque(true);
		add(lCurrPot);
		
		tBid = new JTextField("  Your bid: ");
		tBid.setBounds(445, 285, 100, 20);
		tBid.setEnabled(false);
		add(tBid);
		
		bReady = new JButton("Ready");				//revision 32
		bReady.setBounds(550, 285, 100, 20);
		bReady.setEnabled(false);
		bReady.addActionListener(this);
		bReady.setEnabled(false);
		add(bReady);
		
		bBet = new JButton("Bet");
		bBet.setBounds(25, 310, 100, 40);
		bBet.addActionListener(this);
		bBet.setEnabled(false);
		add(bBet);
		
		bCall = new JButton("Call");
		bCall.setBounds(130, 310, 100, 40);
		bCall.addActionListener(this);
		bCall.setEnabled(false);
		add(bCall);
		
		bRaise = new JButton("Raise");
		bRaise.setBounds(235, 310, 100, 40);
		bRaise.addActionListener(this);
		bRaise.setEnabled(false);
		add(bRaise);
		
		bAllIn = new JButton("All In");
		bAllIn.setBounds(340, 310, 100, 40);
		bAllIn.addActionListener(this);
		bAllIn.setEnabled(false);
		add(bAllIn);
		
		bCheck = new JButton("Check");
		bCheck.setBounds(445, 310, 100, 40);
		bCheck.addActionListener(this);
		bCheck.setEnabled(false);
		add(bCheck);
		
		bFold = new JButton("Fold");
		bFold.setBounds(550, 310, 100, 40);
		bFold.addActionListener(this);
		bFold.setEnabled(false);
		add(bFold);
		
		for(int i = 0; i < 4 ;i++) {
			cards[i] = new JButton();
			cards[i].setBounds(150+(i*95), 140, 90, 130);
			add(cards[i]);
		}

		for(int i = 0; i < 4 ;i++) {
			boxes[i] = new JCheckBox("");
			boxes[i].setBounds(185+(i*95), 120, 20, 15);
			boxes[i].setOpaque(false); //transparent background behind checkboxes xD
			add(boxes[i]);
		}
		
		bChange = new JButton("<html><center>Change<br/>cards</center></html>");
		bChange.setBounds(550, 230, 100, 40);
		bChange.addActionListener(this);
		bChange.setEnabled(false);
		add(bChange);
		

		setVisible(true);
	}

	

	/*_________________________________________________
	 *            Block/Unblock functions part
	 *_________________________________________________*/
	
	protected void blockCallButton(boolean b)
	{
		bCall.setEnabled(!b);
	}
	
	protected void blockReadyButton(boolean b)		//revision 32
	{
		bReady.setEnabled(!b);
	}

	protected void blockRaiseButton(boolean b)
	{
		bRaise.setEnabled(!b);
	}
	
	protected void blockCheckButton(boolean b)
	{
		bCheck.setEnabled(!b);
	}
	
	protected void blockChangeButton(boolean b)
	{
		bChange.setEnabled(!b);
	}
	
	protected void blockAllInButton(boolean b)
	{
		bAllIn.setEnabled(!b);
	}
	
	protected void blockFoldButton(boolean b)
	{
		bFold.setEnabled(!b);
	}
	
	protected void blockYourBidField(boolean b)		// is necessary?
	{
		
		tBid.setEnabled(!b);
		
		if(b)
		{
			tBid.setText("  Your bid: ");
		}
		else
		{
			tBid.setText("");
		}
	}
	
	protected void blockCheckBoxes(boolean b)
	{
		for(int i=3; i>=0; i--)
			boxes[i].setEnabled(!b);
	}
	/*_________________________________________________
	 *            Set/get functions part
	 *_________________________________________________*/
	
	//set player pot function
	
	protected void setYourMoney(String playerMoney)
	{
		lMoney.setText("  Your money: " + playerMoney);
	}
	
	protected void setCardView(int cardNum, String view, Color color)
	{
		cards[cardNum].setText(view);
		cards[cardNum].setFont(new Font(null, Font.PLAIN, 22));
		cards[cardNum].setForeground(color);
	}
	
	protected void setCurrentPot(int currentPot)//revision 30
	{
		lCurrPot.setText("  Current pot: " + currentPot);
	}
	
	protected void setYourPot(int playerMoney)
	{
		lPot.setText("  Your pot: " + playerMoney);
	}
	
	protected int getYourBid()
	{		
		/*check bid*/
		int bid = 0;
		
		try
		{
			bid = Integer.parseInt(tBid.getText());
		}
		catch(NumberFormatException e)
		{
			System.out.println("Wrong sum to raise");
		}
			
		return bid;		
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		/* TODO cases for all kind off actions performed
		 * if(event.getSource() == ...) for all buttons
		 */
		
		if(event.getSource() == bCall)
		{
			client.call();
		}
		
		if(event.getSource() == bRaise)
		{
			int bid = getYourBid();
			
			if(bid != 0)
				client.raise(bid);
		}
		
		if(event.getSource() == bAllIn)
		{
			client.allIn();
		}
		
		if(event.getSource() == bCheck)
		{
			client.check();
		}
		
		if(event.getSource() == bReady)
		{
			client.sendQueryToServer("Ready");
			bReady.setEnabled(false);
		}
		
		if(event.getSource() == bFold)
		{
			client.fold();
		}
		
		if(event.getSource() == bChange)
		{
			
			for(int i=3; i>=0; i--)
			{
				if(boxes[i].isSelected())
				{
					client.changeCards(i);
					boxes[i].setSelected(false);	//revision 30	
				}
			}
			
			blockChangeButton(true);
			blockCheckBoxes(true);					//revision 30
			client.sendQueryToServer("End of changing");
			
		}
	}
	
}
