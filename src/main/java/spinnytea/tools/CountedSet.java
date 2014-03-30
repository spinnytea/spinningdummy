package spinnytea.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class is a thinly disguised HashMap/HashSet.<br>
 * It acts like a HashSet, in that it stores one and only one of an item (give that an Object implements hashCode and equals methods properly).
 * It creates a HashMap, and "counts" the number of times an item is added to the list.
 * <p/>
 * <b>Last Update:</b> Mar 20, 2012
 */
@SuppressWarnings("UnusedDeclaration")
public class CountedSet<T>
implements Set<T>
{
	private final Map<T, Integer> map;
	private long total;
	private long maxCount;

	public CountedSet()
	{
		map = new HashMap<T, Integer>();
		total = 0;
		maxCount = 0;
	}

	public CountedSet(Collection<? extends T> aList)
	{
		this();
		addAll(aList);
	}

	/** add an item to the set; this adds it with a count of 1 */
	@Override
	public boolean add(T t)
	{
		return this.add(t, 1);
	}

	/** add the item to the list, increasing the counter by <code>num</code> */
	public boolean add(T t, int num)
	{
		if(t != null)
		{
			total += num;
			Integer n = map.get(t);
			if(n != null)
			{
				num += n;
				map.remove(t);
			}
			map.put(t, num);
			if(maxCount >= 0 && maxCount < num)
				maxCount = num;
			return true;
		}
		return false;
	}

	/** add all items from c into this object; uses the counts in c */
	public boolean addAll(CountedSet<T> c)
	{
		for(T t : c)
			this.add(t, c.getCountFor(t));
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		for(T t : c)
			this.add(t);
		return true;
	}

	/** Get the current count of the object. If the object doesn't exists, then the count is 0 */
	public int getCountFor(T t)
	{
		Integer count = map.get(t);
		if(count == null)
			return 0;
		return count;
	}

	/** Get the count for each of the objects, and return the total */
	public int getCountsFor(Iterable<? extends T> sublist)
	{
		int total = 0;
		for(T t : sublist)
			total += getCountFor(t);
		return total;
	}

	/**
	 * Get the total of all the objects in the list
	 * <p/>
	 * if A was added 2 times, and B was added 3 times, then getTotal will return 5.
	 */
	public long getTotal()
	{
		return total;
	}

	/**
	 * Get the maximum count in the list
	 * <p/>
	 * if A was added 2 times, and B was added 3 times, then getTotal will return 3.
	 * <p/>
	 * Note: This may need to be recalculated if the object with the highest count has been removed
	 */
	public long getMaxCount()
	{
		if(maxCount < 0) // if it gets reset
			for(Integer i : map.values())
				if(i > maxCount)
					maxCount = i;
		return maxCount;
	}

	@Override
	public void clear()
	{
		map.clear();
		total = 0;
		maxCount = 0;
	}

	@Override
	public boolean contains(Object o)
	{
		// if o is the right type
		// and if map contains o
		//noinspection SuspiciousMethodCalls
		return !(o == null || getClass() != o.getClass()) && map.containsKey(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return map.keySet().containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return map.keySet().iterator();
	}

	@Override
	public boolean remove(Object o)
	{
		//noinspection SuspiciousMethodCalls
		Integer i = map.get(o);
		if(i != null)
			total -= i;
		if(i != null && i == maxCount)
			maxCount = -1;
		return map.remove(o) != null;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		for(Object o : c)
			remove(o);
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public int size()
	{
		return map.size();
	}

	@Override
	public Object[] toArray()
	{
		return map.keySet().toArray();
	}

	@Override
	public <E> E[] toArray(E[] a)
	{
		// while this is suspicious, it is required as is
		//noinspection SuspiciousToArrayCall
		return map.keySet().toArray(a);
	}

	public ArrayList<T> toArrayList()
	{
		return new ArrayList<T>(map.keySet());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("[ ");

		for(T key : map.keySet())
			sb.append("(").append(key).append(" : ").append(getCountFor(key)).append(") ");

		sb.append("]");
		return sb.toString();
	}

	/** @param ascending if true, the first value will be low, if false, the first value will be high */
	@SuppressWarnings("BooleanParameter") // this is a special case
	public CountedComparator getCountedComparator(boolean ascending)
	{
		return new CountedComparator(ascending);
	}

	/**
	 * Specifically, this is used for sorting the data from the "toArrayList" of a counted set<br>
	 * It is a common case to want the items sorted by count - this is a canned comparator that does that.
	 */
	public class CountedComparator
	implements Comparator<T>
	{
		private final boolean ascending;

		/** @param ascending if true, the first value will be low, if false, the first value will be high */
		private CountedComparator(boolean ascending)
		{
			this.ascending = ascending;
		}

		@Override
		public int compare(T o1, T o2)
		{
			if(ascending)
				return getCountFor(o1) - getCountFor(o2);
			return getCountFor(o2) - getCountFor(o1);
		}
	}

	/** If you want a Serializable CountedSet, then the objects it contain must also be Serializable */
	public static class SerializableCountedSet<T extends Serializable>
	extends CountedSet<T>
	implements Serializable
	{
		private static final long serialVersionUID = -9034844083494946717L;

		public SerializableCountedSet()
		{
			super();
		}

		public SerializableCountedSet(Collection<? extends T> aList)
		{
			super(aList);
		}
	}
}
