package spinnytea.programmagic.maze;

import spinnytea.programmagic.maze.algorithms.KruskalMaze;
import spinnytea.programmagic.maze.algorithms.RecursiveDivisionMaze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Maze2D
extends JPanel
{
	private static final long serialVersionUID = 8719545072725768470L;
	private static final Random random = new Random();
	private static final int WallSize = 2; // really, it's twice this big
	private static final int CellSize = 20;
	private static final Color CellColor = Color.lightGray;
	private static final Color WallColor = Color.darkGray;

	private final Cell2D[][] maze;

	public Maze2D(Cell2D[][] maze)
	{
		this.maze = maze;
		setPreferredSize(new Dimension(maze[0].length * CellSize, maze.length * CellSize));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//
		// This is a very dumb method for painting rooms and walls
		//

		// fill the whole screen with black
		g.setColor(WallColor);
		g.fillRect(0, 0, maze[0].length * CellSize, maze.length * CellSize);

		g.setColor(CellColor);

		// now fill in the space with rooms
		for(int y = 0; y < maze.length; y++)
			for(int x = 0; x < maze[0].length; x++)
				if(maze[y][x].inTheMaze())
				{
					// draw the cell
					g.fillRect(x * CellSize + WallSize, y * CellSize + WallSize, CellSize - 2 * WallSize, CellSize - 2 * WallSize);

					// draw the north passage
					if(maze[y][x].north != null)
						g.fillRect(x * CellSize + WallSize, y * CellSize - WallSize, CellSize - 2 * WallSize, 2 * WallSize);
					// draw the west passage
					if(maze[y][x].west != null)
						g.fillRect(x * CellSize - WallSize, y * CellSize + WallSize, 2 * WallSize, CellSize - 2 * WallSize);
				}

		// remove the floating pillars when there is a tight box? (created by RecursiveDivisionMaze)
		for(int y = 0; y < maze.length - 1; y++)
			for(int x = 0; x < maze[0].length - 1; x++)
			{
				if(maze[y][x].south != null && maze[y][x].east != null && maze[y][x + 1].south != null && maze[y + 1][x].east != null)
					g.fillRect((x + 1) * CellSize - WallSize, (y + 1) * CellSize - WallSize, 2 * WallSize, 2 * WallSize);
			}
	}

	@SuppressWarnings("MagicNumber")
	public static void main(String[] args)
	{
		Maze2D maze2D = new Maze2D(new RecursiveDivisionMaze(20, 20, 5, KruskalMaze.class).generateMaze(random.nextLong()));
//		Maze2D maze2D = new Maze2D(new PrimMaze(10, 10, 4, 4).generateMaze(random.nextLong(), 50));

		JFrame frame = new JFrame("Maze2D");
		frame.setContentPane(maze2D);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
