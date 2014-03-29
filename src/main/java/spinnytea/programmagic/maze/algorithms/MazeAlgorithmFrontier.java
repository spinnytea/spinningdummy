package spinnytea.programmagic.maze.algorithms;

import spinnytea.programmagic.maze.Cell2D;

import lombok.Data;

@Data
@SuppressWarnings("UnusedDeclaration")
class MazeAlgorithmFrontier
{
	private final Cell2D from;
	private final Cell2D to;
	private final Cell2D.Direction direction;
}
