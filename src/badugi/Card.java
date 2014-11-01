package badugi;

/*♥♦♣♠ ♡♢♧♤*/

public class Card 
{
	private int colour;
	private int figure;
	private boolean beenUsed;
	
	public Card(int colour, int figure)
	{
		this.colour = colour;
		this.figure = figure;
		//System.out.println("New card "+colour+figure);
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
		this.colour = cardColor;
	}
	public void setCardFigure(int cardFigure)
	{
		this.figure = cardFigure;
	}

	public int getCardColor()
	{
		return colour;
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
