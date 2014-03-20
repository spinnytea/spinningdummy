package spinnytea.clone._2048.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.RequiredArgsConstructor;
import spinnytea.clone._2048._2048;

@RequiredArgsConstructor
public class key
extends KeyAdapter
{
	private final _2048 game;
	private final spinnytea.clone._2048.view.view view;

	@Override
	public void keyPressed(KeyEvent evt)
	{
		switch(evt.getKeyCode())
		{
		case KeyEvent.VK_LEFT:
			game.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			game.moveRight();
			break;
		case KeyEvent.VK_UP:
			game.moveUp();
			break;
		case KeyEvent.VK_DOWN:
			game.moveDown();
			break;
		}
		// TODO animation
		view.repaint();
	}
}
