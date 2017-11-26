package simpleUIApp;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fr.ubordeaux.simpleUI.Application;
import fr.ubordeaux.simpleUI.ApplicationRunnable;
import fr.ubordeaux.simpleUI.Arena;
import fr.ubordeaux.simpleUI.TimerRunnable;
import fr.ubordeaux.simpleUI.TimerTask;
import simpleUIApp.Planet.PlanetType;

public class Run implements ApplicationRunnable<Item> {

	private int width;
	private int height;
	private boolean isGO;
	private boolean isWon;
	ArrayList<Planet> planets;

	public Run(int width, int height, ArrayList<Planet> al) {
		this.width = width;
		this.height = height;
		this.planets = al;
		
	}

	@Override
	public void run(final Arena<Item> arg0, Collection<? extends Item> arg1) {
		MouseListener mouseHandler = new MouseListener();

		/*
		 * We build the graphical interface by adding the graphical component
		 * corresponding to the Arena - by calling createComponent - to a JFrame.
		 */
		final JFrame frame = new JFrame("Test Arena");

		/*
		 * This is our KeyHandler that will be called by the Arena in case of key events
		 */
		KeyListener keyListener = new KeyListener(frame);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(arg0.createComponent(this.width, this.height, mouseHandler, keyListener));
		frame.pack();
		frame.setVisible(true);
		this.isWon = false;
		this.isGO = false;

		/*
		 * We initially draw the component
		 */
		arg0.refresh();

		/*
		 * We ask the Application to call the following run function every seconds. This
		 * method just refresh the component.
		 */
		Application.timer(10, new TimerRunnable() {

			public void run(TimerTask timerTask) {
				arg0.refresh();
				for (Item item : arg1) {
					item.move();
				}
			}

		});
		
		// On met a jour les planètes et on teste la fin de partie
		Application.timer(500, new TimerRunnable() {

			public void run(TimerTask timerTask) {
				int cptIAP = 0, cptPP = 0;
				for (Planet p : planets) {
					p.update();
					
					// Si la partie n'est pas terminÃ©
					if(!isGO)
					{
						
						
						if(p.getType() == PlanetType.IA)
							cptIAP++;
						else if(p.getType() == PlanetType.PLAYER)
							cptPP++;
						
					}
				}
				
				if((cptIAP == 0 || cptPP == 0) && isGO == false){
					isGO = true;
					
					if(cptPP == 0){
						isWon = false;
						System.out.println("** YOU LOSE !  **");
					}
						
					else{
						isWon = true;
						System.out.println("** YOU WIN !  **");
					}
						
				}
			}

		});
		
		// on met a jour le nombre de vaisseau sur les planètes neutres
		Application.timer(1500, new TimerRunnable() {

			public void run(TimerTask timerTask) {
				for (Planet p : planets) {
					p.updateNeutral();
				}
			}

		});
		
		// On met a jour les actions de l'IA
		Application.timer(5000, new TimerRunnable() {

			public void run(TimerTask timerTask) {
				Planet.updateIA();
			}
		});
	}

}
