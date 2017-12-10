package simpleUIApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Entity extends Item implements Serializable{
	
	Color c;

	public Entity(double x, double y, int w, Color col) {
		super(x, y, w);
		c = col;
	}
	
	public void setColor(Color c)
	{
		this.c = c;
	}
	
	@Override
	public void move() {}

	/**
	 *  Permet de dessiner un élément graphique avec la bonne couleur. (Vert joueur, Rouge IA).
	 *
	 * @param arg0
	 * 			  L'élément graphique à dessiné.
	 */
	@Override
	public void draw(Graphics2D arg0) {
		Point2D pos = this.center;
		int x = (int) pos.getX(), y = (int) pos.getY(), w = this.getWidth();
		arg0.setColor(c);
		//arg0.fillRect(x - w / 2, y - w / 2, w, w);
		arg0.fillOval(x - w / 2, y - w / 2, w, w);
	}

	@Override
	public void setObjective(Item o) {
		
	}

	/**
	 *  Permet de savoir si un point est dans un élément de l'application.
	 *
	 * @param p
	 * 			Le point pour lequel il faut savoir s'il est dans l'Entity ou non.
	 * @return
	 * 			True s'il est dedans, False sinon.
	 */
	@Override
	public boolean contains(Point2D p) {
		return squareDistance(this.center, p) <= (getWidth() / 2) * (getWidth() / 2);
	}

	/**
	 *
	 * @param p1
	 * 			Point 1 pour le calcul.
	 * @param p2
	 * 			Point 2 pour le calcul.
	 * @return
	 * 			La somme des carrés des différences en x et y des 2 points.
	 */
	
	private static double squareDistance(Point2D p1, Point2D p2) {
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();
		return dx * dx + dy * dy;
	}
}
