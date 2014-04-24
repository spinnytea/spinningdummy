package spinnytea.programmagic.puzzle.square;

import java.util.Arrays;

import lombok.Getter;

public class Board
{
	@Getter
	private final Piece[] pieces;

	/**
	 * this is how all of the pieces fit on the board; it's mainly used to check if we can add another piece
	 * <p/>
	 * the values represent the piece number at that location
	 * <p/>
	 * boardCache[y][x]
	 */
	@Getter
	private final int[][] boardCache;

	public Board(int width, int height, Piece... pieces)
	{
		boardCache = new int[height][width];
		this.pieces = pieces;

		int totalSize = 0;
		for(Piece p : pieces)
			totalSize += p.getSize();
		if(totalSize < width * height)
			throw new IllegalArgumentException("total piece area should add up to the board area");

		for(int[] y : boardCache)
			Arrays.fill(y, -1);
	}

	public int getWidth()
	{
		return boardCache[0].length;
	}

	public int getHeight()
	{
		return boardCache.length;
	}

	/**
	 * @param idx the piece we want to place
	 * @param x the top left corner
	 * @param y the top left corner
	 * @param t the orientation
	 */
	public boolean canPlacePiece(int idx, int x, int y, Transform t)
	{
		Piece p = pieces[idx];
		if(p.isFixed())
			return false;
		if(p.isPlaced())
			return false;

		// iterate over piece space
		for(int py = 0; py < p.getHeight(); py++)
			for(int px = 0; px < p.getWidth(); px++)
			{
				// is part of the piece at this location?
				if(p.isAt(px, py))
				{
					// position of the piece on the board
					int bx = x + t.x(px, py, p);
					int by = y + t.y(px, py, p);

					// if the piece is off the board, then it can't be placed
					if(bx < 0 || bx >= getWidth() || by < 0 || by >= getHeight())
						return false;

					// if this location is occupied by something else, then it can't be placed
					if(boardCache[by][bx] != -1)
						return false;
				}
			}

		// if we passed all the tests, then it should be fine
		return true;
	}

	public void placePiece(int idx, int x, int y, Transform t)
	{
		if(!canPlacePiece(idx, x, y, t))
			return;

		Piece p = pieces[idx];

		// iterate over piece space
		for(int py = 0; py < p.getHeight(); py++)
			for(int px = 0; px < p.getWidth(); px++)
				// is part of the piece at this location?
				if(p.isAt(px, py))
				{
					int bx = x + t.x(px, py, p);
					int by = y + t.y(px, py, p);

					boardCache[by][bx] = idx;
				}

		p.setX(x);
		p.setY(y);
		p.setPlaced(true);
	}

	public void removePiece(int idx)
	{
		Piece p = pieces[idx];

		if(!p.isPlaced())
			return;
		if(p.isFixed())
			return;

		// this will find all places regardless of the transform
		int max = Math.max(p.getHeight(), p.getWidth());

		for(int py = 0; py < max; py++)
			for(int px = 0; px < max; px++)
			{
				int bx = p.getX() + px;
				int by = p.getY() + py;
				if(bx >= 0 && by >= 0 && bx < getWidth() && by < getHeight())
					if(boardCache[by][bx] == idx)
						boardCache[by][bx] = -1;
			}

		p.setPlaced(false);
		p.setX(-1);
		p.setY(-1);
	}

	public String asciiPrint()
	{
		StringBuilder sb = new StringBuilder();
		for(int[] y : boardCache)
		{
			sb.append("|");
			for(int x : y)
				if(x == -1)
					sb.append(" ");
				else
					sb.append("X");
			sb.append("|\n");
		}
		return sb.toString();
	}
}
