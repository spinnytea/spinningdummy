package spinnytea.programmagic.maze.callforhelp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class TestDisjointSets
{
	/** This test was included with the file. I'm not sure why this works; I'll come document it later. */
	@Test
	public void doIt()
	{
		int NumElements = 12;
		int NumInSameSet = 4;

		DisjointSets s = new DisjointSets(NumElements);
		int set1, set2;

		for(int k = 1; k < NumInSameSet; k *= 2)
		{
			for(int j = 0; j + k < NumElements; j += 2 * k)
			{
				set1 = s.find(j);
				set2 = s.find(j + k);
				s.union(set1, set2);
			}
		}

		// All the find()s on the same output line should be identical.
		ArrayList<Integer> finds = new ArrayList<Integer>();
		for(int i = 0; i < NumElements; i++)
		{
//			System.out.print(s.find(i) + "*");
			finds.add(s.find(i));
			if(i % NumInSameSet == NumInSameSet - 1)
			{
//				System.out.println();
				if(!finds.isEmpty())
				{
					int first = finds.get(0);
					for(int f : finds)
						assertEquals(first, f);
				}
				finds.clear();
			}
		}
	}
}
