package spinnytea.tools;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RandomAccessCollection<T>
{
	private final Random random = new Random();
	// a HashMap is O(1), but more memory
	// a TreeMap is less memory, but O(log(n))
	private final Map<T, Integer> map;
	// this one needs to be random access
	private final ArrayList<T> list = new ArrayList<>();

	public void setSeed(long seed)
	{
		random.setSeed(seed);
	}

	/** add an element to the collection */
	public void add(T t)
	{
		// add the element to the list (at the end)
		int pos = list.size();
		map.put(t, pos);
		list.add(pos, t);
	}

	/** remove a random element from the list */
	public T remove()
	{
		if(list.isEmpty())
			return null;

		int retIdx = random.nextInt(list.size());
		T ret = list.get(retIdx);

		int lastIdx = list.size() - 1;
		if(retIdx < lastIdx)
		{
			// if it isn't the last element
			// then we move the last element to it's position
			// then we can simply remove the last element
			T last = list.get(lastIdx);
			list.set(retIdx, last);

			// update this value in the map
			map.put(last, retIdx);
		}

		// remove ret from the map
		map.remove(ret);
		// remove the last element from the map
		list.remove(lastIdx);

		return ret;
	}

	public int size()
	{
		return list.size();
	}
}
