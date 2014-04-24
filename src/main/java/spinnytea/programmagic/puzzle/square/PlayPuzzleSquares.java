package spinnytea.programmagic.puzzle.square;

import javax.swing.JFrame;

public class PlayPuzzleSquares
{
	private static Board testBoard()
	{
		Board b = new Board(4, 3, //
		new Piece(new boolean[][] { { true, true }, { true, false } }), //
		new Piece(new boolean[][] { { true, true }, { true, false } }), //
		new Piece(new boolean[][] { { true, true, true }, { true, false, false } }), //
		new Piece(new boolean[][] { { true, true } }));

		// place one piece
		b.placePiece(0, 0, 0, Transform.N_0);
		// fix another
		b.placePiece(2, 2, 0, Transform.N_90);
		b.getPieces()[2].setFixed(true);

		return b;
	}

	private static Piece[] mensaPieces()
	{
		return new Piece[] { //
		new Piece(new boolean[][] { { true } }), // this one shouldn't be used
				new Piece(new boolean[][] { { true }, { true }, { true }, { true }, { true }, { true } }), // 1
				new Piece(new boolean[][] { { true, true }, { true, false }, { true, false }, { true, false }, { true, false } }), // 2
				new Piece(new boolean[][] { { true, false }, { true, false }, { true, false }, { true, true }, { true, false } }), // 3
				new Piece(new boolean[][] { { true, false, false }, { true, true, false }, { false, true, false }, { false, true, true } }), // 4
				new Piece(new boolean[][] { { false, true }, { false, true }, { true, true }, { true, true }, }), // 5
				new Piece(new boolean[][] { { false, false, true }, { false, false, true }, { false, false, true }, { true, true, true } }), // 6
				new Piece(new boolean[][] { { true, true }, { true, true }, { true, true } }), // 7
				new Piece(new boolean[][] { { true, false }, { true, true }, { true, true }, { true, false } }), // 8
				new Piece(new boolean[][] { { true, true, true, true }, { false, true, false, true } }), // 9
				new Piece(new boolean[][] { { true, true, true, false }, { false, true, true, true } }), // 10
				new Piece(new boolean[][] { { true, true, false }, { true, true, false }, { false, true, true }, }), // 11
				new Piece(new boolean[][] { { false, true, false }, { true, true, true }, { false, true, true } }), // 12
				new Piece(new boolean[][] { { true, true }, { false, true }, { false, true }, { true, true } }), // 13
				new Piece(new boolean[][] { { false, true, true }, { false, false, true }, { true, true, true } }), // 14
				new Piece(new boolean[][] { { false, false, true }, { false, true, true }, { true, true, true } }), // 15
				new Piece(new boolean[][] { { false, true, true }, { true, true, false }, { false, true, false }, { false, true, false } }), // 16
				new Piece(new boolean[][] { { false, false, true }, { false, false, true }, { true, true, true }, { false, false, true } }), // 17
				new Piece(new boolean[][] { { false, false, true }, { true, true, true }, { false, true, true } }), // 18
				new Piece(new boolean[][] { { false, true, false }, { false, true, false }, { true, true, true }, { false, false, true } }), // 19
				new Piece(new boolean[][] { { false, true, false }, { true, true, false }, { false, true, false }, { false, true, true } }), // 20
		};
	}

	private static Board mensaBoard1()
	{
		Piece[] pieces = mensaPieces();
		Board b = new Board(10, 6, pieces[2], pieces[3], pieces[4], pieces[9], pieces[10], pieces[13], pieces[14], pieces[15], pieces[16], pieces[18]);
		b.placePiece(0, 0, 4, Transform.N_270);
		b.getPieces()[0].setFixed(true);
		b.placePiece(3, 0, 0, Transform.N_270);
		b.getPieces()[3].setFixed(true);
		b.placePiece(5, 6, 0, Transform.N_270);
		b.getPieces()[5].setFixed(true);
		return b;
	}

	private static Board mensaBoard2()
	{
		Piece[] pieces = mensaPieces();
		Board b = new Board(10, 6, pieces[3], pieces[6], pieces[7], pieces[8], pieces[10], pieces[12], pieces[14], pieces[15], pieces[17], pieces[19]);
		b.placePiece(8, 3, 2, Transform.N_90);
		b.getPieces()[8].setFixed(true);
		return b;
	}

	private static Board mensaBoard3()
	{
		Piece[] pieces = mensaPieces();
		Board b = new Board(10, 6, pieces[1], pieces[4], pieces[7], pieces[11], pieces[13], pieces[14], pieces[15], pieces[17], pieces[19], pieces[20]);
		return b;
	}

	public static void main(String[] args)
	{
		BoardPanel panel = new BoardPanel(mensaBoard1());

		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.addKeyListener(panel.getKeyListener());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
