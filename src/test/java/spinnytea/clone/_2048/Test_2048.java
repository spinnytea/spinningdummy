package spinnytea.clone._2048;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TEST moveRight
// TEST moveUp
// TEST moveDown

// TEST canMoveLeft
// TEST canMoveRight
// TEST canMoveUp
// TEST canMoveDown
public class Test_2048
{
	public static final Logger logger = LoggerFactory.getLogger(Test_2048.class);

	@Test
	public void moveLeft()
	{
		_2048 game = new _2048(4, 1);

		for(Row row : testCases)
		{
			logger.trace("moveLeft: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard().length, row.start.length);

			copyHorizontal(game.getBoard(), row.start);
			game.testLeft();
			equalsHorizontal(game.getBoard(), row.start);
			game.moveLeft(false);
			equalsHorizontal(game.getBoard(), row.result);
		}
	}

	private void copyHorizontal(int[][] board, int[] row)
	{
		for(int i = 0; i < row.length; i++)
			board[i][0] = row[i];
	}

	public void equalsHorizontal(int[][] board, int[] row)
	{
		int[] check = new int[row.length];

		for(int i = 0; i < row.length; i++)
			check[i] = board[i][0];

		assertArrayEquals(row, check);
	}

	@Test
	public void moveRight()
	{
		_2048 game = new _2048(4, 1);

		for(Row row : testCases)
		{
			logger.trace("moveRight: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard().length, row.start.length);

			copyHorizontalReverse(game.getBoard(), row.start);
			game.testRight();
			equalsHorizontalReverse(game.getBoard(), row.start);
			game.moveRight(false);
			equalsHorizontalReverse(game.getBoard(), row.result);
		}
	}

	private void copyHorizontalReverse(int[][] board, int[] row)
	{
		int max = row.length - 1;
		for(int i = 0; i < row.length; i++)
			board[max - i][0] = row[i];
	}

	private void equalsHorizontalReverse(int[][] board, int[] row)
	{
		int max = row.length - 1;
		int[] check = new int[row.length];
		for(int i = 0; i < row.length; i++)
			check[i] = board[max - i][0];
		assertArrayEquals(row, check);
	}

	@Test
	public void moveUp()
	{
		_2048 game = new _2048(1, 4);

		for(Row row : testCases)
		{
			logger.trace("moveUp: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard()[0].length, row.start.length);

			copyVertical(game.getBoard(), row.start);
			game.testUp();
			equalsVertical(game.getBoard(), row.start);
			game.moveUp(false);
			equalsVertical(game.getBoard(), row.result);
		}
	}

	private void copyVertical(int[][] board, int[] row)
	{
		for(int i = 0; i < row.length; i++)
			board[0][i] = row[i];
	}

	private void equalsVertical(int[][] board, int[] row)
	{
		int[] check = new int[row.length];
		for(int i = 0; i < row.length; i++)
			check[i] = board[0][i];
		assertArrayEquals(row, check);
	}

	@Test
	public void moveDown()
	{
		_2048 game = new _2048(1, 4);

		for(Row row : testCases)
		{
			logger.trace("moveDown: " + Arrays.toString(row.start));
			assertEquals(row.start.length, row.result.length);
			assertEquals(game.getBoard()[0].length, row.start.length);

			copyVerticalReverse(game.getBoard(), row.start);
			game.testDown();
			equalsVerticalReverse(game.getBoard(), row.start);
			game.moveDown(false);
			equalsVerticalReverse(game.getBoard(), row.result);
		}
	}

	private void copyVerticalReverse(int[][] board, int[] row)
	{
		int max = row.length - 1;
		for(int i = 0; i < row.length; i++)
			board[0][max - i] = row[i];
	}

	private void equalsVerticalReverse(int[][] board, int[] row)
	{
		int max = row.length - 1;
		int[] check = new int[row.length];
		for(int i = 0; i < row.length; i++)
			check[i] = board[0][max - i];
		assertArrayEquals(row, check);
	}

	//
	//
	//

	@AllArgsConstructor
	private static class Row
	{
		private int[] start;
		private int[] result;
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
