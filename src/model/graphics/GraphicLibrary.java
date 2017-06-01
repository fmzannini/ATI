
package model.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class GraphicLibrary {
	
	private Graphics2D graphics;
	
	
	public GraphicLibrary(Graphics2D graphics) {
		this.graphics = graphics;
	}

	public void drawCircle(Point center, double radius){
		int diameter=(int)Math.ceil(2*radius);
		graphics.fillOval(center.x-diameter/2,center.y-diameter/2, diameter-1, diameter-1);
	}
	
	public void drawCircleLine(Point center, double radius){
		int diameter=(int)Math.ceil(2*radius);
		Color aux=graphics.getColor();
		graphics.setColor(Color.RED);
		graphics.drawOval(center.x-diameter/2,center.y-diameter/2, diameter-1, diameter-1);
		graphics.setColor(aux);	
	}
	
	public void drawSquare(Point center, double length){
		int size=(int) length;
		graphics.fillRect(center.x-size/2, center.y-size/2, size-1, size-1);
	}
	
	public void drawLine(Point p1, Point p2){
		Color aux=graphics.getColor();
		graphics.setColor(Color.RED);
		graphics.drawLine(p1.x, p1.y, p2.x, p2.y);
		graphics.setColor(aux);	

	}
	
}
