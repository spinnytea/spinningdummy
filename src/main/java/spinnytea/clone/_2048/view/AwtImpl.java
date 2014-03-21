package spinnytea.clone._2048.view;

import spinnytea.clone._2048._2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/** draw the game with awt */
public class AwtImpl
extends JPanel
implements Repaintable
{
	private static final long serialVersionUID = -3870595594462521279L;
	private static final Color ZERO_COLOR = Color.darkGray;
	private static final Color BORDER_COLOR = Color.lightGray;
	private static final float[] MIN_COLOR = { 0.1f, 0.7f, 0.8f };
	private static final float[] MAX_COLOR = { 0.9f, 0.8f, 0.9f };
	private static final Color NUMBER_COLOR = Color.white;

	private static final int BLOCK_SIZE = 80;
	private static final int BORDER_SIZE = 4;

	private final boolean lookahead;
	private final _2048 game;
	private final BufferedImage boardBuffer;

	public AwtImpl(_2048 game, boolean lookahead)
	{
		this.game = game;
		this.lookahead = lookahead;
		boardBuffer = new BufferedImage(game.getBoard().length * BLOCK_SIZE, game.getBoard()[0].length * BLOCK_SIZE, BufferedImage.TYPE_INT_RGB);

		if(lookahead)
			setPreferredSize(new Dimension((game.getBoard().length * 2 + 2) * BLOCK_SIZE, (game.getBoard()[0].length * 2 + 2) * BLOCK_SIZE));
		else
			setPreferredSize(new Dimension(boardBuffer.getWidth(), boardBuffer.getHeight()));
		setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// draw the main baord
		drawBoard(game.getBoard());

		if(!lookahead)
		{
			g.drawImage(boardBuffer, 0, 0, null);
		}
		else
		{
			int width = game.getBoard().length;
			int height = game.getBoard()[0].length;

			g.drawImage(boardBuffer, (width + 2) * BLOCK_SIZE / 2, (height + 2) * BLOCK_SIZE / 2, null);

			int midWidthOffset = (width + 2) * BLOCK_SIZE / 2 + width * BLOCK_SIZE / 4;
			int maxWidthOffset = (width * 2 + 2) * BLOCK_SIZE - width * BLOCK_SIZE / 2;
			int midHeightOffset = (height + 2) * BLOCK_SIZE / 2 + height * BLOCK_SIZE / 4;
			int maxHeightOffset = (height * 2 + 2) * BLOCK_SIZE - height * BLOCK_SIZE / 2;

			// draw what would happen if we pressed "left"
			drawBoard(game.testLeft());
			g.drawImage(boardBuffer, 0, midHeightOffset, boardBuffer.getWidth() / 2, boardBuffer.getHeight() / 2, null);
			// draw what would happen if we pressed "right"
			drawBoard(game.testRight());
			g.drawImage(boardBuffer, maxWidthOffset, midHeightOffset, boardBuffer.getWidth() / 2, boardBuffer.getHeight() / 2, null);
			// draw what would happen if we pressed "up"
			drawBoard(game.testUp());
			g.drawImage(boardBuffer, midWidthOffset, 0, boardBuffer.getWidth() / 2, boardBuffer.getHeight() / 2, null);
			// draw what would happen if we pressed "down"
			drawBoard(game.testDown());
			g.drawImage(boardBuffer, midWidthOffset, maxHeightOffset, boardBuffer.getWidth() / 2, boardBuffer.getHeight() / 2, null);
		}

		// TODO draw lose
		if(game.isLose())
			System.out.println("lose: " + game.getScore()); // FIXME remove
		// TODO draw win
		if(game.isWin())
			System.out.println("win: " + game.getScore()); // FIXME remove
	}

	private void drawBoard(int[][] board)
	{
		Graphics g = boardBuffer.getGraphics();
		g.setFont(new Font(null, Font.BOLD, BLOCK_SIZE / 3));

		for(int h = 0; h < board.length; h++)
			for(int v = 0; v < board[0].length; v++)
			{
				g.setColor(BORDER_COLOR);
				g.fillRect(h * BLOCK_SIZE, v * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

				if(board[h][v] == 0)
				{
					g.setColor(ZERO_COLOR);
				}
				else
				{
					// TODO configure the interpolation percent
					// do we want to max out
					float percent = interpolation(1, minlog2(board[h][v]), 11);
					g.setColor(Color.getHSBColor( //
					interpolate(MIN_COLOR[0], MAX_COLOR[0], percent), //
					interpolate(MIN_COLOR[1], MAX_COLOR[1], percent), //
					interpolate(MIN_COLOR[2], MAX_COLOR[2], percent) //
					));
				}

				g.fillRect(h * BLOCK_SIZE + BORDER_SIZE, v * BLOCK_SIZE + BORDER_SIZE, BLOCK_SIZE - BORDER_SIZE * 2, BLOCK_SIZE - BORDER_SIZE * 2);

				if(board[h][v] != 0)
				{
					g.setColor(NUMBER_COLOR);
					// TODO font specifics
					g.drawString(board[h][v] + "", //
					h * BLOCK_SIZE + (BLOCK_SIZE - (int) g.getFontMetrics().getStringBounds(board[h][v] + "", g).getWidth()) / 2, //
					v * BLOCK_SIZE + (BLOCK_SIZE + g.getFontMetrics().getAscent()) / 2 //
					);
				}
			}
	}

	/** calculates Math.floor(Math.log(x)/Math.log(2)) using interger math */
	private int minlog2(int val)
	{
		int exp = 0;
		while(val > 1)
		{
			val = (val >>> 1);
			exp++;
		}
		return exp;
	}

	/** calculate the percentage that value is between min and max; min <= value will return 0, max >= value will return 1 */
	private float interpolation(int min, int value, int max)
	{
		return Math.min(1f, Math.max(0f, ((float) (value - min)) / ((float) (max - min))));
	}

	/** interpolate the values */
	private float interpolate(float min, float max, float percent)
	{
		return ((max - min) * percent) + min;
	}
}
