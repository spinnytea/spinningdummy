package spinnytea.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Test;

public class TestRandomAccessCollection
{
	@Test
	public void hash()
	{
		test(new RandomAccessCollection<String>(new HashMap<String, Integer>()));
	}

	@Test
	public void tree()
	{
		test(new RandomAccessCollection<String>(new TreeMap<String, Integer>()));
	}

	private void test(RandomAccessCollection<String> rac)
	{
		rac.setSeed(1L);

		// there is nothing in the list
		assertNull(rac.remove());

		// just one element
		rac.add("banana");
		assertEquals(1, rac.size());
		assertEquals("banana", rac.remove());
		assertEquals(0, rac.size());
		assertNull(rac.remove());

		// now add a bunch
		rac.add("one");
		rac.add("two");
		rac.add("three");
		rac.add("four");
		rac.add("five");
		assertEquals(5, rac.size());
		assertEquals("four", rac.remove());
		assertEquals("two", rac.remove());
		assertEquals(3, rac.size());
		rac.add("six");
		rac.add("seven");
		rac.add("eight");
		assertEquals(6, rac.size());
		assertEquals("six", rac.remove());
		assertEquals("seven", rac.remove());
		assertEquals("one", rac.remove());
		assertEquals("three", rac.remove());
		assertEquals("five", rac.remove());
		assertEquals("eight", rac.remove());
		assertEquals(0, rac.size());
		assertNull(rac.remove());
	}
}
