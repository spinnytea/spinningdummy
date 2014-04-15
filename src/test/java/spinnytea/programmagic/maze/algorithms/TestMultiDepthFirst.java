package spinnytea.programmagic.maze.algorithms;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TestMultiDepthFirst
{
	@Test
	@SuppressWarnings("MagicNumber")
	public void compare()
	{
		// the base case of the multi should produce the same result
		assertArrayEquals(new DepthFirstMaze(6, 6).generateMaze(1L), new MultiDepthFirstMaze(6, 6).generateMaze(1L));
		assertArrayEquals(new DepthFirstMaze(60, 60).generateMaze(10L), new MultiDepthFirstMaze(60, 60).generateMaze(10L));
		assertArrayEquals(new DepthFirstMaze(60, 60, 10, 10).generateMaze(10L), new MultiDepthFirstMaze(60, 60, new int[][] { { 10, 10 } }).generateMaze(10L));
		assertArrayEquals(new DepthFirstMaze(100, 160, 10, 10).generateMaze(10L), new MultiDepthFirstMaze(100, 160, new int[][] { { 10, 10 } }).generateMaze(10L));
	}

	@Test
	public void complex()
	{

	}
}
