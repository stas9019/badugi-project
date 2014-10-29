package Client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GameFrame extends JFrame implements ActionListener//, Runnable
{
	
	private JLabel gameStatus, moneyLeft, dealer;
	private JLabel cards[] = new JLabel[4];
	private Client client;
	private JButton betButton, callButton, raiseButton, allInButton, checkButton, foldButton;
	private JTextField sumField;
	
	/*
	 * TO-DO
	 * Size
	 * Position
	 * */
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
		 
		 betButton.addActionListener(this);
		 callButton.addActionListener(this);
		 raiseButton.addActionListener(this);
		 allInButton.addActionListener(this);
		 checkButton.addActionListener(this);
		 foldButton.addActionListener(this);
		 
		 for(int i = 0; i < 4 ;i++)
			 cards[i] = new JLabel("Card" +i);
		  
		 setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); // to avoid ghost processes in memory
		 setLayout(new GridBagLayout());
		 GridBagConstraints c = new GridBagConstraints();
			 
			 
			 /*make order for elements of frame*/
		 c.fill = GridBagConstraints.HORIZONTAL;
		 c.weightx = 1;
		 c.gridx = 0;
		 c.gridy = 0;
		 c.gridwidth = 1;
		 add(dealer,c);
			 
			 
			 
		 c.weightx = 2;
		 c.gridx = 0;
		 c.gridy = 1;
		 c.gridwidth = 1;
		 add(moneyLeft,c);
			 
		 add(betButton);  
		 add(callButton);
		 add(raiseButton);
		 add(allInButton);
		 add(checkButton);
		 add(foldButton);
		 add(sumField);
		     
		 for(int i = 0; i < 4 ;i++)
		 {
			 add(cards[i]);
		 }
		     
		 add(gameStatus);
		    
			 
		 pack();
		 setVisible(true);

	}
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		/* TODO cases for all kind off actions performed
		 * if(event.getSource() == ...) for all buttons
		 */
		
		if(event.getSource() == callButton)
		{
			client.sendQueryToServer("ready");
		}
	}
	
	public void setStatusText(String text)
	{
		gameStatus.setText(text);
	}
}
