package Client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

	/*
	 * To Add:
	 * Dealer/smallBlind/bigBlind
	 * Status
	 * change button + checkBoxes
	 * time left label
	 */

public class GameFrame2 extends JFrame
{
	//private Client client;
	private JButton bConfirm, bBet, bCall, bRaise, bAllIn, bCheck, bFold, bChange;
	private JLabel lMoney, lPot;
	private JButton cards[] = new JButton[4];
	private JCheckBox boxes[] = new JCheckBox[4];
	private JTextField tBid;

	GameFrame2(/*Client client*/)
	{
		super();
		//this.client = client;

		setSize(680,423);
		setTitle("Poker - Badugi");
		setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); //set JFrame in the middle of screen
		setResizable(false); //not allowed do change size of JFrame
		setLayout(null);

		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/Client/back.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		lMoney = new JLabel("  Your money: ");
		lMoney.setBounds(25, 285, 205, 20);
		lMoney.setBackground(new Color(250,250,250));
		lMoney.setOpaque(true);
		add(lMoney);
				
		lPot = new JLabel("  Current pot: ");
		lPot.setBounds(235, 285, 205, 20);
		lPot.setBackground(new Color(250,250,250));
		lPot.setOpaque(true);
		add(lPot);
		
		tBid = new JTextField("  Your bid");
		tBid.setBounds(445, 285, 100, 20);
		tBid.setEnabled(false);
		add(tBid);
		
		bConfirm = new JButton("Confirm");
		bConfirm.setBounds(550, 285, 100, 20);
		bConfirm.setEnabled(false);
		add(bConfirm);
		
		bBet = new JButton("Bet");
		bBet.setBounds(25, 310, 100, 40);
		add(bBet);
		
		bCall = new JButton("Call");
		bCall.setBounds(130, 310, 100, 40);
		add(bCall);
		
		bRaise = new JButton("Raise");
		bRaise.setBounds(235, 310, 100, 40);
		add(bRaise);
		
		bAllIn = new JButton("All In");
		bAllIn.setBounds(340, 310, 100, 40);
		add(bAllIn);
		
		bCheck = new JButton("Check");
		bCheck.setBounds(445, 310, 100, 40);
		add(bCheck);
		
		bFold = new JButton("Fold");
		bFold.setBounds(550, 310, 100, 40);
		add(bFold);
		
		for(int i = 0; i < 4 ;i++) {
			cards[i] = new JButton("Card" +i);
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
		add(bChange);
		

		setVisible(true);




	}

	public static void main(String[] args)
	{
		GameFrame2 neu = new GameFrame2();

	}
}
