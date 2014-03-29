package spinnytea.programmagic.maze.algorithms;

import spinnytea.programmagic.maze.Cell2D;
import spinnytea.programmagic.maze.callforhelp.DisjointSets;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * <ol>
 * This algorithm is a randomized version of Kruskal's algorithm.
 * <li>Create a list of all walls, and create a set for each cell, each containing just that one cell.</li>
 * <li>For each wall, in some random order:</li>
 * <ol>
 * <li>If the cells divided by this wall belong to distinct sets:</li>
 * <ol>
 * <li>Remove the current wall.</li>
 * <li>Join the sets of the formerly divided cells.</li>
 * </ol>
 * </ol> </ol>
 */
public class KruskalMaze
implements MazeAlgorithm
{
	private static final Logger logger = LoggerFactory.getLogger(KruskalMaze.class);
	private static final Random random = new Random();

	private final int width;
	private final int height;

	public KruskalMaze(Integer width, Integer height)
	{
		this.width = width;
		this.height = height;
		if(width < 1)
			throw new IllegalArgumentException("The width of the maze must be at least 1.");
		if(height < 1)
			throw new IllegalArgumentException("The height of the maze must be at least 1.");
	}

	@Override
	public Cell2D[][] generateMaze(long seed)
	{
		long start = System.currentTimeMillis();
		logger.debug("Generating " + this);
		random.setSeed(seed);

		// initialize the maze
		Cell2D[][] maze = new Cell2D[height][width];
		for(int y = 0; y < maze.length; y++)
			for(int x = 0; x < maze[0].length; x++)
				maze[y][x] = new Cell2D(y, x);

		// each cell has it's own set to begin with
		// use y*width + x to get the sets.int (the position in sets)
		DisjointSets sets = new DisjointSets(height * width);

		// a list of all the available walls
		// contains the two rooms, and a direction for ease
		// TODO is there a better structure for removing a random item? ~ is there a method, better than n^2 of putting a list of items in random order
		ArrayList<Tuple3<Cell2D, Cell2D, Cell2D.Direction>> walls = new ArrayList<Tuple3<Cell2D, Cell2D, Cell2D.Direction>>();
		// add all the x-walls
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width - 1; x++)
				walls.add(new Tuple3<Cell2D, Cell2D, Cell2D.Direction>(maze[y][x], maze[y][x + 1], Cell2D.Direction.EAST));
		// add all the y-walls
		for(int y = 0; y < height - 1; y++)
			for(int x = 0; x < width; x++)
				walls.add(new Tuple3<Cell2D, Cell2D, Cell2D.Direction>(maze[y][x], maze[y + 1][x], Cell2D.Direction.SOUTH));

		// pick random walls from the list, knock them down if they belong to two different rooms
		while(!walls.isEmpty())
		{
			Tuple3<Cell2D, Cell2D, Cell2D.Direction> wall = walls.remove(random.nextInt(walls.size()));

			int set1 = sets.find(wall._1().y * width + wall._1().x);
			int set2 = sets.find(wall._2().y * width + wall._2().x);

			// if they belong to different sets
			// then add the wall to the maze
			if(set1 != set2)
			{
				wall._1().setRoom(wall._3(), wall._2());
				sets.union(set1, set2);
			}
		}

		logger.debug("Finished " + this + " in " + (System.currentTimeMillis() - start) / 1000.0 + " seconds");
		return maze;
	}

	@Override
	public String toString()
	{
		return "KruskalMaze [width=" + width + ", height=" + height + "]";
	}
}
