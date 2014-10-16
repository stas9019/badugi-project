package badugi;

public class Game
{
	private int round;
	private int bank, playersNumber, money, port;
	
	private Card suit[] = new Card[52];
	
	
	/*
	 * initializing card suite
	 */
	Game(int playersNumber, int money)
	{
		this.playersNumber = playersNumber;
		this.money = money;
		
		for(int i=1; i<=13; i++)
		{
			for(int j=1; j<=4; j++)
			{
				suit[i*j-1].setCardFigure(i);
				suit[i*j-1].setCardColor(j);
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
