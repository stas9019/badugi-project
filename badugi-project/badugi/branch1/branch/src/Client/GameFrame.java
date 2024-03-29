package Client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import badugi.Card;

public class GameFrame extends JFrame implements ActionListener, Runnable
{
	
	private JLabel gameStatus, moneyLeft, dealer;
	private JLabel cards[] = new JLabel[4];
	private Client client;
	private JButton betButton, callButton, raiseButton, allInButton, checkButton, foldButton;
	private JTextField sumField;
	
	
	GameFrame(Client client)
	{
		 this.client = client;
		 
		 betButton = new JButton("Bet");
		 callButton = new JButton("Call");
		 raiseButton = new JButton("Raise");
		 allInButton = new JButton("All in");
		 checkButton = new JButton("Check");
		 foldButton = new JButton("Fold");
		 
		 sumField = new JTextField(10);
		 gameStatus = new JLabel();
		 moneyLeft = new JLabel();
		 dealer = new JLabel();
		 
		  for(int i = 0; i < 4 ;i++)
		    	 cards[i] = new JLabel();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO cases for all kind off actions performed
		
	}
	@Override
	public void run()
	{
		GameFrame f = new GameFrame(client);
		 
		 f.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); // to avoid ghost processes in memory
		 f.setLayout(new GridBagLayout());
		 GridBagConstraints c = new GridBagConstraints();
		 
		 
		 /*make order for elements of frame*/
		 c.fill = GridBagConstraints.HORIZONTAL;
		 c.weightx = 1;
	     c.gridx = 0;
	     c.gridy = 0;
	     c.gridwidth = 1;
		 f.add(dealer,c);
		 
		 
		 
		 c.weightx = 1;
	     c.gridx = 0;
	     c.gridy = 1;
	     c.gridwidth = 1;
		 f.add(moneyLeft,c);
		 
	    
	     //betButton, callButton, raiseButton, allInButton, checkButton, foldButton
	     f.add(betButton);
	     
	     f.add(callButton);
	     f.add(raiseButton);
	     f.add(allInButton);
	     f.add(checkButton);
	     f.add(foldButton);
	     f.add(sumField);
	     
	     for(int i = 0; i < 4 ;i++)
	    	 f.add(cards[i]);
	     
	     f.add(gameStatus);
	    
		 
	     f.pack();
		 f.setVisible(true);
	
		
	}

}
