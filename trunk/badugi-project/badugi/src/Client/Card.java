package Client;

/*♥♦♣♠ ♡♢♧♤*/

public class Card // implements Comparable<Card>
{
	private int color;
	private int figure;
	
	private boolean marked = false;
	
	protected Card(int colour, int figure)
	{
		this.color = colour;
		this.figure = figure;
	}
	
	protected void markCard()
	{
		marked = true;
	}
	
	protected boolean isMarked()
	{
		return marked;
		
	}
	/*
	 * set and get functions to card color and figure
	 */
	 
	protected void setCardColor(int cardColor)
	{
		color = cardColor;
	}
	protected void setCardFigure(int cardFigure)
	{
		figure = cardFigure;
	}

	protected int getCardColor()
	{
		return color;
	}
	
	protected int  getCardFigure()
	{
		return figure;
	}
	
	protected String getCardFigureLexic()		//especially for sorting
	{		
		if(figure == 10)
			return "B";
		if(figure == 11)
			return "C";
		if(figure == 12)
			return "D";
		if(figure == 13)
			return "E";
		else
			return "A"+ figure;
	}
	
	 public String toString() {
         return "color " + color + " " + " figure " + figure ;
     }
	/*@Override													//revision 30
	public int compareTo(Card card)
	{
		return String.valueOf(this.getCardColor()).compareTo(String.valueOf(card.getCardColor()));      
	}*/
}
