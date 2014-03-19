package spinnytea.clone._2048;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class key_2048 extends KeyAdapter {
	private final _2048 game;
	private final view_2048 view;

	@Override
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
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
