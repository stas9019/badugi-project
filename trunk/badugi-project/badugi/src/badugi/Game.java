package badugi;

import java.util.Random;

public class Game
{
	private int round;
	private int bank, money, port;
	private ClientWorker workers[];
	private Card suit[];
	public Random random = new Random();
	
	/*
	 * initializing card suite
	 */
	Game(ClientWorker workers[], int money)
	{
		//this.playersNumber = playersNumber; // we have length of workers-1
		this.workers = workers;
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
	
	Card takeNewCard()
	{
		Card randomCard = suit[random.nextInt(51)];
		return randomCard;
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
