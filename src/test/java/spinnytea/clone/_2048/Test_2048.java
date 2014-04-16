package spinnytea.clone._2048;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Test_2048
{
	@Test
	public void moveLeft()
	{
		_2048 game = new _2048(4, 1);

		for(Row row : testCases)
		{
			log.trace("moveLeft: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard().length, row.start.length);

			copyHorizontal(game.getBoard(), row.start);
			assertArrayEquals(row.start, extractHorizontal(game.getBoard()));
			game.testLeft();
			assertArrayEquals(row.start, extractHorizontal(game.getBoard()));
			assertEquals(!Arrays.equals(row.start, row.result), game.canMoveLeft());
			assertArrayEquals(row.start, extractHorizontal(game.getBoard()));

			game.moveLeft(false);
			assertArrayEquals(row.result, extractHorizontal(game.getBoard()));
		}
	}

	private void copyHorizontal(int[][] board, int[] row)
	{
		for(int i = 0; i < row.length; i++)
			board[i][0] = row[i];
	}

	private int[] extractHorizontal(int[][] board)
	{
		int[] check = new int[board.length];
		for(int i = 0; i < board.length; i++)
			check[i] = board[i][0];
		return check;
	}

	@Test
	public void moveRight()
	{
		_2048 game = new _2048(4, 1);

		for(Row row : testCases)
		{
			log.trace("moveRight: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard().length, row.start.length);

			copyHorizontalReverse(game.getBoard(), row.start);
			assertArrayEquals(row.start, extractHorizontalReverse(game.getBoard()));
			game.testRight();
			assertArrayEquals(row.start, extractHorizontalReverse(game.getBoard()));
			assertEquals(!Arrays.equals(row.start, row.result), game.canMoveRight());
			assertArrayEquals(row.start, extractHorizontalReverse(game.getBoard()));

			game.moveRight(false);
			assertArrayEquals(row.result, extractHorizontalReverse(game.getBoard()));
		}
	}

	private void copyHorizontalReverse(int[][] board, int[] row)
	{
		int max = row.length - 1;
		for(int i = 0; i < row.length; i++)
			board[max - i][0] = row[i];
	}

	private int[] extractHorizontalReverse(int[][] board)
	{
		int max = board.length - 1;
		int[] check = new int[board.length];
		for(int i = 0; i < board.length; i++)
			check[i] = board[max - i][0];
		return check;
	}

	@Test
	public void moveUp()
	{
		_2048 game = new _2048(1, 4);

		for(Row row : testCases)
		{
			log.trace("moveUp: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard()[0].length, row.start.length);

			copyVertical(game.getBoard(), row.start);
			assertArrayEquals(row.start, extractVertical(game.getBoard()));
			game.testUp();
			assertArrayEquals(row.start, extractVertical(game.getBoard()));
			assertEquals(!Arrays.equals(row.start, row.result), game.canMoveUp());
			assertArrayEquals(row.start, extractVertical(game.getBoard()));

			game.moveUp(false);
			assertArrayEquals(row.result, extractVertical(game.getBoard()));
		}
	}

	@SuppressWarnings("ManualArrayCopy") // because it mimics the others
	private void copyVertical(int[][] board, int[] row)
	{
		for(int i = 0; i < row.length; i++)
			board[0][i] = row[i];
	}

	@SuppressWarnings("ManualArrayCopy") // because it mimics the others
	private int[] extractVertical(int[][] board)
	{
		int[] check = new int[board[0].length];
		for(int i = 0; i < board[0].length; i++)
			check[i] = board[0][i];
		return check;
	}

	@Test
	public void moveDown()
	{
		_2048 game = new _2048(1, 4);

		for(Row row : testCases)
		{
			log.trace("moveDown: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard()[0].length, row.start.length);

			copyVerticalReverse(game.getBoard(), row.start);
			assertArrayEquals(row.start, extractVerticalReverse(game.getBoard()));
			game.testDown();
			assertArrayEquals(row.start, extractVerticalReverse(game.getBoard()));
			assertEquals(!Arrays.equals(row.start, row.result), game.canMoveDown());
			assertArrayEquals(row.start, extractVerticalReverse(game.getBoard()));

			game.moveDown(false);
			assertArrayEquals(row.result, extractVerticalReverse(game.getBoard()));
		}
	}

	private void copyVerticalReverse(int[][] board, int[] row)
	{
		int max = row.length - 1;
		for(int i = 0; i < row.length; i++)
			board[0][max - i] = row[i];
	}

	private int[] extractVerticalReverse(int[][] board)
	{
		int max = board[0].length - 1;
		int[] check = new int[board[0].length];
		for(int i = 0; i < board[0].length; i++)
			check[i] = board[0][max - i];
		return check;
	}

	//
	//
	//

	@AllArgsConstructor
	private static class Row
	{
		private final int[] start;
		private final int[] result;
	}

	// these are coded while imagining a moveLeft
	private static final Row[] testCases = { //
	new Row(new int[] { 2, 0, 0, 0 }, new int[] { 2, 0, 0, 0 }), //
	new Row(new int[] { 0, 2, 0, 0 }, new int[] { 2, 0, 0, 0 }), //
	new Row(new int[] { 0, 0, 0, 2 }, new int[] { 2, 0, 0, 0 }), //
	new Row(new int[] { 0, 2, 4, 0 }, new int[] { 2, 4, 0, 0 }), //
	new Row(new int[] { 2, 0, 0, 4 }, new int[] { 2, 4, 0, 0 }), //
	new Row(new int[] { 2, 2, 0, 0 }, new int[] { 4, 0, 0, 0 }), //
	new Row(new int[] { 0, 2, 2, 0 }, new int[] { 4, 0, 0, 0 }), //
	new Row(new int[] { 4, 2, 2, 4 }, new int[] { 4, 4, 4, 0 }), //
	new Row(new int[] { 4, 4, 4, 0 }, new int[] { 8, 4, 0, 0 }), //
	new Row(new int[] { 2, 2, 4, 4 }, new int[] { 4, 8, 0, 0 }), //
	new Row(new int[] { 2, 2, 2, 2 }, new int[] { 4, 4, 0, 0 }), //
	new Row(new int[] { 4, 4, 0, 0 }, new int[] { 8, 0, 0, 0 }), //
	};
}
