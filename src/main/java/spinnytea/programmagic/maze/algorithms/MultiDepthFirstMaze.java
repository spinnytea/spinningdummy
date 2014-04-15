package spinnytea.programmagic.maze.algorithms;

import spinnytea.programmagic.maze.Cell2D;
import spinnytea.programmagic.maze.callforhelp.DisjointSets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Works like {@link spinnytea.programmagic.maze.algorithms.DepthFirstMaze}, but it can have multiple starting points.
 */
@ToString(of = { "width", "height" })
public class MultiDepthFirstMaze
implements MazeAlgorithm
{
	private static final Logger logger = LoggerFactory.getLogger(MultiDepthFirstMaze.class);
	private static final Random random = new Random();

	private final int width;
	private final int height;
	private final int[][] startingPositions;

	/** @param starts starts[n][0] = x, starts[n][1] = y */
	public MultiDepthFirstMaze(int width, int height, int[]... starts)
	{
		// if starting positions aren't defined, then we should define a default
		if(starts == null || starts.length == 0)
			starts = new int[][] { { 0, 0 } };

		this.width = width;
		this.height = height;
		this.startingPositions = starts;

		if(width < 1)
			throw new IllegalArgumentException("The width of the maze must be at least 1.");
		if(height < 1)
			throw new IllegalArgumentException("The height of the maze must be at least 1.");
		for(int[] start : starts)
		{
			int startX = start[0];
			int startY = start[1];

			if(startX >= width || startX < 0)
				throw new IllegalArgumentException("The starting x position must be in the maze.");
			if(startY >= height || startY < 0)
				throw new IllegalArgumentException("The starting y position must be in the maze.");
		}
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

		// each stack is structure to hold which walls do add to traverse next
		Collection<Deque<MazeAlgorithmFrontier>> wallLists = new ArrayList<Deque<MazeAlgorithmFrontier>>();
		// used for randomizing the order of walls
		ArrayList<MazeAlgorithmFrontier> temp = new ArrayList<MazeAlgorithmFrontier>();

		for(int[] sp : startingPositions)
		{
			int x = sp[0];
			int y = sp[1];

			// add the first edges to the stack
			// randomize order
			if(x > 0)
				temp.add(new MazeAlgorithmFrontier(maze[y][x], maze[y][x - 1], Cell2D.Direction.WEST));
			if(x < width - 1)
				temp.add(new MazeAlgorithmFrontier(maze[y][x], maze[y][x + 1], Cell2D.Direction.EAST));
			if(y > 0)
				temp.add(new MazeAlgorithmFrontier(maze[y][x], maze[y - 1][x], Cell2D.Direction.NORTH));
			if(y < height - 1)
				temp.add(new MazeAlgorithmFrontier(maze[y][x], maze[y + 1][x], Cell2D.Direction.SOUTH));

			Deque<MazeAlgorithmFrontier> walls = new LinkedList<MazeAlgorithmFrontier>();
			randomPush(walls, temp);
			wallLists.add(walls);
		}

		while(!wallLists.isEmpty())
		{
			Iterator<Deque<MazeAlgorithmFrontier>> iter = wallLists.iterator();
			while(iter.hasNext())
			{
				Deque<MazeAlgorithmFrontier> walls = iter.next();
				MazeAlgorithmFrontier wall = walls.pop();

				// put these two rooms into the same set
				int set1 = sets.find(wall.getFrom().y * width + wall.getFrom().x);
				int set2 = sets.find(wall.getTo().y * width + wall.getTo().x);

				// if the two cells are not part of the same maze, then combine them
				// this works for extending a maze into new rooms,
				// as well as combining two separate mazes into one
				if(set1 != set2)
				{
					sets.union(set1, set2);

					Cell2D nextRoom = wall.getTo();
					wall.getFrom().setRoom(wall.getDirection(), nextRoom);

					// randomize order
					if(nextRoom.x > 0)
						temp.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y][nextRoom.x - 1], Cell2D.Direction.WEST));
					if(nextRoom.x < width - 1)
						temp.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y][nextRoom.x + 1], Cell2D.Direction.EAST));
					if(nextRoom.y > 0)
						temp.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y - 1][nextRoom.x], Cell2D.Direction.NORTH));
					if(nextRoom.y < height - 1)
						temp.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y + 1][nextRoom.x], Cell2D.Direction.SOUTH));
					randomPush(walls, temp);
				}

				if(walls.isEmpty())
					iter.remove();
			}
		}

		//noinspection MagicNumber
		logger.debug("Finished " + this + " in " + (System.currentTimeMillis() - start) / 1000.0 + " seconds");
		return maze;
	}

	private void randomPush(Deque<MazeAlgorithmFrontier> walls, ArrayList<MazeAlgorithmFrontier> temp)
	{
		while(!temp.isEmpty())
			walls.push(temp.remove(random.nextInt(temp.size())));
	}
}
