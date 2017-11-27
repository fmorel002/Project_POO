package simpleUIApp;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Any graphical element that will be handle by the application.
 *
 */
abstract class Item implements Serializable{

	protected final Point2D center;
	private int width;

	public Item(double x, double y, int w) {
		center = new Point2D.Double(x, y);
		width = w;
	}

	public Point2D getLocation() {
		return center;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int w) {
		width = w;
	}

	public abstract void move();

	public abstract void draw(Graphics2D arg0);

	public abstract void setObjective(Item o);

	public abstract boolean contains(Point2D p);
}
