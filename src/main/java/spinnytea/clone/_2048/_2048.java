package spinnytea.clone._2048;

import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class _2048
{
	private static final Random RAND = new Random();
	private static final int GOAL = 2048;
	private static final int DEFAULT_WIDTH = 4;
	private static final int DEFAULT_HEIGHT = 4;

	/** int[horizontal][vertical] */
	@Getter(value = AccessLevel.PACKAGE)
	private int[][] board;
	private final int boardWidth;
	private final int boardHeight;
	private int score = 0;
	private boolean win = false;
	private boolean lose = false;

	public _2048()
	{
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public _2048(int width, int height)
	{
		boardWidth = width;
		boardHeight = height;
		board = new int[boardWidth][boardHeight];
		generateNumber();
		generateNumber();

		// shift left test:
		// TEST moveLeft
		// TEST moveRight
		// TEST moveUp
		// TEST moveDown
		// 0 2 0 0; 2 0 0 0
//		board[1][0] = 2;
		// 0 0 0 2
//		board[3][0] = 2;
		// 0 2 4 0
//		board[1][0] = 2;
//		board[2][0] = 4;
		// 2 0 0 4
//		board[0][0] = 2;
//		board[3][0] = 4;
		// 2 2 0 0
//		board[0][0] = 2;
//		board[1][0] = 2;
		// 0 2 2 0
//		board[1][0] = 2;
//		board[2][0] = 2;
		// 4 2 2 4; // 4 4 4 0
//		board[0][0] = 4;
//		board[1][0] = 2;
//		board[2][0] = 2;
//		board[3][0] = 4;
		// 2 2 4 4
//		board[0][0] = 2;
//		board[1][0] = 2;
//		board[2][0] = 4;
//		board[3][0] = 4;
		// 2 2 2 2; 4 4 0 0
//		board[0][0] = 2;
//		board[1][0] = 2;
//		board[2][0] = 2;
//		board[3][0] = 2;

		// TEST canMoveLeft
		// TEST canMoveRight
		// TEST canMoveUp
		// TEST canMoveDown
	}

	public boolean canMoveLeft()
	{
		if(lose || win)
			return false;

		// if the board changes because we go moveLeft, then it counts as being able to move
		return !equalsBoard(testLeft(), board);
	}

	public void moveLeft()
	{
		moveLeft(true);
	}

	void moveLeft(boolean gen)
	{
		if(gen && !canMoveLeft())
			return;

		for(int v = 0; v < boardHeight; v++)
		{
			// first pass, combine adjacent numbers
			int h1 = 0;
			int h2 = 0;

			while(h1 < boardWidth && h2 < boardWidth)
			{
				// find the first non-zero
				while(h1 < boardWidth && board[h1][v] == 0)
					h1++;

				// find the second non-zero
				h2 = h1 + 1;
				while(h2 < boardWidth && board[h2][v] == 0)
					h2++;

				if(h1 == boardWidth || h2 == boardWidth)
					break;
				else
				{
					// I found two non zeros!
					if(board[h1][v] == board[h2][v])
					{
						// and they are the same!
						board[h1][v] += board[h2][v];
						board[h2][v] = 0;
						score += board[h1][v];

						// move h1 just past h2
						h1 = h2 + 1;
					}
					else
					{
						// otherwise, set h1 to where h2 is
						// so we can consider h2 as the next place to combine
						h1 = h2;
					}

					// now move h2 just past that
					// h2 = h1 + 1;
					// h2 += 2
					// this step doesn't matter
				}
			}

			// second pass, shift everything to the left
			h1 = 0;
			h2 = 0;
			while(h1 < boardWidth && h2 < boardWidth)
			{
				// find the first zero
				while(h1 < boardWidth && board[h1][v] != 0)
					h1++;

				// find subsequent non-zeros
				for(h2 = h1 + 1; h2 < boardWidth; h2++)
					if(board[h2][v] != 0)
					{
						// shift the number over
						board[h1][v] = board[h2][v];
						board[h2][v] = 0;
						h1++;
					}
			}
		}

		if(gen)
			generateNumber();
	}

	public boolean canMoveRight()
	{
		if(lose || win)
			return false;

		// if the board changes because we go moveRight, then it counts as being able to move
		return !equalsBoard(testRight(), board);
	}

	public void moveRight()
	{
		moveRight(true);
	}

	void moveRight(boolean gen)
	{
		if(gen && !canMoveRight())
			return;

		for(int v = 0; v < boardHeight; v++)
		{
			// first pass, combine adjacent numbers
			int h1 = boardWidth - 1;
			int h2 = boardWidth - 1;

			while(h1 >= 0 && h2 >= 0)
			{
				// find the first non-zero
				while(h1 >= 0 && board[h1][v] == 0)
					h1--;

				// find the second non-zero
				h2 = h1 - 1;
				while(h2 >= 0 && board[h2][v] == 0)
					h2--;

				if(h1 < 0 || h2 < 0)
					break;
				else
				{
					// I found two non zeros!
					if(board[h1][v] == board[h2][v])
					{
						// and they are the same!
						board[h1][v] += board[h2][v];
						board[h2][v] = 0;
						score += board[h1][v];

						// move h1 just past h2
						h1 = h2 - 1;
					}
					else
					{
						// otherwise, set h1 to where h2 is
						// so we can consider h2 as the next place to combine
						h1 = h2;
					}

					// now move h2 just past that
					// h2 = h1 + 1;
					// h2 += 2
					// this step doesn't matter
				}
			}

			// second pass, shift everything to the left
			h1 = boardWidth - 1;
			h2 = boardWidth - 1;
			while(h1 >= 0 && h2 >= 0)
			{
				// find the first zero
				while(h1 >= 0 && board[h1][v] != 0)
					h1--;

				// find subsequent non-zeros
				for(h2 = h1 - 1; h2 >= 0; h2--)
					if(board[h2][v] != 0)
					{
						// shift the number over
						board[h1][v] = board[h2][v];
						board[h2][v] = 0;
						h1--;
					}
			}
		}

		if(gen)
			generateNumber();
	}

	public boolean canMoveUp()
	{
		if(lose || win)
			return false;

		// if the board changes because we go moveUp, then it counts as being able to move
		return !equalsBoard(testUp(), board);
	}

	public void moveUp()
	{
		moveUp(true);
	}

	void moveUp(boolean gen)
	{
		if(gen && !canMoveUp())
			return;

		for(int h = 0; h < boardWidth; h++)
		{
			// first pass, combine adjacent numbers
			int v1 = 0;
			int v2 = 0;

			while(v1 < boardHeight && v2 < boardHeight)
			{
				// find the first non-zero
				while(v1 < boardHeight && board[h][v1] == 0)
					v1++;

				// find the second non-zero
				v2 = v1 + 1;
				while(v2 < boardHeight && board[h][v2] == 0)
					v2++;

				if(v1 == boardHeight || v2 == boardHeight)
					break;
				else
				{
					// I found two non zeros!
					if(board[h][v1] == board[h][v2])
					{
						// and they are the same!
						board[h][v1] += board[h][v2];
						board[h][v2] = 0;
						score += board[h][v1];

						// move v1 just past v2
						v1 = v2 + 1;
					}
					else
					{
						// otherwise, set v1 to where v2 is
						// so we can consider v2 as the next place to combine
						v1 = v2;
					}

					// now move v2 just past that
					// v2 = v1 + 1;
					// v2 += 2
					// this step doesn't matter
				}
			}

			// second pass, shift everything to the left
			v1 = 0;
			v2 = 0;
			while(v1 < boardHeight && v2 < boardHeight)
			{
				// find the first zero
				while(v1 < boardHeight && board[h][v1] != 0)
					v1++;

				// find subsequent non-zeros
				for(v2 = v1 + 1; v2 < boardHeight; v2++)
					if(board[h][v2] != 0)
					{
						// shift the number over
						board[h][v1] = board[h][v2];
						board[h][v2] = 0;
						v1++;
					}
			}
		}

		if(gen)
			generateNumber();
	}

	public boolean canMoveDown()
	{
		if(lose || win)
			return false;

		// if the board changes because we go moveDown, then it counts as being able to move
		return !equalsBoard(testDown(), board);
	}

	public void moveDown()
	{
		moveDown(true);
	}

	void moveDown(boolean gen)
	{
		if(gen && !canMoveDown())
			return;

		for(int h = 0; h < boardWidth; h++)
		{
			// first pass, combine adjacent numbers
			int v1 = boardHeight - 1;
			int v2 = boardHeight - 1;

			while(v1 >= 0 && v2 >= 0)
			{
				// find the first non-zero
				while(v1 >= 0 && board[h][v1] == 0)
					v1--;

				// find the second non-zero
				v2 = v1 - 1;
				while(v2 >= 0 && board[h][v2] == 0)
					v2--;

				if(v1 < 0 || v2 < 0)
					break;
				else
				{
					// I found two non zeros!
					if(board[h][v1] == board[h][v2])
					{
						// and they are the same!
						board[h][v1] += board[h][v2];
						board[h][v2] = 0;
						score += board[h][v1];

						// move v1 just past v2
						v1 = v2 - 1;
					}
					else
					{
						// otherwise, set v1 to where v2 is
						// so we can consider v2 as the next place to combine
						v1 = v2;
					}

					// now move v2 just past that
					// v2 = v1 + 1;
					// v2 += 2
					// this step doesn't matter
				}
			}

			// second pass, shift everything to the left
			v1 = boardHeight - 1;
			v2 = boardHeight - 1;
			while(v1 >= 0 && v2 >= 0)
			{
				// find the first zero
				while(v1 >= 0 && board[h][v1] != 0)
					v1--;

				// find subsequent non-zeros
				for(v2 = v1 - 1; v2 >= 0; v2--)
					if(board[h][v2] != 0)
					{
						// shift the number over
						board[h][v1] = board[h][v2];
						board[h][v2] = 0;
						v1--;
					}
			}
		}

		if(gen)
			generateNumber();
	}

	public int[][] testLeft()
	{
		// store a copy of the board
		int[][] temp = copyBoard();

		// try moving left
		moveLeft(false);

		// keep the new board
		int[][] ret = board;

		// restore the old board
		board = temp;

		// return the new board
		return ret;
	}

	public int[][] testRight()
	{
		// store a copy of the board
		int[][] temp = copyBoard();

		// try moving left
		moveRight(false);

		// keep the new board
		int[][] ret = board;

		// restore the old board
		board = temp;

		// return the new board
		return ret;
	}

	public int[][] testUp()
	{
		// store a copy of the board
		int[][] temp = copyBoard();

		// try moving left
		moveUp(false);

		// keep the new board
		int[][] ret = board;

		// restore the old board
		board = temp;

		// return the new board
		return ret;
	}

	public int[][] testDown()
	{
		// store a copy of the board
		int[][] temp = copyBoard();

		// try moving left
		moveDown(false);

		// keep the new board
		int[][] ret = board;

		// restore the old board
		board = temp;

		// return the new board
		return ret;
	}

	private void generateNumber()
	{
		if(lose || win)
			return;

		// first count the number of empty squares
		int empty = 0;
		for(int h = 0; h < boardWidth; h++)
			for(int v = 0; v < boardHeight; v++)
			{
				if(board[h][v] == 0)
					empty++;
				if(board[h][v] == GOAL)
				{
					win = true;
					return;
				}
			}

		boolean canLose = (empty == 1 || empty == 0);

		if(empty > 0)
		{
			// now, pick a random one to place a number in
			// this is how many empty spaces we will skip
			empty = RAND.nextInt(empty);
			pick_spot:
			for(int h = 0; h < boardWidth; h++)
				for(int v = 0; v < boardHeight; v++)
					if(board[h][v] == 0)
					{
						empty--;
						if(empty < 0)
						{
//							board[h][v] = (RAND.nextInt(2) + 1) * 2;
//							board[h][v] = (RAND.nextBoolean() ? 2 : 4);
							board[h][v] = ((RAND.nextDouble() < 0.7) ? 2 : 4);
							break pick_spot;
						}
					}
		}

		if(canLose)
		{
			if(canMoveLeft())
				return;
			if(canMoveRight())
				return;
			if(canMoveUp())
				return;
			if(canMoveDown())
				return;
			lose = true;
		}
	}

	private int[][] copyBoard()
	{
		// store a copy of the board
		int[][] ret = new int[boardWidth][];
		for(int h = 0; h < boardWidth; h++)
			ret[h] = Arrays.copyOf(board[h], boardHeight);
		return ret;
	}

	private boolean equalsBoard(int[][] one, int[][] two)
	{
		for(int h = 0; h < boardWidth; h++)
			if(!Arrays.equals(one[h], two[h]))
				return false;
		return true;
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setContentPane(awt_2048.mvc_2048());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
