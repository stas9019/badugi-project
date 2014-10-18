package badugi;

public class Game
{
	private int round;
	private int bank, playersNumber, money, port;
	
	private Card suit[];
	
	
	/*
	 * initializing card suite
	 */
	Game(int playersNumber, int money)
	{
		this.playersNumber = playersNumber;
		this.money = money;
		
		suit = new Card[52];
		
		for(int i=1; i<=13; i++)
		{
			for(int j=1; j<=4; j++)
			{
				suit[i*j-1] = new Card(i,j);
				//suit[i*j-1].setCardFigure(i);
				//suit[i*j-1].setCardColor(j);
			}
		}
	}
	
	void nullBank()
	{
		bank = 0;
	}
	
	void nextRound()
	{
		round++;
	}
	
}
