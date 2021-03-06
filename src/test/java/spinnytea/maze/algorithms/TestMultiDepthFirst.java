package spinnytea.maze.algorithms;

import static org.junit.Assert.assertArrayEquals;
import static spinnytea.maze.Cell2D.Direction.EAST;
import static spinnytea.maze.Cell2D.Direction.NORTH;
import static spinnytea.maze.Cell2D.Direction.SOUTH;
import static spinnytea.maze.Cell2D.Direction.WEST;
import static spinnytea.maze.TestAlgorithms.createCells;
import static spinnytea.maze.TestAlgorithms.setRow;

import spinnytea.maze.Cell2D;

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
		Cell2D[][] cells = new MultiDepthFirstMaze(6, 6, new int[][] { { 1, 1 }, { 4, 4 } }).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, EAST, EAST, SOUTH, SOUTH, EAST, SOUTH);
		setRow(expected, 1, NORTH, null, SOUTH, EAST, NORTH, SOUTH);
		setRow(expected, 2, EAST, NORTH, SOUTH, NORTH, SOUTH, WEST);
		setRow(expected, 3, NORTH, WEST, SOUTH, SOUTH, WEST, NORTH);
		setRow(expected, 4, EAST, NORTH, SOUTH, WEST, SOUTH, NORTH);
		setRow(expected, 5, NORTH, WEST, WEST, NORTH, WEST, NORTH);

		assertArrayEquals(expected, cells);
	}

	@Test
	public void complex2()
	{
		Cell2D[][] cells = new MultiDepthFirstMaze(6, 6, new int[][] { { 1, 1 }, { 1, 1 }, { 2, 1 }, { 3, 1 }, { 4, 1 } }).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, SOUTH, EAST, EAST, EAST, SOUTH, WEST);
		setRow(expected, 1, SOUTH, EAST, NORTH, EAST, EAST, SOUTH);
		setRow(expected, 2, EAST, NORTH, NORTH, EAST, NORTH, SOUTH);
		setRow(expected, 3, NORTH, SOUTH, NORTH, SOUTH, SOUTH, WEST);
		setRow(expected, 4, NORTH, SOUTH, NORTH, SOUTH, SOUTH, SOUTH);
		setRow(expected, 5, EAST, EAST, NORTH, EAST, EAST, null);

		assertArrayEquals(expected, cells);
	}
}
