package spinnytea.programmagic.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * <dl>
 * <dt>Inspited by XKCD</dt>
 * <dd><a href="http://xkcd.com/1103/">Nine</a>: Ever since I heard the simile "as neglected as the nine button on the microwave," I've found myself adjusting
 * cook times.</dd>
 * <dd>FYI: If you get curious and start trying to calculate the time adjustment function that minimizes the gap between the most-used and least-used digit (for
 * a representative sample of common cook times) without altering any time by more than 10%, and someone asks you what you're doing, it's easier to just lie.</dd>
 * </dl>
 * Implemented as a genetic algorithm
 */
public class TimeAdjustment
{
	private static final Logger logger = Logger.getLogger(TimeAdjustment.class);
	private static final Random rand = new Random();
	private static final Pattern TIME_AND_FREQUENCY = Pattern.compile("(\\d+):(\\d\\d), (\\d+)");
	private static final ArrayList<String> TIME_SAMPLE = new ArrayList<String>(Arrays.asList("0:15, 10", "0:30, 20", "0:45, 5", "1:00, 30", "1:30, 10", "2:00, 30", "4:30, 10", "10:00, 1"));

	TA_Sample best;

	public TimeAdjustment()
	{
		best = new TA_Sample();

		// load the initial data
		for(String s : TIME_SAMPLE)
		{
			Matcher m = TIME_AND_FREQUENCY.matcher(s);
			try
			{
				if(m.find())
				{
					int minutes = Integer.parseInt(m.group(1));
					int seconds = Integer.parseInt(m.group(2));
					if(seconds > 100)
						throw new Exception("Minutes must be less than 100 (got: " + seconds + ")");
					best.min_sec_freq_origSecs.add(new int[] { minutes, seconds, Integer.parseInt(m.group(3)), minutes * 60 + seconds });
				}
				else
					throw new Exception("No matching parser");
			}
			catch(Exception e)
			{
				logger.error("Did not match pattern: " + s, e);
			}
		}

		best.calculateVariance();
	}

	public void train(int count)
	{
		TreeSet<TA_Sample> population = new TreeSet<TA_Sample>();
		population.add(best);

		for(int i = count; i > 0; i--)
		{
			population = epoch(0.08, i / (double) count, 100, 100, population);
		}

		// the top of the list is the best
		best = population.pollFirst();
	}

	/**
	 * @param deviationFromOriginal the new time must be approx. +/- this % of the original
	 * @param mutationRate          what is the probability of picking a new time for the given min_sec_freq_origSecs position
	 * @param branchSize            how many children do we create from each parent
	 * @param maxChildren           only keep the best children to return; this is the max count
	 * @param parents               generate new children from each of these
	 * @return a list of the best examples from this epoch
	 */
	private TreeSet<TA_Sample> epoch(double deviationFromOriginal, double mutationRate, int branchSize, int maxChildren, TreeSet<TA_Sample> parents)
	{
		TreeSet<TA_Sample> ret = new TreeSet<TA_Sample>();
		ret.addAll(parents); // saving parents will help us maintain our current progress

		// branch 10 times off the initial
		// save the best
		for(TA_Sample initial : parents)
			for(int i = 0; i < branchSize; i++)
			{
				TA_Sample next = new TA_Sample();
				// add the times
				for(int[] msfo : initial.min_sec_freq_origSecs)
				{
					// TODO keep a stat to allow time to be moved from minutes to seconds (between 0 and 40)
					int time = msfo[3];
					if(rand.nextDouble() < mutationRate)
					{
						// a random number that is approx. +/- 10% of msfo[3]
						int range = (int) (msfo[3] * deviationFromOriginal);
						time = msfo[3] - range + rand.nextInt(range * 2 + 1);
					}
					next.min_sec_freq_origSecs.add(new int[] { time / 60, time % 60, msfo[2], msfo[3] });
				}
				// we are finished
				next.calculateVariance();

				ret.add(next);
				if(ret.size() > maxChildren)
					ret.pollLast();
			}

		return ret;
	}

	/** This is a sample point that we can branch from. Pick the */
	private static final class TA_Sample
	implements Comparable<TA_Sample>
	{

		/**
		 * <dl>
		 * <dd>int[0] = current minutes (adjusted during algorithm)</dd>
		 * <dd>int[1] = current seconds (adjusted during algorithm)</dd>
		 * <dd>int[2] = frequency of used time</dd>
		 * <dd>int[3] = original absolute seconds (minutes * seconds), used to select new times</dd>
		 * </dl>
		 */
		private ArrayList<int[]> min_sec_freq_origSecs = new ArrayList<int[]>();
		private double variance;

		public int[] countNumbers()
		{
			int[] numberCount = new int[10];
			for(int[] msf : min_sec_freq_origSecs)
			{
				// count minutes
				// if the value is 0, you don't need to push anything
				// if it is greater than zero, you need to push every number
				int num = msf[0];
				while(num > 0)
				{
					numberCount[num % 10] += msf[2];
					num /= 10;
				}

				// count seconds
				// you always need to press the tens and the ones
				num = msf[1];
				numberCount[num % 10] += msf[2];
				num /= 10;
				numberCount[num % 10] += msf[2];
			}
			return numberCount;
		}

		public void calculateVariance()
		{
			int[] numberCount = countNumbers();

			double sum = 0;
			for(int i : numberCount)
				sum += i;

			variance = 0;
			for(int i = 0; i < 10; i++)
				variance += Math.pow(((numberCount[i] / sum) - 0.1), 2);
			variance /= 10;
			variance = Math.sqrt(variance);
		}

		@Override
		public int compareTo(TA_Sample tas)
		{
			return new Double(variance).compareTo(tas.variance);
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder(Arrays.toString(countNumbers()));
			sb.append(": ").append(variance);
			for(int[] msfo : min_sec_freq_origSecs)
				sb.append("\n\t").append(msfo[3]).append(" -> ").append(msfo[0]).append(':').append(msfo[1] / 10).append(msfo[1] % 10);
			return sb.toString();
		}
	}

	public static void main(String[] args)
	{
		TimeAdjustment ta = new TimeAdjustment();
		System.out.println(ta.best); // _FIXME remove
		ta.train(200);
		System.out.println(ta.best); // _FIXME remove
	}
}
