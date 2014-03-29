package spinnytea.programmagic.maze.algorithms;

import spinnytea.programmagic.maze.Cell2D;

import java.util.LinkedList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * Begin with the maze's space with no walls. Call this a chamber. Divide the chamber with a randomly positioned wall (or multiple walls) where each wall
 * contains a randomly positioned passage opening within it. Then recursively repeat the process on the subchambers until all chambers are minimum sized. This
 * method results in mazes with long straight walls crossing their space, making it easier to see which areas to avoid.
 * <p/>
 * For example, in a rectangular maze, build at random points two walls that are perpendicular to each other. These two walls divide the large chamber into four
 * smaller chambers separated by four walls. Choose three of the four walls at random, and open a one cell-wide hole at a random point in each of the three.
 * Continue in this manner recursively, until every chamber has a width of one cell in either of the two directions.
 */
public class RecursiveDivisionMaze
implements MazeAlgorithm
{
	private static final Logger logger = LoggerFactory.getLogger(RecursiveDivisionMaze.class);
	private static final Random random = new Random();

	private final int width;
	private final int height;
	private final int minimumRoomSize;
	private final Class<? extends MazeAlgorithm> smallRoomClass;

	public RecursiveDivisionMaze(Integer width, Integer height)
	{
		this(width, height, 1, null);
	}

	public RecursiveDivisionMaze(int width, int height, int minimumRoomSize, Class<? extends MazeAlgorithm> smallRoomClass)
	{
		this.width = width;
		this.height = height;
		this.minimumRoomSize = minimumRoomSize;
		this.smallRoomClass = smallRoomClass;
		if(width < 1)
			throw new IllegalArgumentException("The width of the maze must be at least 1.");
		if(height < 1)
			throw new IllegalArgumentException("The height of the maze must be at least 1.");
		if(minimumRoomSize < 1)
			throw new IllegalArgumentException("The minimumRoomSize of the maze must be at least 1.");
		try
		{
			if(smallRoomClass != null)
				smallRoomClass.getConstructor(Integer.class, Integer.class).newInstance(minimumRoomSize, minimumRoomSize);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Cannot use this class to create rooms.", e);
		}
	}

	@Override
	@SuppressWarnings("FeatureEnvy")
	public Cell2D[][] generateMaze(long seed)
	{
		long start = System.currentTimeMillis();
		logger.debug("Generating " + this);
		random.setSeed(seed);

		// initialize the maze
		Cell2D[][] maze = new Cell2D[height][width];
		for(int y = 0; y < maze.length; y++)
			for(int x = 0; x < maze[0].length; x++)
			{
				maze[y][x] = new Cell2D(y, x);
				if(x > 0)
					maze[y][x].setRoom(Cell2D.Direction.WEST, maze[y][x - 1]);
				if(y > 0)
					maze[y][x].setRoom(Cell2D.Direction.NORTH, maze[y - 1][x]);
			}

		// a list of all the available chambers to subdivide
		LinkedList<Chamber> chambers = new LinkedList<Chamber>();
		chambers.add(new Chamber(0, width - 1, 0, height - 1));

		while(!chambers.isEmpty())
		{
			Chamber c = chambers.pop();

			// when a room gets too small but bigger than one, use a different algorithm to fill it :)
			if(c.max_x - c.min_x < minimumRoomSize || c.max_y - c.min_y < minimumRoomSize)
			{
				if((c.max_x - c.min_x > 1 || c.max_y - c.min_y > 1) && smallRoomClass != null)
					try
					{
						// create a maze to fill the space
						Cell2D[][] submaze = smallRoomClass.getConstructor(Integer.class, Integer.class).newInstance(c.max_x - c.min_x + 1, c.max_y - c.min_y + 1).generateMaze(random.nextLong());

						// copy x walls into map
						for(int y = 0; y < submaze.length; y++)
							for(int x = 0; x < submaze[0].length - 1; x++)
							{
								if(submaze[y][x].east == null)
									maze[y + c.min_y][x + c.min_x].removeRoom(Cell2D.Direction.EAST);
							}
						// copy y walls into map
						for(int y = 0; y < submaze.length - 1; y++)
							for(int x = 0; x < submaze[0].length; x++)
							{
								if(submaze[y][x].south == null)
									maze[y + c.min_y][x + c.min_x].removeRoom(Cell2D.Direction.SOUTH);
							}
					}
					catch(Exception e)
					{
						throw new RuntimeException("This exception should have already been handled.", e);
					}

				continue;
			}

			// TODO add a heuristic to reduce the split range when the width/height is greater than 4

			// if x
			if(random.nextBoolean())
			{
				// find the wall to split the chamber
				int xsplit = random.nextInt(c.max_x - c.min_x) + c.min_x;
				// keep a door open between chambers
				int ydoor = random.nextInt(c.max_y - c.min_y + 1) + c.min_y;

				// add a wall to the map (except one door)
				for(int y = c.min_y; y <= c.max_y; y++)
				{
					if(y == ydoor)
						continue;
					maze[y][xsplit].removeRoom(Cell2D.Direction.EAST);
				}

				// add the two new chambers to the list
				chambers.add(new Chamber(c.min_x, xsplit, c.min_y, c.max_y));
				chambers.add(new Chamber(xsplit + 1, c.max_x, c.min_y, c.max_y));
			}
			// otherwise y
			else
			{
				// find the wall to split the chamber
				int ysplit = random.nextInt(c.max_y - c.min_y) + c.min_y;
				// keep a door open between chambers
				int xdoor = random.nextInt(c.max_x - c.min_x + 1) + c.min_x;

				// add a wall to the map (except one door)
				for(int x = c.min_x; x <= c.max_x; x++)
				{
					if(x == xdoor)
						continue;
					maze[ysplit][x].removeRoom(Cell2D.Direction.SOUTH);
				}

				// add the two new chambers to the list
				chambers.add(new Chamber(c.min_x, c.max_x, c.min_y, ysplit));
				chambers.add(new Chamber(c.min_x, c.max_x, ysplit + 1, c.max_y));
			}
		}

		//noinspection MagicNumber
		logger.debug("Finished " + this + " in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds");
		return maze;
	}

	private static final class Chamber
	{
		final int min_x;
		final int max_x;
		final int min_y;
		final int max_y;

		public Chamber(int min_x, int max_x, int min_y, int max_y)
		{
			this.min_x = min_x;
			this.max_x = max_x;
			this.min_y = min_y;
			this.max_y = max_y;
		}
	}

	@Override
	public String toString()
	{
		return "RecursiveDivisionMaze [width=" + width + ", height=" + height + "]";
	}
}
