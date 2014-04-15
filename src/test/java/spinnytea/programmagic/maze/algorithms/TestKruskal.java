package spinnytea.programmagic.maze.algorithms;

import static org.junit.Assert.assertArrayEquals;
import static spinnytea.programmagic.maze.Cell2D.Direction.EAST;
import static spinnytea.programmagic.maze.Cell2D.Direction.NORTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.SOUTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.WEST;
import static spinnytea.programmagic.maze.TestAlgorithms.createCells;
import static spinnytea.programmagic.maze.TestAlgorithms.setRow;

import spinnytea.programmagic.maze.Cell2D;

import org.junit.Test;

public class TestKruskal
{
	@Test
	public void simple()
	{
		Cell2D[][] cells = new KruskalMaze(2, 2).generateMaze(1L);
		Cell2D[][] expected = createCells(2, 2);
		setRow(expected, 0, EAST, SOUTH);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(expected, cells);

		// this is the first seed that produces a different maze
		// this is a super simple board, after-all
		//noinspection MagicNumber
		cells = new KruskalMaze(2, 2).generateMaze(256L);
		expected = createCells(2, 2);
		setRow(expected, 0, SOUTH, WEST);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(expected, cells);
	}

	@Test
	public void complex()
	{
		Cell2D[][] cells = new KruskalMaze(6, 6).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, SOUTH, EAST, EAST, SOUTH, EAST, SOUTH);
		setRow(expected, 1, EAST, NORTH, SOUTH, EAST, SOUTH, SOUTH);
		setRow(expected, 2, EAST, EAST, EAST, NORTH, SOUTH, SOUTH);
		setRow(expected, 3, EAST, EAST, NORTH, EAST, SOUTH, SOUTH);
		setRow(expected, 4, NORTH, SOUTH, SOUTH, EAST, SOUTH, SOUTH);
		setRow(expected, 5, EAST, EAST, EAST, NORTH, EAST, null);

		assertArrayEquals(expected, cells);
	}
}
