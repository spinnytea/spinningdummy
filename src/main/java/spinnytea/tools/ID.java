package spinnytea.tools;

import java.util.Arrays;

/**
 * Counts through a character based generated id.<br>
 * It acts much the same way as a numbering system (say decimal or hexadecimal), but, instead of just using numerals, it uses alpha characters, too.<br>
 * Underscores are not used, so you can use that character for other reasons (such as, separating types with the same id)
 */
public class ID
{
	/**
	 * <p/>
	 * does not include underscores or periods (this way, they can be used for other things such as delimiters)
	 * <p/>
	 * does not include characters that are disallowed by the file system (this way this can be used in a filename)
	 * <p/>
	 * in sorted order so we can use {@link java.util.Arrays#binarySearch(char[], char)}
	 */
	static final char[] tokens = { //
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', // numbers
//		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', // upper case letters
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', // lower case letters
	};

	/** in case you want to extend the ID */
	public static final char getZero()
	{
		return tokens[0];
	}

	private StringBuffer nextID;

	/** This will start from one (not zero). */
	public ID()
	{
		nextID = new StringBuffer("" + tokens[1]);
	}

	public synchronized String nextID()
	{
		String ret = nextID.toString();

		increment(nextID.length() - 1);

		return ret;
	}

	/**
	 * <p/>
	 * does not have very much error checking since this is private
	 * <p/>
	 * recursive increment
	 */
	private void increment(int index)
	{
		boolean keepGoing = true;
		while(keepGoing)
		{
			// if any other negative value is supplied, this will throw a StringArrayIndexOutOfBoundsException
			if(index == -1)
			{
				nextID.insert(0, tokens[1]);
				keepGoing = false;
			}
			else
			{
				// get the next token index
				int idx = Arrays.binarySearch(tokens, nextID.charAt(index)) + 1;

				// if we can't increase this anymore, then increase the next value
				if(idx == tokens.length)
				{
					// increment the value before recursion
					// when we roll over (99 -> 100), our index will be off by one
					nextID.setCharAt(index, tokens[0]);
//					increment(index - 1); // _XXX do a loop instead of recursion
//					 - I just wrapped the function in while(keepGoing)
					index--;
				}
				else
				{
					nextID.setCharAt(index, tokens[idx]);
					keepGoing = false;
				}
			}
		}
	}
}
