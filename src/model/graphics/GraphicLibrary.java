
package model.graphics;

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
	
	public void drawSquare(Point center, double length){
		int size=(int) length;
		
		graphics.fillRect(center.x-size/2, center.y-size/2, size-1, size-1);
	}
}
