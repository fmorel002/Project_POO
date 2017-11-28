package simpleUIApp;

import javax.swing.JFrame;

import fr.ubordeaux.simpleUI.KeyHandler;

import java.io.*;
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
					FileOutputStream outFile = new FileOutputStream("./src/saves/saved.map");
					try {
						ObjectOutputStream outStream = new ObjectOutputStream(outFile);
						for(Planet p : gameData){
							outStream.writeObject(p);
						}
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 'l':
				System.out.println("l for Loading has been typed");
				try {
					FileInputStream inFile = new FileInputStream("./src/saves/saved.map");
					try {
						ObjectInputStream inStream = new ObjectInputStream(inFile);
						boolean over = false;
						Object o;
						while(!over){
							try{
								o = inStream.readObject();
								System.out.println(o);
							}
							catch(Exception e){
								over = true;
							}
						}
						inStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//gameData.removeAll(gameData);
				break;
			default:
				// do nothing
				break;
		}
	}

}
