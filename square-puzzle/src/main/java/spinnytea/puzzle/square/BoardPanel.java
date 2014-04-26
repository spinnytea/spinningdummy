package spinnytea.puzzle.square;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import lombok.NonNull;

public class BoardPanel
extends JPanel
{
	private static final long serialVersionUID = 943348345958943170L;
	private static final int SQUARE_SIZE = 24;

	// colors
	private static final Color BORDER_COLOR = Color.black;
	private static final Color NO_PIECE_COLOR = Color.lightGray;
	private static final Color PIECE_FILL_COLOR = Color.white;
	private static final Color PIECE_OFF_BOARD = Color.darkGray;
	private static final Color SELECTED_PLACED = new Color(255, 255, 162);
	private static final Color SELECTED_CAN_PLACE = new Color(162, 255, 162, 128);
	private static final Color SELECTED_CANNOT_PLACE = new Color(255, 162, 162, 128);
	private static TexturePaint FIXED_PIECE_PAINT;

	{
		BufferedImage img = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(new Color(255, 255, 162));
		g.fillRect(0, 0, SQUARE_SIZE, SQUARE_SIZE);
		g.setColor(new Color(196, 196, 196));
		g.setStroke(new BasicStroke(2));
		for(int i = -SQUARE_SIZE; i <= SQUARE_SIZE; i += SQUARE_SIZE / 6)
		{
			g.drawLine(i - SQUARE_SIZE, i + SQUARE_SIZE, i + SQUARE_SIZE, i - SQUARE_SIZE);
			g.drawLine(i - SQUARE_SIZE, i + SQUARE_SIZE, i + SQUARE_SIZE, i - SQUARE_SIZE);
		}
		FIXED_PIECE_PAINT = new TexturePaint(img, new Rectangle2D.Double(0, 0, SQUARE_SIZE, SQUARE_SIZE));
	}

	/** the board and all it's pieces */
	private final Board board;
	private final Piece[] pieces;

	private int selectedIdx = 1;
	private int selectedX = 0;
	private int selectedY = 0;
	private Transform selectedT = Transform.N_0;

	public BoardPanel(@NonNull Board board)
	{
		this.board = board;
		pieces = board.getPieces();

		selectPrev();

		setPreferredSize(new Dimension((board.getWidth() + 6) * SQUARE_SIZE, (board.getHeight() + 2) * 3 * SQUARE_SIZE));
	}

	public static JMenuBar buildJMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenuItem keyMap = new JMenuItem("Keys");
		keyMap.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				StringBuilder description = new StringBuilder();
				description.append(KeyEvent.getKeyText(KeyEvent.VK_LEFT)).append(" - move selected piece left\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_RIGHT)).append(" - move selected piece right\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_UP)).append(" - move selected piece up\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_DOWN)).append(" - move selected piece down\n");
				description.append("\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_E)).append(" - rotate selected piece left\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_T)).append(" - rotate selected piece right\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_R)).append(" - reverse selected piece\n");
				description.append("\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_MINUS)).append(", ").append(KeyEvent.getKeyText(KeyEvent.VK_PAGE_DOWN))
					.append(" - select previous piece\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_PLUS)).append(", ").append(KeyEvent.getKeyText(KeyEvent.VK_PAGE_UP))
					.append(" - select next piece\n");
				description.append("\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_ENTER)).append(", ").append(KeyEvent.getKeyText(KeyEvent.VK_SPACE))
					.append(" - place/remove selected piece\n");
				description.append(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)).append(" - reset board");

				JFrame frame = new JFrame("Keys");
				frame.setContentPane(new JTextArea(description.toString()));
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});

		JMenu aboutMenu = new JMenu("About");
		aboutMenu.add(keyMap);
		menuBar.add(aboutMenu);

		return menuBar;
	}

	private KeyListener keyListener = null;

	public KeyListener getKeyListener()
	{
		if(keyListener == null)
			keyListener = new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent evt)
				{
					// these options are locked when the piece has been placed
					Piece p = pieces[selectedIdx];
					if(!p.isPlaced())
					{
						switch(evt.getKeyCode())
						{
						case KeyEvent.VK_LEFT:
							selectedX--;
							break;
						case KeyEvent.VK_RIGHT:
							selectedX++;
							break;
						case KeyEvent.VK_UP:
							selectedY--;
							break;
						case KeyEvent.VK_DOWN:
							selectedY++;
							break;

						case KeyEvent.VK_E:
							selectedT = selectedT.prev();
							break;
						case KeyEvent.VK_T:
							selectedT = selectedT.next();
							break;
						case KeyEvent.VK_R:
							selectedT = selectedT.reverse();
							break;
						}
					}

					switch(evt.getKeyCode())
					{
					case KeyEvent.VK_PAGE_DOWN:
					case KeyEvent.VK_MINUS:
					case KeyEvent.VK_UNDERSCORE:
						selectPrev();
						break;
					case KeyEvent.VK_PAGE_UP:
					case KeyEvent.VK_PLUS:
					case KeyEvent.VK_EQUALS:
						selectNext();
						break;
					case KeyEvent.VK_ENTER:
					case KeyEvent.VK_SPACE:
						if(p.isPlaced())
							board.removePiece(selectedIdx);
						else
							board.placePiece(selectedIdx, selectedX, selectedY, selectedT);
						break;
					case KeyEvent.VK_ESCAPE:
						for(int idx = 0; idx < pieces.length; idx++)
							board.removePiece(idx);
					}

					repaint();
				}
			};
		return keyListener;
	}

	private void selectNext()
	{
		int start = selectedIdx;
		do
		{
			selectedIdx++;
			if(selectedIdx >= pieces.length)
				selectedIdx = 0;
		}
		while(pieces[selectedIdx].isFixed() && selectedIdx != start);

		if(pieces[selectedIdx].isPlaced())
		{
			selectedX = pieces[selectedIdx].getX();
			selectedY = pieces[selectedIdx].getY();
			// no need to deduce transform
		}
	}

	private void selectPrev()
	{
		int start = selectedIdx;
		do
		{
			selectedIdx--;
			if(selectedIdx < 0)
				selectedIdx = pieces.length - 1;
		}
		while(pieces[selectedIdx].isFixed() && selectedIdx != start);

		if(pieces[selectedIdx].isPlaced())
		{
			selectedX = pieces[selectedIdx].getX();
			selectedY = pieces[selectedIdx].getY();
			// no need to deduce transform
		}
	}

	@Override
	public void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;

		//
		// draw the board
		//

		g.translate(3 * SQUARE_SIZE, SQUARE_SIZE);

		// draw the piece backgrounds
		for(int y = 0; y < board.getHeight(); y++)
			for(int x = 0; x < board.getWidth(); x++)
			{
				int idx = board.getBoardCache()[y][x];
				if(idx == -1)
					g.setColor(NO_PIECE_COLOR);
				else if(pieces[idx].isFixed())
					g.setPaint(FIXED_PIECE_PAINT);
				else if(idx == selectedIdx)
					g.setColor(SELECTED_PLACED);
				else
					g.setColor(PIECE_FILL_COLOR);

				g.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			}

		// draw borders
		g.setColor(BORDER_COLOR);
		g.drawRect(0, 0, board.getWidth() * SQUARE_SIZE, board.getHeight() * SQUARE_SIZE);
		for(int y = 0; y < board.getHeight(); y++)
			for(int x = 0; x < board.getWidth(); x++)
			{
				if(x + 1 < board.getWidth())
					if(board.getBoardCache()[y][x] != board.getBoardCache()[y][x + 1])
						g.drawLine((x + 1) * SQUARE_SIZE, y * SQUARE_SIZE, //
						(x + 1) * SQUARE_SIZE, (y + 1) * SQUARE_SIZE);

				if(y + 1 < board.getHeight())
					if(board.getBoardCache()[y][x] != board.getBoardCache()[y + 1][x])
						g.drawLine(x * SQUARE_SIZE, (y + 1) * SQUARE_SIZE, //
						(x + 1) * SQUARE_SIZE, (y + 1) * SQUARE_SIZE);
			}
		// draw the selected piece
		Piece sel = pieces[selectedIdx];
		if(!sel.isPlaced())
		{
			if(board.canPlacePiece(selectedIdx, selectedX, selectedY, selectedT))
				g.setColor(SELECTED_CAN_PLACE);
			else
				g.setColor(SELECTED_CANNOT_PLACE);

			for(int py = 0; py < sel.getHeight(); py++)
				for(int px = 0; px < sel.getWidth(); px++)
					if(sel.isAt(px, py))
					{
						int bx = selectedX + selectedT.x(px, py, sel);
						int by = selectedY + selectedT.y(px, py, sel);

						g.fillRect(bx * SQUARE_SIZE, by * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
		}

		//
		// draw spare pieces
		//
		g.translate(-3 * SQUARE_SIZE, (board.getHeight() + 1) * SQUARE_SIZE);

		int x = 0;
		int y = 0;
		int maxHeight = 0;
		g.setColor(PIECE_OFF_BOARD);
		for(int i = 0; i < pieces.length; i++)
			if(!pieces[i].isPlaced() && i != selectedIdx)
			{
				Piece p = pieces[i];

				// if this piece goes off the edge, then wrap to the next line
				if(x + p.getWidth() > board.getWidth() + 6)
				{
					x = 0;
					y += maxHeight + 1;
					maxHeight = 0;
				}

				// draw the piece
				for(int px = 0; px < p.getWidth(); px++)
					for(int py = 0; py < p.getHeight(); py++)
						if(p.isAt(px, py))
							g.fillRect((x + px) * SQUARE_SIZE, (y + py) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

				x += p.getWidth() + 1;
				maxHeight = Math.max(maxHeight, p.getHeight());
			}
	}
}
