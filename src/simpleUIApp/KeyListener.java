package simpleUIApp;

import javax.swing.JFrame;

import fr.ubordeaux.simpleUI.Application;
import fr.ubordeaux.simpleUI.KeyHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class KeyListener implements KeyHandler {

	private JFrame mFrame;
	

	public KeyListener(JFrame frame)
	{
		mFrame = frame;
	}

	@Override
	public JFrame getParentFrame() {
		return mFrame;
	}

	@Override
	public void keyPressed(char arg0) {

	}

	@Override
	public void keyReleased(char arg0) {

	}

	@Override
	public void keyTyped(char arg0) {
		switch (arg0) {
			case '+':
				break;
			case '-':
				break;
			case 's':
				Run.saveGame();
				break;
			case 'l':
				Run.loadGame();
				break;
			default:
				// do nothing
				break;
		}
	}

}
