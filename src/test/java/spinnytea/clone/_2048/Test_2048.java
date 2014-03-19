package spinnytea.clone._2048;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertArrayEquals;

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
			logger.info("moveLeft: " + Arrays.toString(row.start));
			copyHorizontal(game.getBoard(), row.start);
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
