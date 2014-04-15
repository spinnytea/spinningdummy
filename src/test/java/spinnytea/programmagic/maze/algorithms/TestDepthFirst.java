package spinnytea.programmagic.maze.algorithms;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static spinnytea.programmagic.maze.Cell2D.Direction.EAST;
import static spinnytea.programmagic.maze.Cell2D.Direction.NORTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.SOUTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.WEST;
import static spinnytea.programmagic.maze.TestAlgorithms.createCells;
import static spinnytea.programmagic.maze.TestAlgorithms.setRow;

import spinnytea.programmagic.maze.Cell2D;

import org.junit.Test;

public class TestDepthFirst
{
	@Test
	public void simple()
	{
		Cell2D[][] cells = new DepthFirstMaze(2, 2).generateMaze(1L);
		Cell2D[][] expected = createCells(2, 2);
		setRow(expected, 0, EAST, SOUTH);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(expected, cells);

		// this is the first seed that produces a different maze
		// this is a super simple board, after-all
		//noinspection MagicNumber
		cells = new DepthFirstMaze(2, 2).generateMaze(4096L);
		expected = createCells(2, 2);
		setRow(expected, 0, SOUTH, SOUTH);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(expected, cells);
	}

	@Test(expected = IllegalArgumentException.class)
	public void startBig()
	{
		new DepthFirstMaze(2, 2, 3, 3);
		assertTrue(false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void startSmall()
	{
		new DepthFirstMaze(2, 2, -1, -1);
		assertTrue(false);
	}

	@Test
	public void complex()
	{
		Cell2D[][] cells = new DepthFirstMaze(6, 6).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, EAST, SOUTH, EAST, EAST, EAST, SOUTH);
		setRow(expected, 1, SOUTH, EAST, SOUTH, EAST, EAST, SOUTH);
		setRow(expected, 2, SOUTH, WEST, SOUTH, NORTH, SOUTH, SOUTH);
		setRow(expected, 3, EAST, SOUTH, EAST, NORTH, SOUTH, SOUTH);
		setRow(expected, 4, NORTH, EAST, SOUTH, EAST, SOUTH, SOUTH);
		setRow(expected, 5, NORTH, WEST, EAST, NORTH, EAST, null);

		assertArrayEquals(expected, cells);
	}

	@Test
	public void complex2()
	{

		Cell2D[][] cells = new DepthFirstMaze(6, 6, 3, 3).generateMaze(2L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, EAST, SOUTH, EAST, EAST, EAST, SOUTH);
		setRow(expected, 1, NORTH, EAST, NORTH, SOUTH, WEST, WEST);
		setRow(expected, 2, NORTH, WEST, SOUTH, WEST, SOUTH, WEST);
		setRow(expected, 3, NORTH, SOUTH, WEST, null, WEST, NORTH);
		setRow(expected, 4, NORTH, SOUTH, NORTH, WEST, EAST, NORTH);
		setRow(expected, 5, NORTH, EAST, EAST, EAST, NORTH, WEST);

		assertArrayEquals(expected, cells);
	}
}
