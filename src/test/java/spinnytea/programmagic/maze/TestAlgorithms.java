package spinnytea.programmagic.maze;

import spinnytea.programmagic.maze.algorithms.DepthFirstMaze;

import javax.swing.JFrame;

import org.junit.Assert;
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
		expected[0][0].setRoom(Cell2D.Direction.EAST, expected[0][1]);
		expected[0][1].setRoom(Cell2D.Direction.SOUTH, expected[1][1]);
		expected[1][0].setRoom(Cell2D.Direction.EAST, expected[1][1]);
		Assert.assertArrayEquals(cells, expected);
	}

	private Cell2D[][] createCells(int width, int height)
	{
		Cell2D[][] ret = new Cell2D[height][width];
		for(int h = 0; h < height; h++)
			for(int w = 0; w < width; w++)
				ret[h][w] = new Cell2D(h, w);
		return ret;
	}

	private void displayCells(Cell2D[][] cells)
	{
		JFrame frame = new JFrame("Maze2D");
		frame.setContentPane(new Maze2D(cells));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		try
		{
			Thread.sleep(10000L);
		}
		catch(InterruptedException e)
		{
			logger.error("what?", e);
		}
	}
}
