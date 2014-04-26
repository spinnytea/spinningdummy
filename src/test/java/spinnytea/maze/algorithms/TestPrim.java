package spinnytea.maze.algorithms;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static spinnytea.maze.Cell2D.Direction.EAST;
import static spinnytea.maze.Cell2D.Direction.NORTH;
import static spinnytea.maze.Cell2D.Direction.SOUTH;
import static spinnytea.maze.Cell2D.Direction.WEST;
import static spinnytea.maze.TestAlgorithms.createCells;
import static spinnytea.maze.TestAlgorithms.setRow;

import spinnytea.maze.Cell2D;

import org.junit.Test;

public class TestPrim
{
	@Test
	public void simple()
	{
		Cell2D[][] cells = new PrimMaze(2, 2).generateMaze(1L);
		Cell2D[][] expected = createCells(2, 2);
		setRow(expected, 0, SOUTH, WEST);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(expected, cells);

		cells = new PrimMaze(2, 2).generateMaze(2L);
		expected = createCells(2, 2);
		setRow(expected, 0, EAST, SOUTH);
		setRow(expected, 1, NORTH, null);
		assertArrayEquals(expected, cells);
		try
		{
			new PrimMaze(2, 2, -1, -1);
			assertTrue(false);
		}
		catch(Exception e)
		{
			assertTrue(true);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void startLarge()
	{
		new PrimMaze(2, 2, 3, 3);
		assertTrue(false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void startSmall()
	{
		new PrimMaze(2, 2, -1, -1);
		assertTrue(false);
	}

	@Test
	public void complex()
	{
		Cell2D[][] cells = new PrimMaze(6, 6).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, SOUTH, WEST, WEST, WEST, WEST, WEST);
		setRow(expected, 1, EAST, EAST, EAST, SOUTH, WEST, WEST);
		setRow(expected, 2, NORTH, EAST, NORTH, SOUTH, WEST, WEST);
		setRow(expected, 3, NORTH, EAST, EAST, SOUTH, NORTH, WEST);
		setRow(expected, 4, EAST, NORTH, WEST, EAST, SOUTH, WEST);
		setRow(expected, 5, NORTH, NORTH, EAST, NORTH, EAST, null);

		assertArrayEquals(expected, cells);
	}

	@Test
	public void complex2()
	{
		Cell2D[][] cells = new PrimMaze(6, 6, 3, 3).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, SOUTH, WEST, SOUTH, SOUTH, SOUTH, SOUTH);
		setRow(expected, 1, EAST, EAST, EAST, EAST, EAST, SOUTH);
		setRow(expected, 2, EAST, SOUTH, WEST, SOUTH, WEST, WEST);
		setRow(expected, 3, EAST, EAST, EAST, null, WEST, NORTH);
		setRow(expected, 4, EAST, NORTH, NORTH, NORTH, NORTH, WEST);
		setRow(expected, 5, EAST, NORTH, NORTH, WEST, NORTH, WEST);

		assertArrayEquals(expected, cells);
	}

	@Test
	@SuppressWarnings({ "MagicNumber", "ForLoopReplaceableByForEach" })
	public void limited()
	{
		int count = 50;
		Cell2D[][] cells = new PrimMaze(10, 10, 5, 5).generateMaze(1L, count);
		for(int y = 0; y < cells.length; y++)
			for(int x = 0; x < cells[0].length; x++)
				if(cells[y][x].inTheMaze())
					count--;
		assertEquals(0, count);

		count = 20;
		cells = new PrimMaze(10, 10, 1, 1).generateMaze(1L, count);
		for(int y = 0; y < cells.length; y++)
			for(int x = 0; x < cells[0].length; x++)
				if(cells[y][x].inTheMaze())
					count--;
		assertEquals(0, count);
	}
}
