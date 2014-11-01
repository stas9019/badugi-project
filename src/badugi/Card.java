package badugi;

/*♥♦♣♠ ♡♢♧♤*/

public class Card // implements Comparable<Card>
{
	private int color;
	private int figure;
	
	public Card(int colour, int figure)
	{
		this.color = colour;
		this.figure = figure;
	}
	


	/*
	 * set and get functions to card color and figure
	 */
	 
	public void setCardColor(int cardColor)
	{
		this.color = cardColor;
	}
	public void setCardFigure(int cardFigure)
	{
		this.figure = cardFigure;
	}

	public int getCardColor()
	{
		return color;
	}
	
	public int  getCardFigure()
	{
		return figure;
	}
	
	public String getCardFigureLexic()		//especially for sorting
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
