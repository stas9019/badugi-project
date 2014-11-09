package Client;

import static org.junit.Assert.*;

import org.junit.Test;

import badugi.Card;

public class CardTest
{
	int colour, figure;
	Card card = new Card(colour, figure);


	@Test
	//requires a change in the type of method from "protected" to "public"
	public void testGetCardColor()
	{
		for (int i=1; i<=4; i++)
		{
			card = new Card(i,5);
			int result = card.getCardColor();
			assertEquals(result, i);
		}
	}

	@Test
	//requires a change in the type of method from "protected" to "public"
	public void testGetCardFigure()
	{
		for (int i=1; i<=13; i++)
		{
			card = new Card(1,i);
			int result = card.getCardFigure();
			assertEquals(result, i);
		}
	}

	@Test
	//requires a change in the type of method from "protected" to "public"
	public void testGetCardFigureLexic()
	{
		card = new Card(1,10);
		String result = card.getCardFigureLexic();
		assertEquals(result, "B");
	}
	
	@Test
	public void testToString()
	{
		for (int i=1; i<=4; i++)
		{
			for (int j=1; j<=4; j++)
			{
				card = new Card(i,j);
				String result = card.toString();
				assertEquals(result, "color " + i + " " + " figure " + j);
			}
		}
		
	}
}
