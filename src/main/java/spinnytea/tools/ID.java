package spinnytea.tools;

/**
 * Counts through a character based generated id.<br>
 * It acts much the same way as a numbering system (say decimal or hexadecimal),
 * but, instead of just using numerals, it uses alpha characters, too.<br>
 * Underscores are not used, so you can use that character for other reasons (such as, separating types with the same id)
 * <p/>
 * This classes requires a "Config" to save the "nextID" that will be generated (so it persists between runs).
 * This uses the "nextID" variable name for the given owner.
 *
 * @author chicarksey
 */
public class ID
{
	private char[] nextID;

	// do not include underscores (this way, they can be used for other things)
	// cannot include characters that are disallowed by the file system
	private static final char[] tokens = {
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'a', 'b', 'c', 'd', 'e', 'f',
	'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
	};

	/** Needs to have an owner so it can use "saveConfig" ~ if you don't wish to use Config, then you may pass in null */
	public ID()
	{
		nextID = new char[] { tokens[1] };
	}

	/**
	 * This is not synchronized because it is not up to this class to manage that.
	 * Different applications require different synchronization.
	 * <p/>
	 * If you need this value to be atomic, then you need to put it in a synchronized block
	 */
	public String nextID()
	{
		String ret = new String(nextID);

		char[] temp = increment(nextID, nextID.length - 1);
		if(temp != null)
			nextID = temp;

		return ret;
	}

	private static char[] increment(char[] id, int pos)
	{
		if(pos == -1)
		{
			char[] ret = new char[id.length + 1];
			ret[0] = tokens[1];
			for(int i = 1; i < ret.length; i++)
				ret[i] = tokens[0];
			return ret;
		}

		char c = id[pos];
		int c_i = -1;
		for(int i = 0; i < tokens.length; i++)
			if(tokens[i] == c)
			{
				c_i = i + 1;
				break;
			}

		if(c_i < tokens.length)
		{
			id[pos] = tokens[c_i];
		}
		else
		{
			id[pos] = tokens[0];
			return increment(id, pos - 1);
		}

		return null;
	}
}
