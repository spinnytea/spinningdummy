package spinnytea.programmagic.maze.algorithms;

import spinnytea.programmagic.maze.Cell2D;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * This is very similar to a {@link DepthFirstMaze}, but instead of keeping a stack for the "depth first", just picks a random wall from the
 * list of available walls.
 */
public class PrimMaze
implements MazeAlgorithm
{
	private static final Logger logger = LoggerFactory.getLogger(DepthFirstMaze.class);
	private static final Random random = new Random();

	private final int startX;
	private final int startY;
	private final int width;
	private final int height;

	public PrimMaze(Integer width, Integer height)
	{
		this(width, height, 0, 0);
	}

	public PrimMaze(int width, int height, int startX, int startY)
	{
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;
		if(width < 1)
			throw new IllegalArgumentException("The width of the maze must be at least 1.");
		if(height < 1)
			throw new IllegalArgumentException("The height of the maze must be at least 1.");
		if(startX >= width)
			throw new IllegalArgumentException("The starting x position must be in the maze.");
		if(startY >= height)
			throw new IllegalArgumentException("The starting y position must be in the maze.");
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

		// a list of available walls to pick from
		// Tuple<Current Room, Next Room, Direction from current to next>
		ArrayList<MazeAlgorithmFrontier> walls = new ArrayList<MazeAlgorithmFrontier>();

		// add the first edges to the list
		// randomize order
		if(startX > 0)
			walls.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY][startX - 1], Cell2D.Direction.WEST));
		if(startX < width - 1)
			walls.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY][startX + 1], Cell2D.Direction.EAST));
		if(startY > 0)
			walls.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY - 1][startX], Cell2D.Direction.NORTH));
		if(startY < height - 1)
			walls.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY + 1][startX], Cell2D.Direction.SOUTH));

		while(!walls.isEmpty())
		{
			// pick a random wall
			MazeAlgorithmFrontier wall = walls.remove(random.nextInt(walls.size()));

			// if the next wall isn't in the maze,
			// - then add it to the maze
			// - and add it's neighbors
			if(!wall.getTo().inTheMaze())
			{
				Cell2D nextRoom = wall.getTo();
				wall.getFrom().setRoom(wall.getDirection(), nextRoom);

				// add the walls to the list
				if(nextRoom.x > 0)
					walls.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y][nextRoom.x - 1], Cell2D.Direction.WEST));
				if(nextRoom.x < width - 1)
					walls.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y][nextRoom.x + 1], Cell2D.Direction.EAST));
				if(nextRoom.y > 0)
					walls.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y - 1][nextRoom.x], Cell2D.Direction.NORTH));
				if(nextRoom.y < height - 1)
					walls.add(new MazeAlgorithmFrontier(maze[nextRoom.y][nextRoom.x], maze[nextRoom.y + 1][nextRoom.x], Cell2D.Direction.SOUTH));
			}
		}

		logger.debug("Finished " + this + " in " + (System.currentTimeMillis() - start) / 1000.0 + " seconds");
		return maze;
	}

	@Override
	public String toString()
	{
		return "PrimMaze [width=" + width + ", height=" + height + "]";
	}
}
