package spinnytea.maze.algorithms;

import spinnytea.maze.Cell2D;

/**
 * <p/>
 * As an interface, we need to declare the subclasses to run the algorithm. This allows us to configure the generation methods.
 * <p/>
 * <b>TODO</b> Binary tree maze<br>
 * <div style="margin-left:15px;"><b>Binary tree maze</b>: Most maze generation algorithms require maintaining relationships between cells within it, to ensure
 * the end result will be solvable. Valid simply connected mazes can however be generated by focusing on each cell independently. A binary tree maze is a
 * standard orthogonal maze where each cell always has a passage leading up or leading left, but never both. To create a binary tree maze, for each cell flip a
 * coin to decide whether to add a passage leading up or left. Always pick the same direction for cells on the boundary, and the end result will be a valid
 * simply connected maze that looks like a binary tree, with the upper left corner its root.</div> <br>
 */
public interface MazeAlgorithm
{
	/** generate a basic maze from scratch ~ use the seed to make it "deterministic" so we can get back to the same puzzles */
	public abstract Cell2D[][] generateMaze(long seed);
}