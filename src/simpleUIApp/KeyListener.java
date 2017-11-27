package simpleUIApp;

import javax.swing.JFrame;

import fr.ubordeaux.simpleUI.KeyHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class KeyListener implements KeyHandler {

	private JFrame mFrame;
	private ArrayList<Planet> gameData;

	public KeyListener(JFrame frame, ArrayList<Planet> data)
	{
		mFrame = frame;
		gameData = data;
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
				System.out.println("+ has been typed");
				break;
			case '-':
				System.out.println("- has been typed");
				break;
			case 's':
				System.out.println("s for Saving has been typed");
				try {
					FileWriter out = new FileWriter(new File("./src/saves/saved.map"));
					for(Planet i : gameData){
						out.write(i.toSave());
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				// do nothing
				break;
		}
	}

}
