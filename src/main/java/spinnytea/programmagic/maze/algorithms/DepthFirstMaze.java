package spinnytea.programmagic.maze.algorithms;

import spinnytea.programmagic.maze.Cell2D;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * "In mazes generated depth-first-search that algorithm, it will typically be relatively easy to find the way to the square that was first picked at the beginning of the algorithm, since most paths lead to or from there, but it is hard to find the way out."
 * - wikipedia <br>
 * <p/>
 * The algorithm is very simple and does not produce over-complex mazes. More specific refinements to the algorithm can help to generate
 * mazes that are harder to solve.
 * <ol>
 * <li>Start at a particular cell and call it the "exit."</li>
 * <li>Mark the current cell as visited, and get a list of its neighbors. For each neighbor, starting with a randomly selected neighbor:</li>
 * <ol>
 * <li>If that neighbor hasn't been visited, remove the wall between this cell and that neighbor, and then recurse with that neighbor as the
 * current cell.</li>
 * </ol>
 * </ol>
 * <p/>
 * <b>TODO</b> See about generating the map from more than one point ~ how do you combine the different maps?<br>
 */
@ToString(of = { "width", "height" })
public class DepthFirstMaze
implements MazeAlgorithm
{
	private static final Logger logger = LoggerFactory.getLogger(DepthFirstMaze.class);
	private static final Random random = new Random();

	private final int width;
	private final int height;
	private final int startX;
	private final int startY;

	public DepthFirstMaze(Integer width, Integer height)
	{
		this(width, height, 0, 0);
	}

	public DepthFirstMaze(int width, int height, int startX, int startY)
	{
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;
		if(width < 1)
			throw new IllegalArgumentException("The width of the maze must be at least 1.");
		if(height < 1)
			throw new IllegalArgumentException("The height of the maze must be at least 1.");
		if(startX >= width || startX < 0)
			throw new IllegalArgumentException("The starting x position must be in the maze.");
		if(startY >= height || startY < 0)
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

		// a stack structure to hold which walls do add to traverse next
		Deque<MazeAlgorithmFrontier> walls = new LinkedList<MazeAlgorithmFrontier>();
		// used for randomizing the order of walls
		ArrayList<MazeAlgorithmFrontier> temp = new ArrayList<MazeAlgorithmFrontier>();

		// add the first edges to the stack
		// randomize order
		if(startX > 0)
			temp.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY][startX - 1], Cell2D.Direction.WEST));
		if(startX < width - 1)
			temp.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY][startX + 1], Cell2D.Direction.EAST));
		if(startY > 0)
			temp.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY - 1][startX], Cell2D.Direction.NORTH));
		if(startY < height - 1)
			temp.add(new MazeAlgorithmFrontier(maze[startY][startX], maze[startY + 1][startX], Cell2D.Direction.SOUTH));
		randomPush(walls, temp);

		while(!walls.isEmpty())
		{
			MazeAlgorithmFrontier wall = walls.pop();

			// if the next wall isn't in the maze,
			// - then add it to the maze
			// - and add it's neighbors
			if(!wall.getTo().inTheMaze())
			{
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
