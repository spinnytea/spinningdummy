package spinnytea.clone._2048.controller;

import spinnytea.clone._2048._2048;
import spinnytea.clone._2048.view.Repaintable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RandomImpl
implements ActionListener
{
	private static final Random RAND = new Random();
	private final _2048 game;
	private final Repaintable view;
	private final int iterationsPerStep;

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if(game.isLose() || game.isWin())
			return;

		for(int i = 0; i < iterationsPerStep && !game.isLose() && !game.isWin(); i++)
		{
			// make a move
			switch(RAND.nextInt(4))
			{
			case 0:
				game.moveLeft();
				break;
			case 1:
				game.moveRight();
				break;
			case 2:
				game.moveUp();
				break;
			case 3:
				game.moveDown();
				break;
			}
		}

		// repaint the board
		view.repaint();
	}
}
