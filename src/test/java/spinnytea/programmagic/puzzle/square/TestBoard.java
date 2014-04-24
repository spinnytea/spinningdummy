package spinnytea.programmagic.puzzle.square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestBoard
{
	private Piece p0;
	private Piece p1;
	private Piece p2;
	private Board b;

	@Before
	public void before()
	{
		p0 = new Piece(new boolean[][] { { true, true }, { true, false } });
		p1 = new Piece(new boolean[][] { { true, true }, { true, false } });
		p2 = new Piece(new boolean[][] { { true, true, true } });

		b = new Board("Simple unit test board", 3, 3, p0, p1, p2);
	}

	@Test
	public void canPlacePiece()
	{
		// we should be able to place all of the pieces on the empty board
		assertTrue(b.canPlacePiece(0, 0, 0, Transform.N_0));
		assertTrue(b.canPlacePiece(1, 0, 0, Transform.N_0));
		assertTrue(b.canPlacePiece(2, 0, 0, Transform.N_0));

		// place a piece
		b.placePiece(0, 0, 0, Transform.N_0);

		// we shouldn't be able to place any piece at the current location
		assertFalse(b.canPlacePiece(0, 0, 0, Transform.N_0));
		assertFalse(b.canPlacePiece(1, 0, 0, Transform.N_0));
		assertFalse(b.canPlacePiece(2, 0, 0, Transform.N_0));

		// place the second piece
		assertTrue(b.canPlacePiece(1, 1, 1, Transform.R_0));
		b.placePiece(1, 1, 1, Transform.R_0);
	}

	@Test
	public void removePiece()
	{
		assertEquals("" //
		+ "|   |\n" //
		+ "|   |\n" //
		+ "|   |\n", b.asciiPrint());

		b.placePiece(0, 0, 0, Transform.N_0);
		b.placePiece(1, 1, 1, Transform.N_0);
		assertEquals("" //
		+ "|XX |\n" //
		+ "|XXX|\n" //
		+ "| X |\n", b.asciiPrint());

		b.removePiece(0);
		assertEquals("" //
		+ "|   |\n" //
		+ "| XX|\n" //
		+ "| X |\n", b.asciiPrint());
	}
}
