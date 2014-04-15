package spinnytea.programmagic.maze;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static spinnytea.programmagic.maze.Cell2D.Direction.EAST;
import static spinnytea.programmagic.maze.Cell2D.Direction.NORTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.SOUTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.WEST;

import spinnytea.programmagic.maze.algorithms.DepthFirstMaze;
import spinnytea.programmagic.maze.algorithms.KruskalMaze;
import spinnytea.programmagic.maze.algorithms.MultiDepthFirstMaze;
import spinnytea.programmagic.maze.algorithms.PrimMaze;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** these test cases have been created from a stable implementation */
public class TestAlgorithms
{
	private static final Logger logger = LoggerFactory.getLogger(TestAlgorithms.class);

	@Test
	public void depthFirst_Simple()
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
	public void depthFirst_StartBig()
	{
		new DepthFirstMaze(2, 2, 3, 3);
		assertTrue(false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void depthFirst_StartSmall()
	{
		new DepthFirstMaze(2, 2, -1, -1);
		assertTrue(false);
	}

	@Test
	public void depthFirst_Complex()
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
	public void depthFirst_Complex2()
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

	//

	@Test
	public void kruskal_Simple()
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
	public void kruskal_Complex()
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

	//

	@Test
	public void prim_Simple()
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
	public void prim_StartLarge()
	{
		new PrimMaze(2, 2, 3, 3);
		assertTrue(false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void prim_StartSmall()
	{
		new PrimMaze(2, 2, -1, -1);
		assertTrue(false);
	}

	@Test
	public void prim_Complex()
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
	public void prim_Complex2()
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
	public void prim_limited()
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

	//

	@Test
	public void depthFirst_Compare()
	{
		assertArrayEquals(new DepthFirstMaze(6, 6).generateMaze(1L), new MultiDepthFirstMaze(6, 6).generateMaze(1L));
		assertArrayEquals(new DepthFirstMaze(60, 60).generateMaze(10L), new MultiDepthFirstMaze(60, 60).generateMaze(10L));
	}

	//
	// helper methods
	//

	private Cell2D[][] createCells(int width, int height)
	{
		Cell2D[][] ret = new Cell2D[height][width];
		for(int h = 0; h < height; h++)
			for(int w = 0; w < width; w++)
				ret[h][w] = new Cell2D(h, w);
		return ret;
	}

	/** for each cell, in the row, attach it to an adjacent cell */
	private void setRow(Cell2D[][] cells, int h, Cell2D.Direction... row)
	{
		for(int w = 0; w < row.length; w++)
		{
			if(row[w] != null)
				switch(row[w])
				{
				case EAST:
					cells[h][w].setRoom(EAST, cells[h][w + 1]);
					break;
				case WEST:
					cells[h][w].setRoom(WEST, cells[h][w - 1]);
					break;
				case NORTH:
					cells[h][w].setRoom(NORTH, cells[h - 1][w]);
					break;
				case SOUTH:
					cells[h][w].setRoom(SOUTH, cells[h + 1][w]);
					break;
				}
		}
	}

	/** this is for creating new test cases, or if you want to visualize it */
	@SuppressWarnings("unused")
	private void displayCells(Cell2D[][] cells)
	{
		JFrame frame = new JFrame("Maze2D");
		frame.setContentPane(new Maze2D(cells));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		try
		{
			// sleep for a little while to allow us to look at the maze
			//noinspection MagicNumber
			Thread.sleep(10000L);
		}
		catch(InterruptedException e)
		{
			logger.error("what?", e);
		}
	}
}
