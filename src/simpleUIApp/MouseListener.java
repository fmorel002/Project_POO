package simpleUIApp;

import java.util.ArrayList;
import java.util.List;

import fr.ubordeaux.simpleUI.KeyPress;
import fr.ubordeaux.simpleUI.MouseHandler;
import simpleUIApp.Planet.PlanetType;

public class MouseListener implements MouseHandler<Item> {

	ArrayList<Item> dragList = new ArrayList<Item>();;

	public MouseListener() {
		super();
	}

	@Override
	public void mouseClicked(List<Item> arg0, KeyPress arg1) {
		System.out.println("Select " + arg0 + " " + arg1);
		for (Item testItem : arg0) {
			System.out.println("Mouse click " + testItem);
		}
	}

	@Override
	public void mouseDrag(List<Item> arg0, KeyPress arg1) {
		dragList = new ArrayList<Item>(arg0);
		System.out.println("Drag :" + dragList);
	}

	@Override
	public void mouseDragging(List<Item> arg0, KeyPress arg1) {
		if (!arg0.isEmpty())
			System.out.println("Dragging :" + arg0);
	}

	@Override
	public void mouseDrop(List<Item> arg0, KeyPress arg1) {
		System.out.println("Drag& Drop :" + dragList + " => " + arg0 + " using " + arg1.toString());
		if (!arg0.isEmpty()) {
			for (Item item : dragList) {
				
				// Pour chaque item, on teste si c'est une planète du joueur
				if (item instanceof Planet && ((Planet) item).getType() == PlanetType.PLAYER) 
				{
					// On change l'objectif de la planète et on lance la génération des vaissaux
					Planet p = (Planet) item;
					p.setObjective(arg0.get(0));
					p.generateShips(arg0.get(0), arg1.toString());
					
				// Pour chaque item on test si c'est un vaisseau et qu'il appartienne au joueur
				} else if (item instanceof SpaceShip && !((SpaceShip) item).IsIaShip()) {
					SpaceShip s = (SpaceShip) item;
					
					// On teste que arg0 soit bien une planète
					if (arg0.get(0) instanceof Planet) {
						// On met à jour l'esacadron correspondant vers son nouvel objectif
						Planet p = (Planet) arg0.get(0);
						s.setObjective(p);
						Planet.updateEscadronsObjective(s.getObjective(),s.getEscadronID());
					}

				}

			}
		}
	}

	@Override
	public void mouseOver(List<Item> arg0, KeyPress arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(List<Item> arg0, KeyPress arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println(arg0 + " using " + arg1.toString() + " wheel rotate " + arg2);
	}

}
