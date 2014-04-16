package spinnytea.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RandomAccessCollection<T>
{
	private final Random random = new Random();

	// list needs to be random access; linked list won't work
	@SuppressWarnings({ "CollectionDeclaredAsConcreteClass", "TypeMayBeWeakened" })
	private final ArrayList<T> list = new ArrayList<T>();

	public void setSeed(long seed)
	{
		random.setSeed(seed);
	}

	/** add an element to the collection */
	public void add(T t)
	{
		list.add(t);
	}

	public void addAll(Collection<T> ts)
	{
		list.addAll(ts);
	}

	/** remove a random element from the list */
	public T remove()
	{
		if(list.isEmpty())
			return null;

		int retIdx = random.nextInt(list.size());
		int lastIdx = list.size() - 1;

		T ret = list.get(retIdx);

		if(retIdx < lastIdx)
		{
			// if it isn't the last element
			// then we move the last element to it's position
			// then we can simply remove the last element
			T last = list.get(lastIdx);
			list.set(retIdx, last);
		}

		// remove the last element from the map
		list.remove(lastIdx);

		return ret;
	}

	public int size()
	{
		return list.size();
	}
}
