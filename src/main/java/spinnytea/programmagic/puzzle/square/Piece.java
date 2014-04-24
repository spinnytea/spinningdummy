package spinnytea.programmagic.puzzle.square;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Piece {
	/** true represents where the peice is, false represents where the piece is not */
	@Getter(value = AccessLevel.NONE)
	private final boolean[][] layout;

	/**
	 * @param px location relative to the piece (0 <= px < getWidth())
	 * @param py location relative to the piece (0 <= py < getHeight())
	 */
	public boolean isAt(int px, int py) {
		// is the y in the piece space
		if(py < 0 || py >= layout.length)
			return false;

		boolean[] y = layout[py];

		// is the x in the piece space
		if(px < 0 || px >= y.length)
			return false;

		return y[px];
	}

	/** the x position of the piece */
	private int x = -1;

	public void setX(int x) {
		if(placed)
			return;
		this.x = x;
	}

	/** the y position of the piece */
	private int y = -1;

	public void setY(int y) {
		if(placed)
			return;
		this.y = y;
	}

	/** is the piece on the board */
	private boolean placed;
	/** can we move the piece */
	private boolean fixed;

	@Setter(value = AccessLevel.NONE)
	private int size = -1;

	/** lazy calculation of size */
	public int getSize() {
		if(size < 0) {
			size = 0;
			for(boolean[] y : layout)
				for(boolean x : y)
					if(x)
						size++;
		}
		return size;
	}

	@Setter(value = AccessLevel.NONE)
	private int width = -1;

	public int getWidth() {
		if(width < 0)
			for(boolean[] y : layout)
				width = Math.max(width, y.length);
		return width;
	}

	public int getHeight() {
		return layout.length;
	}
}
