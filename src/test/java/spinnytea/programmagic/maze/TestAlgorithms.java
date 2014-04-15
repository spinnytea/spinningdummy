package spinnytea.programmagic.maze;

import static spinnytea.programmagic.maze.Cell2D.Direction.EAST;
import static spinnytea.programmagic.maze.Cell2D.Direction.NORTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.SOUTH;
import static spinnytea.programmagic.maze.Cell2D.Direction.WEST;

import spinnytea.programmagic.maze.algorithms.TestDepthFirst;
import spinnytea.programmagic.maze.algorithms.TestKruskal;
import spinnytea.programmagic.maze.algorithms.TestMultiDepthFirst;
import spinnytea.programmagic.maze.algorithms.TestPrim;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** these test cases have been created from a stable implementation */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = { TestDepthFirst.class, TestKruskal.class, TestMultiDepthFirst.class, TestPrim.class })
public class TestAlgorithms
{
	private static final Logger logger = LoggerFactory.getLogger(TestAlgorithms.class);

	//
	// helper methods
	//

	public static Cell2D[][] createCells(int width, int height)
	{
		Cell2D[][] ret = new Cell2D[height][width];
		for(int h = 0; h < height; h++)
			for(int w = 0; w < width; w++)
				ret[h][w] = new Cell2D(h, w);
		return ret;
	}

	/** for each cell, in the row, attach it to an adjacent cell */
	public static void setRow(Cell2D[][] cells, int h, Cell2D.Direction... row)
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
	public static void displayCells(Cell2D[][] cells)
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
