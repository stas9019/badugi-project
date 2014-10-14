package badugi;

public class Card 
{
	private int color;
	private int figure;
	private boolean beenUsed;
	
	Card(int color, int figure)
	{
		this.color = color;
		this.figure = figure;
		beenUsed = false;
	}
}
