package spinnytea.maze.algorithms;

import spinnytea.maze.Cell2D;

import lombok.Data;

@Data
@SuppressWarnings("UnusedDeclaration")
class MazeAlgorithmFrontier
{
	private final Cell2D from;
	private final Cell2D to;
	private final Cell2D.Direction direction;
}
