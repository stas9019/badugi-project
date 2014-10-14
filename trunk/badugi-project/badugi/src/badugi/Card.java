package badugi;

public class Card 
{
	private int color;
	private int figure;
	private boolean beenUsed;
	
	Card(int colour, int figure)
	{
		this.color = colour;
		this.figure = figure;
		beenUsed = false;
	}
	
	/*
	 * set and get functions to card figure
	 */
	public void setCardFigure(int cardFigure)
	{
		this.figure = cardFigure;
	}
	
	public int  getCardFigure()
	{
		return figure;
	}
	
	/*
	 * set and get functions to card color
	 */
	public void setCardColor(int cardColor)
	{
		this.color = cardColor;
	}
	
	public int getCardColor()
	{
		return color;
	}
	
	public void useCard()
	{
		beenUsed = true;
	}
	
	public void returnCard()
	{
		beenUsed = false;
	}
}
