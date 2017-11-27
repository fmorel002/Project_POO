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

				/*try {
					FileWriter out = new FileWriter(new File("./src/saves/saved.map"));
					for(Planet i : gameData){
						out.write(i.toSave());
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				break;
			default:
				// do nothing
				break;
		}
	}

}
