package spinnytea.puzzle.square;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestTransform
{

	private Board board;
	private Piece test;
	private Piece filler;

	@Before
	public void before()
	{
		test = new Piece(new boolean[][] { { true, true, true }, { true, false, false } });
		filler = new Piece(new boolean[][] { { true, true, true }, { true, true, false } });
		board = new Board("simple transform unit test board", 3, 3, test, filler);
	}

	@Test
	public void N_0()
	{
		board.placePiece(0, 0, 0, Transform.N_0);
		assertEquals("" //
		+ "|XXX|\n" //
		+ "|X  |\n" //
		+ "|   |\n", board.asciiPrint());
	}

	@Test
	public void N_90()
	{
		board.placePiece(0, 0, 0, Transform.N_90);
		assertEquals("" //
		+ "|XX |\n" //
		+ "| X |\n" //
		+ "| X |\n", board.asciiPrint());
	}

	@Test
	public void N_180()
	{
		board.placePiece(0, 0, 0, Transform.N_180);
		assertEquals("" //
		+ "|  X|\n" //
		+ "|XXX|\n" //
		+ "|   |\n", board.asciiPrint());
	}

	@Test
	public void N_270()
	{
		board.placePiece(0, 0, 0, Transform.N_270);
		assertEquals("" //
		+ "|X  |\n" //
		+ "|X  |\n" //
		+ "|XX |\n", board.asciiPrint());
	}

	@Test
	public void R_0()
	{
		board.placePiece(0, 0, 0, Transform.R_0);
		assertEquals("" //
		+ "|XXX|\n" //
		+ "|  X|\n" //
		+ "|   |\n", board.asciiPrint());
	}

	@Test
	public void R_90()
	{
		board.placePiece(0, 0, 0, Transform.R_90);
		assertEquals("" //
		+ "| X |\n" //
		+ "| X |\n" //
		+ "|XX |\n", board.asciiPrint());
	}

	@Test
	public void R_180()
	{
		board.placePiece(0, 0, 0, Transform.R_180);
		assertEquals("" //
		+ "|X  |\n" //
		+ "|XXX|\n" //
		+ "|   |\n", board.asciiPrint());
	}

	@Test
	public void R_270()
	{
		board.placePiece(0, 0, 0, Transform.R_270);
		assertEquals("" //
		+ "|XX |\n" //
		+ "|X  |\n" //
		+ "|X  |\n", board.asciiPrint());
	}
}
