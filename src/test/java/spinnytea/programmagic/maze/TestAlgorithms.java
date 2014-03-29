package spinnytea.programmagic.maze;

import static org.junit.Assert.assertArrayEquals;
import static spinnytea.programmagic.maze.Cell2D.Direction.EAST;
import static spinnytea.programmagic.maze.Cell2D.Direction.NORTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.SOUTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.WEST;

import spinnytea.programmagic.maze.algorithms.DepthFirstMaze;
import spinnytea.programmagic.maze.algorithms.KruskalMaze;

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
		// small board for initial test case
		Cell2D[][] cells = new DepthFirstMaze(2, 2).generateMaze(1L);
		Cell2D[][] expected = createCells(2, 2);
		setRow(expected, 0, EAST, SOUTH);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(cells, expected);

		// this is the first seed that produces a different maze
		// this is a super simple board, after-all
		//noinspection MagicNumber
		cells = new DepthFirstMaze(2, 2).generateMaze(4096L);
		expected = createCells(2, 2);
		setRow(expected, 0, SOUTH, SOUTH);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(cells, expected);
	}

	@Test
	public void depthFirst_Complex()
	{
		// small board for initial test case
		Cell2D[][] cells = new DepthFirstMaze(6, 6).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, EAST, SOUTH, EAST, EAST, EAST, SOUTH);
		setRow(expected, 1, SOUTH, EAST, SOUTH, EAST, EAST, SOUTH);
		setRow(expected, 2, SOUTH, WEST, SOUTH, NORTH, SOUTH, SOUTH);
		setRow(expected, 3, EAST, SOUTH, EAST, NORTH, SOUTH, SOUTH);
		setRow(expected, 4, NORTH, EAST, SOUTH, EAST, SOUTH, SOUTH);
		setRow(expected, 5, NORTH, WEST, EAST, NORTH, EAST, null);

		assertArrayEquals(cells, expected);
	}

	@Test
	public void kruskal_Simple()
	{
		Cell2D[][] cells = new KruskalMaze(2, 2).generateMaze(1L);
		Cell2D[][] expected = createCells(2, 2);
		setRow(expected, 0, SOUTH, WEST);
		setRow(expected, 1, EAST, null);
		assertArrayEquals(cells, expected);

		cells = new KruskalMaze(2, 2).generateMaze(2L);
		expected = createCells(2, 2);
		setRow(expected, 0, EAST, SOUTH);
		setRow(expected, 1, NORTH, null);
		assertArrayEquals(cells, expected);
	}

	@Test
	public void kruskal_Complex()
	{
		Cell2D[][] cells = new KruskalMaze(6, 6).generateMaze(1L);

		Cell2D[][] expected = createCells(6, 6);
		setRow(expected, 0, SOUTH, WEST, WEST, SOUTH, WEST, WEST);
		setRow(expected, 1, SOUTH, EAST, NORTH, WEST, WEST, SOUTH);
		setRow(expected, 2, EAST, SOUTH, NORTH, SOUTH, EAST, SOUTH);
		setRow(expected, 3, SOUTH, EAST, SOUTH, WEST, EAST, SOUTH);
		setRow(expected, 4, SOUTH, SOUTH, WEST, WEST, WEST, WEST);
		setRow(expected, 5, EAST, EAST, EAST, EAST, EAST, null);

		assertArrayEquals(expected, cells);
	}

	private Cell2D[][] createCells(int width, int height)
	{
		Cell2D[][] ret = new Cell2D[height][width];
		for(int h = 0; h < height; h++)
			for(int w = 0; w < width; w++)
				ret[h][w] = new Cell2D(h, w);
		return ret;
	}

	/** for each cell, in the row, attach it to an adjacent cell */
	@SuppressWarnings("FeatureEnvy")
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
