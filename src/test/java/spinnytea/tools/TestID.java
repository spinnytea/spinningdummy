package spinnytea.tools;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestID
{
	@Test
	public void tokens()
	{
		char[] tokens = Arrays.copyOf(ID.tokens, ID.tokens.length);
		assertArrayEquals(ID.tokens, tokens);

		// verify that the tokens are already sorted
		Arrays.sort(tokens);
		assertArrayEquals(ID.tokens, tokens);

		// it is perfectly acceptable to change the length
		// this doesn't even really need to be a test
		// but I wanted to ensure that I was using all the upper/lower/number characters
		assertEquals(36, tokens.length);

		for(char c = '0'; c <= '9'; c++)
			assertFalse(Arrays.binarySearch(tokens, c) < 0);

		for(char c = 'a'; c <= 'z'; c++)
			assertFalse(Arrays.binarySearch(tokens, c) < 0);

//		for(char c = 'A'; c <= 'Z'; c++)
//			assertFalse(Arrays.binarySearch(tokens, c) < 0);
	}

	@Test
	public void nextId()
	{
		// check the first few increments
		ID id = new ID();

		// set of single digits
		for(int i = 1; i < ID.tokens.length; i++)
		{
			assertEquals("" + ID.tokens[i], id.nextID());
		}

		// set of double digits
		for(int i = 1; i < ID.tokens.length; i++)
			for(int j = 0; j < ID.tokens.length; j++)
				assertEquals(ID.tokens[i] + "" + ID.tokens[j], id.nextID());
	}
}
