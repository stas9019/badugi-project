package badugi;

public class Card 
{
	private int color;
	private int figure;
	private boolean beenUsed;
	
	public Card(int colour, int figure)
	{
		this.color = colour;
		this.figure = figure;
		beenUsed = false;
	}
	
	/*
	 * set and get functions to card figure
	 
	public void setCardFigure(int cardFigure)
	{
		this.figure = cardFigure;
	}

	/*
	 * set and get functions to card color
	 */
	 
	public void setCardColor(int cardColor)
	{
		this.color = cardColor;
	}
	public void setCardFigure(int cardFigure)
	{
		this.figure = figure;
	}

	public int getCardColor()
	{
		return color;
	}
	
	public int  getCardFigure()
	{
		return figure;
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
