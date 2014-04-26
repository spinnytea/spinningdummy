package spinnytea.puzzle.square;

public enum Transform
{
	/** Normal orientation, rotated 0 degrees */
	N_0,
	/** Normal orientation, rotated 90 degrees */
	N_90,
	/** Normal orientation, rotated 180 degrees */
	N_180,
	/** Normal orientation, rotated 270 degrees */
	N_270,

	/** Reversed along x, rotated 0 degrees */
	R_0,
	/** Reversed along x, rotated 90 degrees */
	R_90,
	/** Reversed along x, rotated 180 degrees */
	R_180,
	/** Reversed along x, rotated 270 degrees */
	R_270,

	;

	public int x(int px, int py, Piece p)
	{
		switch(this)
		{
		case N_0:
		case R_180:
			return px;
		case N_90:
		case R_90:
			return (p.getHeight() - 1) - py;
		case N_180:
		case R_0:
			return (p.getWidth() - 1) - px;
		case N_270:
		case R_270:
			return py;
		default:
			throw new UnsupportedOperationException("Not finished implementing that");
		}
	}

	public int y(int px, int py, Piece p)
	{
		switch(this)
		{
		case N_0:
		case R_0:
			return py;
		case N_90:
		case R_270:
			return px;
		case N_180:
		case R_180:
			return (p.getHeight() - 1) - py;
		case N_270:
		case R_90:
			return (p.getWidth() - 1) - px;
		default:
			throw new UnsupportedOperationException("Not finished implementing that");
		}
	}

	public Transform reverse()
	{
		switch(this)
		{
		case N_0:
			return R_0;
		case N_90:
			return R_270;
		case N_180:
			return R_180;
		case N_270:
			return R_90;

		case R_0:
			return N_0;
		case R_90:
			return N_270;
		case R_180:
			return N_180;
		case R_270:
			return N_90;
		}
		// just in case?
		return N_0;
	}

	public Transform next()
	{
		switch(this)
		{
		case N_0:
			return N_90;
		case N_90:
			return N_180;
		case N_180:
			return N_270;
		case N_270:
			return N_0;

		case R_0:
			return R_90;
		case R_90:
			return R_180;
		case R_180:
			return R_270;
		case R_270:
			return R_0;
		}
		// just in case?
		return N_0;
	}

	public Transform prev()
	{
		switch(this)
		{
		case N_0:
			return N_270;
		case N_90:
			return N_0;
		case N_180:
			return N_90;
		case N_270:
			return N_180;

		case R_0:
			return R_270;
		case R_90:
			return R_0;
		case R_180:
			return R_90;
		case R_270:
			return R_180;
		}
		// just in case?
		return N_0;
	}
}
